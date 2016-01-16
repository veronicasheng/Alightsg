package com.alwinyong.alightsg;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

//testing github

public class TrackingBusActivity extends FragmentActivity implements LocationListener, LocationSource {
    private static final LatLng SINGAPORE_LAT_LNG = new LatLng(1.290270, 103.851959);
    private static final String TONE = "tone";
    public static int nID;
    public static TrackingBusActivity instance = null;
    private static WakerSettings wakerSettings;
    private static Uri tone;
    public int proximityOn = 0;
    GPSTracker gps;
    NotificationManager notifManager;
    Handler handler = new Handler();
//    LocationClient mLocationClient;
    MarkerOptions markerOpts;
    //HANDLER THREAD        z
    Handler mUpdater = new Handler();
    private String selection;
    private String selectedradius;
    private int distanceDetect = 0;
    private double distance;
    private boolean showAlertDialog = false;
    private boolean killhandler = false;
    Runnable mUpdateView = new Runnable() {
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

                //SETTING THE CURRENT LOCATION AS LOCATION A FOR REFERENCE
                Location locationA = new Location("point A");
                locationA.setLatitude(latitude);
                locationA.setLongitude(longitude);

                //SETTING THE DESTINATION MRT'S LOCATION AS LOCATION B
                Location locationB = new Location("point B");
                locationB.setLatitude(wakerSettings.getLatitude());
                locationB.setLongitude(wakerSettings.getLongitude());

                //CALCULATING THE EUCLIDEAN DISTANCE BETWEEN CURRENT LOCATION AND DESTINATION MRT LOCATION
                distance = (int) locationA.distanceTo(locationB);
                updateNotification();

                //DISPLAYING THE DISTANCE REMAINING ON SCREEN
                TextView t1 = (TextView) findViewById(R.id.distanceremaining);

                //DISPLAY DISTANCE REMAINING IN METERS IF DISTANCE IS LESS THAN 1500M
                if (distance < 1500) {
                    t1.setText(distance + " m");
                }

                //DISPLAY DISTANCE REMAINING IN KM IF DISTANCE IS MORE THAN 1500M
                else if (distance > 1500) {
                    DecimalFormat df = new DecimalFormat("0");
                    distance = Double.valueOf(df.format(distance));
                    t1.setText(distance / 1000 + " km");
                }

                //IF REMAINING DISTANCE CALCULATED IS LESS THAN THE SELECTED RADIUS, SET DISTANCEDETECT FLAG AS 1
                if (distance - Integer.valueOf(selectedradius.toString()) < 0) {
                    distanceDetect = 1;
                }

                //DISPLAYING THE ACCURACY OF THE GPS
                TextView t2 = (TextView) findViewById(R.id.gpsaccuracytext);
                if (accuracy < 20) {
                    t2.setText("Very High (<20m)");
                } else if (accuracy > 20 && accuracy < 50) {
                    t2.setText("High (<50m)");
                } else if (accuracy > 50 && accuracy < 300) {
                    t2.setText("Average (<300m)");
                } else if (accuracy > 300 && accuracy < 500) {
                    t2.setText("Poor (<500m)");
                } else if (accuracy > 500) {
                    t2.setText("Very Poor (>500m)");
                }


                //PROXIMITYON = 1
                //DISTANCEDETECT = 1
                //JUST SHOW THE ALERT
                if (proximityOn == 1 && distanceDetect == 1) {
                    proximityOn = 0;
                    distanceDetect = 0;
                    showAlert();
                }


                //GPS ACCURACY = HIGH
                //DISTANCEDETECT = 1 (DISTANCE DETECT ALGORITHM CALCUATES THAT THE USER HAS ENTERED THE GEOFENCE RADIUS)
                //RESULT: IGNORE WHATEVER RESULT FROM LOCATION MANAGER AND SHOW THE ALERT
                if (distanceDetect == 1 && accuracy < 150) {
                    distanceDetect = 0;
                    showAlert();
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
                        showAlert();
                    }
                }


                //GPS ACCURACY = VERY LOW (DISTANCE DETECT ALGORITHM NOT RELIABLE)
                //PROXIMITYON = 1 (LOCATION MANAGER BELIEVES USER HAS ENTERED GEOFENCE)
                //RESULT: TAKE IT AS USER IS APPORACHING DESTINATION AND SHOW THE ALERT
                if (accuracy > 450 && proximityOn == 1) {
                    distanceDetect = 0;
                    proximityOn = 0;
                    showAlert();
                }


                //GPS ACCURACY VERY VERY LOW
                if (accuracy > 1300) {
                    int test = (int) (distance - (accuracy / 2));
                    if (test < Integer.valueOf(selectedradius.toString())) {
                        distanceDetect = 0;
                        proximityOn = 0;
                        showAlert();
                    }
                }
            }
            //RUN THE HANDLER THREAD EVERY 1 SEC
            mUpdater.postDelayed(this, 1000);
        }
    };
    private LatLngBounds center;
    private LocationManager locationManager;
    private ProximityIntentReceiver proximityIntentReceiver;
    private NotificationManager notificationManager;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private OnLocationChangedListener mListener;
    private WakeLock wl;
    private boolean wakelock_preference;
    //END OF ONCREATE METHOD
    private MediaPlayer mMediaPlayer;

    //SETTING UP INTERFACE
    public void onCreate(Bundle savedInstanceState) {
        // Log.d("Success", "Point 2");
        super.onCreate(savedInstanceState);
        // Log.d("Success", "Point 3");
        setContentView(R.layout.activity_tracking_bus);
        // Log.d("Success", "Point 4");
        loadSettings();

        //DEFINING CONTEXT FOR NOTIFICATION ACTION BUTTON
        instance = this;

        if (wakelock_preference) {
            wl = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");
            wl.acquire();
            Toast.makeText(this, "Wakelock enabled", Toast.LENGTH_LONG).show();
        }

        //SETTING UP GPS AND LOCATION SERVICE

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
            } else if (networkIsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            } else {
                showSettingsAlert();
            }
        } else {
            //Show some generic error dialog because something must have gone wrong with location manager.
        }



        //RETRIEVING VARIABLES BROUGHT OVER FROM MAIN ACTIVITY
        selection = getIntent().getStringExtra("selection");                //SELECTED MRT STATION
        // selectedradius = getIntent().getStringExtra("selectedradius");		//SELECTED RADIUS
        setUpMapIfNeeded();

        //SETTING UP DESTINATION STATION TEXT IN INTERFACE
        TextView textView = (TextView) findViewById(R.id.destinationstation);
        if (selection != null) {
            textView.setText(selection);
        }

        //SETTING UP STOP TRACKING BUTTON ACTIONS
        Button stoptrackingbtn = (Button) findViewById(R.id.busstarttrackingbtn);
        stoptrackingbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackingBusActivity.this.unregisterReceiver(TrackingBusActivity.this.proximityIntentReceiver);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(nID);
                if (wl != null && wl.isHeld()) {
                    wl.release();
                }
                mUpdater.removeCallbacks(mUpdateView);
                locationManager.removeUpdates(gps);

                Intent mainactivity = new Intent(TrackingBusActivity.this, LandingActivity.class);
                startActivity(mainactivity);
                finish();
            }


        });

        //CALLING THE FUNCTION TO ADD SETUP GEOFENCE USING LOCATION MANAGER
        TrackingBusActivity.this.addProximityAlert();

        //RUNNING THE HANDLER TO CONSTANTLY MONITOR USER'S LOCATION AND OTHER STATISTICS
        mUpdateView.run();
    }

    //FUNCTION TO SETUP GEOFENCE
    private void addProximityAlert() {
        PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("ProximityAlert"), 0);
        this.locationManager.addProximityAlert(wakerSettings.getLatitude(), wakerSettings.getLongitude(), Integer.valueOf(selectedradius.toString()), -1L, localPendingIntent);
        IntentFilter localIntentFilter = new IntentFilter("sg.mrtwaker.ProximityAlert");
        registerReceiver(this.proximityIntentReceiver, localIntentFilter);
    }
    //END OF HANDLER THREAD

    public WakerSettings getWakerSettings() {
        return wakerSettings;
    }


    public void updateNotification() {

        //SETTING NOTIFICATION ID AS 1 SO THAT NEW NOTIFICATIONS ARE NOT CREATED
        nID = 1;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //SETTING ACTION TO RETURN TO TRACKING SCREEN WHEN THE NOTIFICATION IS CLICKED
        Intent intent = new Intent(this, TrackingBusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        //SETTING ACTION TO STOP TRACKING WHEN THE STOP TRACKING BUTTON IS CLICKED IN NOTIFICATION ACTION BAR
        Intent stoptracking = new Intent(TrackingBusActivity.this, NotificationReceiver.class);
        stoptracking.putExtra("notificationId", nID);
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(this, 0,
                stoptracking, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT);

        //SETTING UP DESIRED NOTIFICATION
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Destination: " + selection)
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

        mListener = null;

        TrackingBusActivity.this.unregisterReceiver(TrackingBusActivity.this.proximityIntentReceiver);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(nID);

        wl = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, "fullwakelock");
        wl.acquire();

        Intent dialogIntent = new Intent(getBaseContext(), AlarmPlayerActivity.class);
        dialogIntent.putExtra("selection", selection);
        dialogIntent.putExtra("transport", "bus");
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);

        wl.release();
        finish();
    }

    //SETTING UP GOOGLE MAPS
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mMap = mMapFragment.getMap();

            // Check if we were successful in obtaining the map.

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setLocationSource(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SINGAPORE_LAT_LNG, 6.0f));
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(wakerSettings.getLatitude(),
                        wakerSettings.getLongitude()
                )
        ).title((selection)));

    }

    public void onNewIntent(Intent paramIntent) {
        if ((paramIntent != null) && (paramIntent.getExtras() != null) && (paramIntent.getExtras().getBoolean("Alert")))
            this.showAlertDialog = true;
    }

    protected void onResume() {
        super.onResume();

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
        //   updatePlaces(lastLoc);
    }


    private void updatePlaces(Location givenlocation) {

        double lat = givenlocation.getLatitude();
        double lng = givenlocation.getLongitude();

        LatLng lastLatLng = new LatLng(lat, lng);
    }


    private void killhandler() {
        killhandler = true;
    }

    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
    }

    @Override
    public void onPause() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

        super.onPause();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;

    }

    @Override
    public void deactivate() {
        mListener = null;

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null) {
            mMap.clear();
            mListener.onLocationChanged(location);
            //	updatePlaces(location);

            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng destiLaLn = new LatLng(wakerSettings.getLatitude(), wakerSettings.getLongitude());

        markerOpts = new MarkerOptions().position(destiLaLn).title(selection);
        mMap.addMarker(markerOpts).showInfoWindow();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
		/*	CircleOptions co = new CircleOptions();
            co.center(latLng);
            co.radius(gps.getAccuracy());
            co.fillColor(0x00000000);
            co.strokeColor(0xee4D2EFF);
            co.strokeWidth(4.0f);
         mMap.addCircle(co); */


        builder.include(latLng);
        builder.include(destiLaLn);
        LatLngBounds bounds = builder.build();
        //	mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, this.getResources().getDisplayMetrics().widthPixels,
        //           this.getResources().getDisplayMetrics().heightPixels, 0));

        if (mListener != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, mMapFragment.getView().getMeasuredWidth(),
                    mMapFragment.getView().getMeasuredHeight(), (int) (mMapFragment.getView().getMeasuredHeight() * 0.2)));

        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
    }


    //FUNCTION THAT WORKS WHEN STOP TRACKING BUTTON IN NOTIFICATION ACTION BAR IS CLICKED
    public void stoptracking() {
        if (wl != null && wl.isHeld()) {
            wl.release();
        }
        TrackingBusActivity.this.unregisterReceiver(TrackingBusActivity.this.proximityIntentReceiver);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(nID);
        mUpdater.removeCallbacks(mUpdateView);
        //	Intent intent = new Intent();
        locationManager.removeUpdates(gps);
        //setResult(2,intent);
        //TrackingScreen.this.finish();
        Intent mainactivity = new Intent(TrackingBusActivity.this, LandingActivity.class);
        //	setResult(2,mainactivity);
        startActivity(mainactivity);
        finish();
    }

    public void centerMap(LatLng latLng) {
        LatLng destiLaLn = new LatLng(wakerSettings.getLatitude(), wakerSettings.getLongitude());
        if (wakerSettings.getLatitude() < latLng.latitude || wakerSettings.getLongitude() < latLng.longitude)
            center = new LatLngBounds(destiLaLn, latLng);
        else {
            center = new LatLngBounds(latLng, destiLaLn);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(center, 300));

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Unable to get your location determined");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Click OK to go to settings and enable GPS.");

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


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    private void loadSettings() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        wakelock_preference = mySharedPreferences.getBoolean("wakelock_preference", false);
        selectedradius = mySharedPreferences.getString("radius_preference", "700");
    }

    public void onBackPressed() {

        stoptracking();

    }


}
