package com.alwinyong.alightsg;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TrackingMRTActivity extends FragmentActivity implements LocationListener, LocationSource {

    private static final LatLng SINGAPORE_LAT_LNG = new LatLng(1.290270, 103.851959);
    public static TrackingMRTActivity instance = null;
    public static int nID;
    private static WakerSettings wakerSettings;
    public int proximityOn = 0;
    GPSTracker gps;
    MarkerOptions markerOpts;
    Gson gson = new Gson();
    // Timer
    long temp_timer = 0;
    Handler handler = new Handler();
    //HANDLER THREAD
    Handler locationUpdater = new Handler();

    private List<StationScan> stationScans;
    private StationScan destinationStationScan;
    private WifiReceiver wifiReceiver;

    private int MRT_Line;
    private int MRT_Start;
    private int MRT_End;
    private String[][] MRT_Array;
    private String destination;
    // Distance to Location
    private String selectedradius;
    private TextView startDestinationText;
    private TextView endDestinationText;
    private TextView timeText;
    private TextView distanceText;
    private TextView accuracyText;
    private TextView modeText;
    private TextView wifiText;

    // Wake Lock
    private boolean wakelock_preference;
    private WakeLock wake_lock;
    // GPS and Location
    private LatLngBounds center;
    private LocationManager locationManager;
    private ProximityIntentReceiver proximityIntentReceiver;
    private GoogleMap gmap;
    private SupportMapFragment gmapFragment;
    private OnLocationChangedListener locationChangeListener;
    private double distance;
    private boolean overToUnder;
    private boolean timerStarted = false;
    private int distanceDetect = 0;
    private boolean showAlertDialog = false;
    private boolean killhandler = false;
    Runnable setUpTimer = new Runnable() {
        @Override
        public void run() {
            new CountDownTimer(temp_timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    long temp_time = millisUntilFinished / 1000;
                    timeText.setText((temp_time / 60) + "m " + (temp_time % 60) + "s");
                }

                public void onFinish() {
                    showAlert();
                }

            }.start();
        }
    };
    Runnable locationUpdaterView = new Runnable() {
        @Override
        public void run() {
            //SWITCH TO KILL HANDLER THREAD
            if (killhandler)
                return;

            //MAIN HANDLER FUNCTIONS
            if (gps.canGetLocation()) {
                gps.getLocation();

                //GETTING THE CURRENT LOCATION'S LATITUDE AND LONGITUDE, AND GPS ACCURACY FROM SYSTEM
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                double accuracy = gps.getAccuracy();

                checkAndUpdateDistance(latitude, longitude);

                //DISPLAYING THE ACCURACY OF THE GPS
                if (accuracy < 20) {
                    accuracyText.setText("Very High (<20m)");
                } else if (accuracy > 20 && accuracy < 50) {
                    accuracyText.setText("High (<50m)");
                } else if (accuracy > 50 && accuracy < 300) {
                    accuracyText.setText("Average (<300m)");
                } else if (accuracy > 300 && accuracy < 500) {
                    accuracyText.setText("Poor (<500m)");
                } else if (accuracy > 500) {
                    accuracyText.setText("Very Poor (>500m)");
                }

                //JUST SHOW THE ALERT
                if (proximityOn == 1 && distanceDetect == 1) {
                    proximityOn = 0;
                    distanceDetect = 0;
                    if (overToUnder) {
                        if(!timerStarted){
                            setUpTimer.run();
                            timerStarted = true;
                        }
                    } else {
                        showAlert();
                    }
                }

                //GPS ACCURACY = HIGH
                //DISTANCEDETECT = 1 (DISTANCE DETECT ALGORITHM CALCUATES THAT THE USER HAS ENTERED THE GEOFENCE RADIUS)
                //RESULT: IGNORE WHATEVER RESULT FROM LOCATION MANAGER AND SHOW THE ALERT
                if (distanceDetect == 1 && accuracy < 150) {
                    distanceDetect = 0;
                    if (overToUnder) {
                        if(!timerStarted){
                            setUpTimer.run();
                            timerStarted = true;
                        }
                    } else {
                        showAlert();
                    }
                }

                //GPS ACCURACY = HIGH (DISTANCE DETECT ALGORITHM SHOULD BE WORKING AS INTENDED)
                //PROXIMITYON = 1 (LOCATION MANAGER BELIEVES USER HAS ENTERED GEOFENCE)
                //DISTANCEDETECT = 0 (DISTANCE DETECT ALGORITHM CALCUTES USER HAS NOT ENTERED GEOFENCE RADIUS)
                //RESULT: SUPPRESS THE PROXIMITY ALERT FROM LOCATION MANAGER
                if (accuracy < 100 && proximityOn == 1 && distanceDetect == 0) {
                    if (distance - Integer.valueOf(selectedradius.toString()) > 500) {
                        proximityOn = 0;
                    }
                }

                //IF GPS ACCURACY = AVERAGE
                //PROXIMITY ON = 1 (LOCATION MANAGER BELIEVES USER HAS ENTERED GEOFENCE)
                //CALCULATE IF DISTANCE REMAINING - ACCURACY VALUE IS LESS THAN THE GEOFENCE RADIUS
                //IF SO, SHOW THE ALERT
                if (proximityOn == 1 && accuracy > 150 && accuracy < 450) {
                    int buffer = (int) (distance - (accuracy / 2));
                    if (buffer < Integer.valueOf(selectedradius.toString())) {
                        distanceDetect = 0;
                        proximityOn = 0;
                        if (overToUnder) {
                            if(!timerStarted){
                                setUpTimer.run();
                                timerStarted = true;
                            }
                        } else {
                            showAlert();
                        }
                    }
                }

                //GPS ACCURACY = VERY LOW (DISTANCE DETECT ALGORITHM NOT RELIABLE)
                //PROXIMITYON = 1 (LOCATION MANAGER BELIEVES USER HAS ENTERED GEOFENCE)
                //RESULT: TAKE IT AS USER IS APPORACHING DESTINATION AND SHOW THE ALERT
                if (accuracy > 450 && proximityOn == 1) {
                    distanceDetect = 0;
                    proximityOn = 0;
                    if (overToUnder) {
                        if(!timerStarted){
                            setUpTimer.run();
                            timerStarted = true;
                        }
                    } else {
                        showAlert();
                    }
                }

                //GPS ACCURACY VERY VERY LOW
                if (accuracy > 1300) {
                    int test = (int) (distance - (accuracy / 2));
                    if (test < Integer.valueOf(selectedradius.toString())) {
                        distanceDetect = 0;
                        proximityOn = 0;
                        if (overToUnder) {
                            if(!timerStarted){
                                setUpTimer.run();
                                timerStarted = true;
                            }
                        } else {
                            showAlert();
                        }
                    }
                }

            } else {
                //Timer only mode
                accuracyText.setText("-");
                distanceText.setText("N/A");
            }

            locationUpdater.postDelayed(this, 1000);
        }
    }; //END OF HANDLER THREAD

    private void checkAndUpdateDistance(double latitude, double longitude) {
        //SETTING THE CURRENT LOCATION AS LOCATION A FOR REFERENCE
        Location locationA = new Location("point A");
        locationA.setLatitude(latitude);
        locationA.setLongitude(longitude);

        //SETTING THE DESTINATION MRT'S LOCATION AS LOCATION B
        Location locationB = new Location("point B");
        locationB.setLatitude(Float.parseFloat(MRT_Array[MRT_End][3]));
        locationB.setLongitude(Float.parseFloat(MRT_Array[MRT_End][4]));

        //CALCULATING THE EUCLIDEAN DISTANCE BETWEEN CURRENT LOCATION AND DESTINATION MRT LOCATION
        distance = (int) locationA.distanceTo(locationB);
        updateNotification();


        //DISPLAY DISTANCE REMAINING IN METERS IF DISTANCE IS LESS THAN 1500M
        if (distance < 1500) {
            distanceText.setText(distance + " m");
        }

        //DISPLAY DISTANCE REMAINING IN KM IF DISTANCE IS MORE THAN 1500M
        else if (distance > 1500) {
            DecimalFormat df = new DecimalFormat("0");
            distance = Double.valueOf(df.format(distance));
            distanceText.setText(distance / 1000 + " km");
        }

        //IF REMAINING DISTANCE CALCULATED IS LESS THAN THE SELECTED RADIUS, SET DISTANCEDETECT FLAG AS 1
        if (distance - Integer.valueOf(selectedradius.toString()) < 0) {
            distanceDetect = 1;
        }

    }

    // Initial Setup
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_mrt);

        getPreferences();

        //DEFINING CONTEXT FOR NOTIFICATION ACTION BUTTON
        instance = this;

        // Setting Wakelock
        if (wakelock_preference) {
            wake_lock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock tag");
            wake_lock.acquire();
            Toast.makeText(this, "Wakelock is Enabled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wakelock is Not Enabled", Toast.LENGTH_LONG).show();
        }

        // WiFi Handler
        final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiLock lock = wifi.createWifiLock(wifi.WIFI_MODE_SCAN_ONLY, "WifiLock Tag");
        lock.acquire();
        wifi.startScan();

        this.wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // GPS and Location Handlers
        this.locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        this.proximityIntentReceiver = new ProximityIntentReceiver();
        wakerSettings = new WakerSettings(getSharedPreferences("APP_SETTINGS", 0));
        gps = new GPSTracker(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gpsIsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
                Log.v("Logging", "Location Manager Using GPS");
            } else if (networkIsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
                Log.v("Logging", "Location Manager Using Network");
            } else {
                showSettingsAlert();
            }
        } else {
            Log.v("Logging", "Location Manager Error");
        }

        // SETTING UP STOP TRACKING BUTTON ACTIONS
        Button stoptrackingbtn = (Button) findViewById(R.id.stoptrackingbtn2);
        stoptrackingbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackingMRTActivity.this.unregisterReceiver(TrackingMRTActivity.this.proximityIntentReceiver);
                TrackingMRTActivity.this.unregisterReceiver(TrackingMRTActivity.this.wifiReceiver);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(nID);
                if (wake_lock != null && wake_lock.isHeld()) {
                    wake_lock.release();
                }
                locationUpdater.removeCallbacks(locationUpdaterView);
                locationManager.removeUpdates(gps);
                locationManager.removeUpdates(TrackingMRTActivity.this);

                Intent mainactivity = new Intent(TrackingMRTActivity.this, LandingActivity.class);
                startActivity(mainactivity);
                finish();
            }


        });

        // Set Views
        startDestinationText = (TextView) findViewById(R.id.txt_start_edit);
        endDestinationText = (TextView) findViewById(R.id.txt_end_edit);
        timeText = (TextView) findViewById(R.id.txt_time_edit);
        distanceText = (TextView) findViewById(R.id.txt_dist_edit);
        accuracyText = (TextView) findViewById(R.id.tv_gps_accuracy);

        modeText = (TextView) findViewById(R.id.tv_mode);
        wifiText = (TextView) findViewById(R.id.tv_wifi);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();

        MRT_Line = extras.getInt("line", 0);
        MRT_Start = extras.getInt("start", 0);
        MRT_End = extras.getInt("end", 0);

        switch (MRT_Line) {
            case 0:
                MRT_Array = MRTStations.NSLine;
                break;
            case 1:
                MRT_Array = MRTStations.EWLine;
                break;
            case 2:
                MRT_Array = MRTStations.CGLine;
                break;
            case 3:
                MRT_Array = MRTStations.NELine;
                break;
            case 4:
                MRT_Array = MRTStations.CCLine;
                break;
            case 5:
                MRT_Array = MRTStations.CELine;
                break;
            case 6:
                MRT_Array = MRTStations.DTLine;
                break;
            case 7:
                MRT_Array = MRTStations.BPLRT;
                break;
            case 8:
                MRT_Array = MRTStations.SELRT;
                break;
            case 9:
                MRT_Array = MRTStations.SWLRT;
                break;
            case 10:
                MRT_Array = MRTStations.PELRT;
                break;
            case 11:
                MRT_Array = MRTStations.PWLRT;
                break;
            //  case 8: MRT_Array = q_location_data.SKLRT; break;
            //  case 9: MRT_Array = q_location_data.PGLRT; break;
        }

        // Setup Text
        destination = MRT_Array[MRT_End][0];
        startDestinationText.setText(MRT_Array[MRT_Start][0]);
        endDestinationText.setText(destination);

        stationScans = new ArrayList<>();
        loadStationScans("scan");
        for (StationScan scan : stationScans) {
            if (scan.station.equalsIgnoreCase(destination)) {
                destinationStationScan = scan;
                break;
            }
        }

        if (MRT_Array[MRT_Start][2].equals("true") && MRT_Array[MRT_End][2].equals("true")) {
            // GPS only mode
            Log.v("Logging", "Starting at " + MRT_Array[MRT_Start][0] + " (Above Ground)");
            Log.v("Logging", "Ending at " + MRT_Array[MRT_End][0] + " (Above Ground)");
            modeText.setText("GPS-only");

        } else if (MRT_Array[MRT_Start][2].equals("false") && MRT_Array[MRT_End][2].equals("false")) {
            // Hybrid (GPS & Timer) mode
            Log.v("Logging", "Starting at " + MRT_Array[MRT_Start][0] + " (Below Ground)");
            Log.v("Logging", "Ending at " + MRT_Array[MRT_End][0] + " (Below Ground)");
            modeText.setText("Hybrid (GPS & Timer)");

            if (MRT_Start < MRT_End) {
                for (int l = MRT_Start; l < MRT_End; l++) {
                    temp_timer += Double.parseDouble(MRT_Array[l][5]) * 60 * 1000;
                }
            } else {
                for (int l = MRT_End; l < MRT_Start; l++) {
                    temp_timer += Double.parseDouble(MRT_Array[l][5]) * 60 * 1000;
                }
            }

            Log.v("Timer", "Timer set to " + temp_timer);
            setUpTimer.run();

        } else if (MRT_Array[MRT_Start][2].equals("false") && MRT_Array[MRT_End][2].equals("true")) {

            Log.v("Logging", "Starting at " + MRT_Array[MRT_Start][0] + " (Below Ground)");
            Log.v("Logging", "Ending at " + MRT_Array[MRT_End][0] + " (Above Ground)");
            modeText.setText("GPS-only");

        } else if (MRT_Array[MRT_Start][2].equals("true") && MRT_Array[MRT_End][2].equals("false")) {

            Log.v("Logging", "Starting at " + MRT_Array[MRT_Start][0] + " (Above Ground)");
            Log.v("Logging", "Ending at " + MRT_Array[MRT_End][0] + " (Below Ground)");

            /*

                Setting up timer to only count the under stations

            */

            int temp_mrt_index = 0;

            if (MRT_Start < MRT_End) {
                for (int l = MRT_Start; l <= MRT_End; l++) {
                    if (MRT_Array[l][2].equals("false")) {
                        temp_timer += Integer.parseInt(MRT_Array[l][5]) * 60 * 1000;
                    } else {
                        temp_mrt_index = l;
                    }
                }
            } else {
                for (int l = MRT_Start; l >= MRT_End; l--) {
                    if (MRT_Array[l][2].equals("false")) {
                        temp_timer += Integer.parseInt(MRT_Array[l][5]) * 60 * 1000;
                    } else {
                        temp_mrt_index = l;
                    }
                }
            }

            MRT_End = temp_mrt_index;

            Log.v("Timer", "Timer set to " + temp_timer);
            Log.v("Logging", "Timer will run when train reaches " + MRT_Array[MRT_End][0]);
            modeText.setText("Hybrid (Timer will start @ " + MRT_Array[MRT_End][0] + ")");

            /*

             Only 1 instance will ever have over to under

             This flag is to detect when the user has reach the
             last over station then it will run the timer

             */
            overToUnder = true;

        } else {

            Log.v("Logging", "Unknown tracking error");

        }

        //CALLING THE FUNCTION TO ADD SETUP GEOFENCE USING LOCATION MANAGER
        TrackingMRTActivity.this.addProximityAlert();

        //RUNNING THE HANDLER TO CONSTANTLY MONITOR USER'S LOCATION AND OTHER STATISTICS
        locationUpdaterView.run();

    } // END OF ONCREATE METHOD

    private boolean loadStationScans(String path) {

        try {
            String[] list = getAssets().list(path);
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if (!loadStationScans(path + "/" + file))
                        return false;
                }
            } else {
                InputStream is = getAssets().open(path);

                int size = is.available();

                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                stationScans.add(gson.fromJson(new String(buffer, "UTF-8"), StationScan.class));
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiLock lock = wifi.createWifiLock(wifi.WIFI_MODE_SCAN_ONLY, "WifiLock Tag");
            lock.acquire();
            wifi.startScan();
            int state = wifi.getWifiState();
            if (state == WifiManager.WIFI_STATE_ENABLED && destinationStationScan != null) {
                wifiText.setText("Available");
                List<ScanResult> results = wifi.getScanResults();

                for (ScanResult result : results) {
                    for (StationScanResult scan : destinationStationScan.results) {
                        if (result.BSSID.equalsIgnoreCase(scan.bssid)) {
                            Log.v("Logging", destinationStationScan.station + " WiFi detected");
                            Toast.makeText(getApplicationContext(), destinationStationScan.station + " WiFi detected", Toast.LENGTH_LONG).show();
                            showAlert();
                            lock.release();
                            break;
                        }
                    }
                }
            }
            else if (destinationStationScan != null) {
                wifiSettingsAlert();
            }
        }

    }

    //FUNCTION TO SETUP GEOFENCE
    private void addProximityAlert() {
        PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("ProximityAlert"), 0);
        this.locationManager.addProximityAlert(Double.parseDouble(MRT_Array[MRT_End][3]), Double.parseDouble(MRT_Array[MRT_End][4]), Integer.valueOf(selectedradius.toString()), -1L, localPendingIntent);
        IntentFilter localIntentFilter = new IntentFilter("sg.mrtwaker.ProximityAlert");
        registerReceiver(this.proximityIntentReceiver, localIntentFilter);
        registerReceiver(this.wifiReceiver, localIntentFilter);
    }

    public void updateNotification() {

        //SETTING NOTIFICATION ID AS 1 SO THAT NEW NOTIFICATIONS ARE NOT CREATED
        nID = 2;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //SETTING ACTION TO RETURN TO TRACKING SCREEN WHEN THE NOTIFICATION IS CLICKED
        Intent intent = new Intent(this, TrackingMRTActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        //SETTING ACTION TO STOP TRACKING WHEN THE STOP TRACKING BUTTON IS CLICKED IN NOTIFICATION ACTION BAR
        Intent stoptracking = new Intent(TrackingMRTActivity.this, NotificationReceiver.class);
        stoptracking.putExtra("notificationId", nID);
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(this, 0,
                stoptracking, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT);

        //SETTING UP DESIRED NOTIFICATION
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Destination: " + destination)
                .setOnlyAlertOnce(true)
                .setWhen(0)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop Tracking", btPendingIntent);

        //SHOWING DISTANCE REMAINING IN METERS WHEN DISTANCE IS LESS THAN 1500M
        if (distance < 1500) {
            mBuilder.setContentText("Distance Remaining: " + distance + " m");
        }

        //SHOWING DISTANCE REMAINING IN KILOMETERS WHEN DISTANCE IS MORE THAN 1500M
        else if (distance > 1500) {
            DecimalFormat df = new DecimalFormat("0");
            distance = Double.valueOf(df.format(distance));
            mBuilder.setContentText("Distance Remaining: " + distance / 1000 + " km");
        }

        //START THE NOTIFICATION
        mNotificationManager.notify(nID, mBuilder.build());
    }

    public void showAlert() {
        killhandler();
        locationManager.removeUpdates(gps);
        locationManager.removeUpdates(this);

        locationChangeListener = null;

        TrackingMRTActivity.this.unregisterReceiver(TrackingMRTActivity.this.proximityIntentReceiver);
        TrackingMRTActivity.this.unregisterReceiver(TrackingMRTActivity.this.wifiReceiver);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(nID);

        wake_lock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, "fullwakelock");
        wake_lock.acquire();

        Intent dialogIntent = new Intent(getBaseContext(), AlarmPlayerActivity.class);
        dialogIntent.putExtra("selection", destination);
        dialogIntent.putExtra("transport", "mrt");
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);

        wake_lock.release();
        finish();
    }

    private void getPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        wakelock_preference = sharedPref.getBoolean("wakelock_preference", false);
        selectedradius = sharedPref.getString("radius_preference", "700");
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is not enabled");

        // Setting Dialog Message
        alertDialog.setMessage("Unable to get your location determined. Click OK to go to settings & enable GPS.");

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.ic_menu_info_details);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void wifiSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("WiFi is not enabled");

        // Setting Dialog Message
        alertDialog.setMessage("Alerts will be less accurate. Click OK to go to settings & enable WiFi.");

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.ic_menu_info_details);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //SETTING UP GOOGLE MAPS
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gmap == null) {
            // Try to obtain the map from the SupportMapFragment.
            gmapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            gmap = gmapFragment.getMap();

            if (gmap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        gmap.getUiSettings().setMyLocationButtonEnabled(false);
        gmap.setMyLocationEnabled(true);
        gmap.setLocationSource(this);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SINGAPORE_LAT_LNG, 4.0f));
        // Setup Markers
        gmap.addMarker(new MarkerOptions().position(
                new LatLng(
                        Float.parseFloat(MRT_Array[MRT_Start][3]),
                        Float.parseFloat(MRT_Array[MRT_Start][4])
                )
        ).title(MRT_Array[MRT_Start][1] + " " + MRT_Array[MRT_Start][0]));

        gmap.addMarker(new MarkerOptions().position(
                new LatLng(
                        Float.parseFloat(MRT_Array[MRT_End][3]),
                        Float.parseFloat(MRT_Array[MRT_End][4])
                )
        ).title(MRT_Array[MRT_End][1] + " " + MRT_Array[MRT_End][0]));
    }

    public void onNewIntent(Intent paramIntent) {
        if ((paramIntent != null) && (paramIntent.getExtras() != null) && (paramIntent.getExtras().getBoolean("Alert")))
            this.showAlertDialog = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (locationManager != null) {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gpsIsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
            } else if (networkIsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            } else {
                //Show an error dialog that GPS is disabled...
            }
        } else {
            //Show some generic error dialog because something must have gone wrong with location manager.
        }
    }


    private void killhandler() {
        killhandler = true;
    }

    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        locationChangeListener = listener;
    }

    @Override
    public void deactivate() {
        locationChangeListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
            if (locationChangeListener != null) {
                gmap.clear();
                locationChangeListener.onLocationChanged(location);
                //	updatePlaces(location);

                if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng destiLaLn = new LatLng(
                    Float.parseFloat(MRT_Array[MRT_End][3]),
                    Float.parseFloat(MRT_Array[MRT_End][4])
            );
            markerOpts = new MarkerOptions().position(destiLaLn).title(destination);
            gmap.addMarker(markerOpts).showInfoWindow();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng);
            builder.include(destiLaLn);
            LatLngBounds bounds = builder.build();

                gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, gmapFragment.getView().getMeasuredWidth(),
                        gmapFragment.getView().getMeasuredHeight(), (int) (gmapFragment.getView().getMeasuredHeight() * 0.15)));
            }


    }

    //FUNCTION THAT WORKS WHEN STOP TRACKING BUTTON IN NOTIFICATION ACTION BAR IS CLICKED
    public void stoptracking() {
        if (wake_lock != null && wake_lock.isHeld()) {
            wake_lock.release();
        }
        TrackingMRTActivity.this.unregisterReceiver(TrackingMRTActivity.this.proximityIntentReceiver);
        TrackingMRTActivity.this.unregisterReceiver(TrackingMRTActivity.this.wifiReceiver);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(nID);
        locationUpdater.removeCallbacks(locationUpdaterView);
        //	Intent intent = new Intent();
        locationManager.removeUpdates(gps);
        locationManager.removeUpdates(this);
        //setResult(2,intent);
        //TrackingScreen.this.finish();
        Intent mainactivity = new Intent(TrackingMRTActivity.this, LandingActivity.class);
        //	setResult(2,mainactivity);
        startActivity(mainactivity);
        finish();
    }

    public void onBackPressed() {
        /*
        Intent station_menu = new Intent(q_mrt_tracking.this, activty_select_mrt.class);
        startActivity(station_menu);
        finish();
        */
        stoptracking();
    }

    public class StationScan {
        private String station;
        private List<StationScanResult> results;
    }

    public class StationScanResult {
        private String ssid;
        private String bssid;
        private String capabilities;
        private String frequency;
    }
}
