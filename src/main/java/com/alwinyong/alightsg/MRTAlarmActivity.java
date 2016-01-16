package com.alwinyong.alightsg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MRTAlarmActivity extends Activity {

    public static final String PREFERENCE_SETTINGS_KEY = "APP_SETTINGS";
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name

    public static final int NOTIFICATION_ID = 4097;
    protected static final int code = 0;
    private static final long PROXIMITY_ALERT_EXPIRATION = -1L;
    private static final String PROXIMITY_ALERT_INTENT_NAME = "sg.mrtwaker.ProximityAlert";

    private static WakerSettings wakerSettings;
    private static ToggleButton alarmbtn2;
    public int proximityOn = 0;
    Boolean isInternetPresent = false;
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
    CheckBox prefCheckBox;
    CheckBox prefCheckBox2;
    private Button starttrackingbtn;
    private TextView autoCompleteTextView1;
    private Button selectstnbtn;
    private LocationManager locationManager;
    private int index = 0;
    private String selection;
    private ProximityIntentReceiver proximityIntentReceiver;
    private boolean showAlertDialog = false;
    private String selectedradius = "700";
    private boolean MRTEW = false;
    private boolean MRTNE = false;
    private boolean MRTNS = false;
    private boolean MRTCC = false;
    private boolean MRTDT = false;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_mrt_alarm);

        loadSettings();

        // SETTING UP DESTINATION AUTOCOMPLETE TEXTVIEW
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.textview);
        String[] stations = getResources().getStringArray(R.array.stations_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stations);
        textView.setAdapter(adapter);

        // SETTING UP SOURCE AUTOCOMPLETE TEXTVIEW
        AutoCompleteTextView textView_source = (AutoCompleteTextView) findViewById(R.id.textview_source);
        String[] source_stations = getResources().getStringArray(R.array.stations_array);
        ArrayAdapter<String> source_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, source_stations);
        textView_source.setAdapter(source_adapter);

        //SETTING UP FUNCTIONS
        this.proximityIntentReceiver = new ProximityIntentReceiver();
        wakerSettings = new WakerSettings(getSharedPreferences("APP_SETTINGS", 0));


        //SETTING UP SELECT SOURCE STATION MANUALLY BUTTON
        Button selectstnBtn_source = (Button) findViewById(R.id.selectstnbtn_source);
        selectstnBtn_source.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stationselector = new Intent(MRTAlarmActivity.this, MRTStationSelectorSource.class);
                startActivityForResult(stationselector, 5);
            }
        });

        //SETTING UP SELECT DESTINATION STATION MANUALLY BUTTON
        Button selectstnBtn = (Button) findViewById(R.id.selectstnbtn);
        selectstnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stationselector = new Intent(MRTAlarmActivity.this, SelectMRTStationActivity.class);
                startActivityForResult(stationselector, 5);
            }
        });


        //SETTING UP THE START TRACKING BUTTON
        this.starttrackingbtn = (Button) findViewById(R.id.stoptrackingbtn);
        this.starttrackingbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.textview);
                selection = textView.getText().toString();

                //	  List <String> list = Arrays.asList(LocationData.MRT_NAMES);
                //	  if(list.contains(selection)) {
                //		  index = list.indexOf(selection);
                //	  }

                List<String> list1 = Arrays.asList(LocationData.MRT_EW);
                List<String> list2 = Arrays.asList(LocationData.MRT_NS);
                List<String> list3 = Arrays.asList(LocationData.MRT_NE);
                List<String> list4 = Arrays.asList(LocationData.MRT_CC);
                List<String> list5 = Arrays.asList(LocationData.MRT_DT);

                if (list1.contains(selection)) {
                    MRTEW = true;
                    MRTNS = false;
                    MRTNE = false;
                    MRTCC = false;
                    MRTDT = false;
                    index = list1.indexOf(selection);
                } else if (list2.contains(selection)) {
                    MRTEW = false;
                    MRTNS = true;
                    MRTNE = false;
                    MRTCC = false;
                    MRTDT = false;
                    index = list2.indexOf(selection);
                } else if (list3.contains(selection)) {
                    MRTEW = false;
                    MRTNS = false;
                    MRTNE = true;
                    MRTCC = false;
                    MRTDT = false;
                    index = list3.indexOf(selection);
                } else if (list4.contains(selection)) {
                    MRTEW = false;
                    MRTNS = false;
                    MRTNE = false;
                    MRTCC = true;
                    MRTDT = false;
                    index = list4.indexOf(selection);
                } else if (list5.contains(selection)) {
                    MRTEW = false;
                    MRTNS = false;
                    MRTNE = false;
                    MRTCC = false;
                    MRTDT = true;
                    index = list5.indexOf(selection);
                }

                //NO MRT STATION SELECTED AND ATTEMPTED TO START TRACKING
                else if (selection.matches("")) {
                    Toast.makeText(MRTAlarmActivity.this.getBaseContext(), "No MRT station selected. Select a station to get started.", Toast.LENGTH_LONG).show();
                    textView.setText("");
                    index = 9999;
                    return;
                } else index = 9999;//SETTING A SPECIAL IDENTIFIER FOR INDEX FOR EASY IDENTIFYING

                //IF THE ENTERED MRT STATION NAME IS INVALID
                if (index == 9999) {
                    Toast.makeText(MRTAlarmActivity.this.getBaseContext(), "Invalid station name. Please try again.", Toast.LENGTH_LONG).show();
                    textView.setText("");
                    index = -2;
                    return;
                }

                //IF THE ENTERED MRT STATION NAME IS VALID
                if (index > -1) {

                    if (MRTEW) {
                        //STORING VALUES
                        MRTAlarmActivity.wakerSettings.saveLatitude(LocationData.MRT_EW_LAT[index]);
                        MRTAlarmActivity.wakerSettings.saveLongitude(LocationData.MRT_EW_LONG[index]);

                    } else if (MRTNS) {
                        MRTAlarmActivity.wakerSettings.saveLatitude(LocationData.MRT_NS_LAT[index]);
                        MRTAlarmActivity.wakerSettings.saveLongitude(LocationData.MRT_NS_LONG[index]);

                    } else if (MRTNE) {
                        MRTAlarmActivity.wakerSettings.saveLatitude(LocationData.MRT_NE_LAT[index]);
                        MRTAlarmActivity.wakerSettings.saveLongitude(LocationData.MRT_NE_LONG[index]);

                    } else if (MRTCC) {
                        MRTAlarmActivity.wakerSettings.saveLatitude(LocationData.MRT_CC_LAT[index]);
                        MRTAlarmActivity.wakerSettings.saveLongitude(LocationData.MRT_CC_LONG[index]);

                    } else if (MRTDT) {
                        MRTAlarmActivity.wakerSettings.saveLatitude(LocationData.MRT_DT_LAT[index]);
                        MRTAlarmActivity.wakerSettings.saveLongitude(LocationData.MRT_DT_LONG[index]);

                    }

                    Intent trackingscreen = new Intent(MRTAlarmActivity.this, TrackingBusActivity.class);
                    trackingscreen.putExtra("selection", selection);
                    startActivityForResult(trackingscreen, 5);
                    finish();
                }
            }

        });
    }
    //END OF ONCREATE METHOD


    //RETURNING BACK TO MAIN ACTIVITY FROM EITHER TRACKING SCREEN/STATION SELECTOR
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            //IF RETURNING FROM STATION SELECTOR (RESULT CODE = 1)
            if (resultCode == 1) {
                selection = data.getStringExtra("selectedstn");
                // Get a reference to the AutoCompleteTextView in the layout
                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.textview);
                // Get the string array
                String[] stations = getResources().getStringArray(R.array.stations_array);
                // Create the adapter and set it to the AutoCompleteTextView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stations);
                textView.setAdapter(null);
                textView.setText(selection);
                textView.setAdapter(adapter);

                selection = data.getStringExtra("selectedstn_source");
                // Get a reference to the AutoCompleteTextView in the layout
                AutoCompleteTextView textView_source = (AutoCompleteTextView) findViewById(R.id.textview_source);
                // Get the string array
                String[] source_stations = getResources().getStringArray(R.array.stations_array);
                // Create the adapter and set it to the AutoCompleteTextView
                ArrayAdapter<String> source_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, source_stations);
                textView_source.setAdapter(null);
                textView_source.setText(selection);
                textView_source.setAdapter(source_adapter);
            }

            //IF RETURNING FROM TRACKING SCREEN (RESULT CODE = 2)
            else if (resultCode == 2) {
                // Destination TextView
                index = 0;
                selection = null;
                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.textview);
                textView.setText("");

                // Source TextView
                index = 0;
                selection = null;
                AutoCompleteTextView textView_source = (AutoCompleteTextView) findViewById(R.id.textview_source);
                textView_source.setText("");
            }
        }

    }
    //END OF ONACTIVITYRESULT


    public WakerSettings getWakerSettings() {
        return wakerSettings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onNewIntent(Intent paramIntent) {
        if ((paramIntent != null) && (paramIntent.getExtras() != null) && (paramIntent.getExtras().getBoolean("Alert")))
            this.showAlertDialog = true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {

            case R.id.menu_settings:
                Intent preferences = new Intent(MRTAlarmActivity.this, SettingsActivity.class);
                startActivity(preferences);
                finish();
                return true;
            case R.id.menu_about:
                Intent about = new Intent(MRTAlarmActivity.this, AboutActivity.class);
                startActivity(about);
            default:
                return super.onOptionsItemSelected(paramMenuItem);

        }

    }

    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
    }


    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
    }


    private String getAppVersionName() {
        try {
            String str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            return str;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace();
        }
        return "";
    }

    private void loadSettings() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        selectedradius = mySharedPreferences.getString("radius_preference", "700");

    }

    public void onBackPressed() {

        Intent mainmenu = new Intent(MRTAlarmActivity.this, LandingActivity.class);
        startActivity(mainmenu);
        finish();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference);
        }

        @Override
        public void onResume() {
            super.onResume();
            getView().setBackgroundColor(Color.BLACK);

        }
    }

}
