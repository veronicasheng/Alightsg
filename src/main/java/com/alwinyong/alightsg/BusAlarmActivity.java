package com.alwinyong.alightsg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BusAlarmActivity extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap gmap;
    private SupportMapFragment gmapFragment;

    // JSON keys
    private static final String TAG_no = "no";
    private static final String TAG_lat = "lat";
    private static final String TAG_long = "long";
    private static final String TAG_name = "name";
    private static String url = "http://cheeaun.github.io/busrouter-sg/data/2/bus-stops.json";
    private static WakerSettings wakerSettings;
    JSONObject jsonObject;
    JSONArray array;

    //private String stopnumberarray[];
    //private String latarray[];
    //private String longarray[];
    //private String namearray[];
    JSONArray services = null;
    ArrayList<HashMap<String, String>> busList = new ArrayList<HashMap<String, String>>(); // used to display in list view
    private ProgressDialog pDialog;
    private ArrayList<String> stopnumberarray = new ArrayList<String>();
    private ArrayList<String> latarray = new ArrayList<String>();
    private ArrayList<String> longarray = new ArrayList<String>();
    private ArrayList<String> namearray = new ArrayList<String>();
    private String nameselection;
    private String selectedradius = "700";
    private String numberselection;
    private int index = 0;
    private Button starttrackingbtn;
    private String retrievedString;
    private String jsonStr;
    private Boolean database_preference;

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;

    private ImageButton mButtonSearch;
    private AutoCompleteTextView mAutocompleteView;

    private TextView mPlaceDetailsText;
    private TextView mPlaceDetailsAttribution;

    private final static String TAG = "SearchActivity";
    private static String mPlaceId = null;

    private static final LatLngBounds BOUNDS_GREATER_SINGAPORE = new LatLngBounds(
            new LatLng(1.240248, 103.613553), new LatLng(1.460600, 104.074292));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_alarm);

        loadPref();
        wakerSettings = new WakerSettings(getSharedPreferences("APP_SETTINGS", 0));

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        database_preference = mySharedPreferences.getBoolean("database_preference", true);

        SharedPreferences sharedPref = getSharedPreferences("jsonstr", Context.MODE_PRIVATE);
        jsonStr = sharedPref.getString("jsonstring", "nth");

        if (database_preference == true) {
            if (jsonStr == "nth") {
                new GetDatabase().execute();
            } else
                new SetupDatabase().execute();


        } else
            new GetDatabase().execute();

        AutoCompleteTextView stopnametextview = (AutoCompleteTextView) findViewById(R.id.busstoptextview);
        AutoCompleteTextView numbertextview = (AutoCompleteTextView) findViewById(R.id.numbertextview);

        stopnametextview.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, namearray);
        stopnametextview.setAdapter(adapter1);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stopnumberarray);
        numbertextview.setAdapter(adapter2);

        stopnametextview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                AutoCompleteTextView stopnametextview = (AutoCompleteTextView) findViewById(R.id.busstoptextview);
                nameselection = stopnametextview.getText().toString();
                index = namearray.indexOf(nameselection);

                AutoCompleteTextView numbertextview = (AutoCompleteTextView) findViewById(R.id.numbertextview);
                numbertextview.setText(stopnumberarray.get(index));
                numberselection = null;

            }
        });

        numbertextview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                AutoCompleteTextView numbertextview = (AutoCompleteTextView) findViewById(R.id.numbertextview);
                numberselection = numbertextview.getText().toString();
                index = stopnumberarray.indexOf(numberselection);

                AutoCompleteTextView stopnametextview = (AutoCompleteTextView) findViewById(R.id.busstoptextview);
                stopnametextview.setText(namearray.get(index));
                nameselection = null;

            }
        });

        Button selectstopBtn = (Button) findViewById(R.id.selectstopmanuallybtn);
        selectstopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent busstopselector = new Intent(BusAlarmActivity.this, BusStopSelector.class);
                busstopselector.putExtra("busstopnames", namearray);
                busstopselector.putExtra("stopnumbers", stopnumberarray);
                startActivityForResult(busstopselector, 5);
            }
        });

        this.starttrackingbtn = (Button) findViewById(R.id.busstarttrackingbtn);
        this.starttrackingbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AutoCompleteTextView stopnametextview = (AutoCompleteTextView) findViewById(R.id.busstoptextview);
                AutoCompleteTextView numbertextview = (AutoCompleteTextView) findViewById(R.id.numbertextview);
                nameselection = stopnametextview.getText().toString();
                index = namearray.indexOf(nameselection);

                if (index < 0) {
                    index = stopnumberarray.indexOf(numbertextview.getText().toString());
                    if (index < 0) {
                        stopnametextview.setText("");
                        numbertextview.setText("");
                        Toast.makeText(BusAlarmActivity.this.getBaseContext(), "Invalid bus stop selected. Please select a bus stop to continue.", Toast.LENGTH_LONG).show();
                    } else {
                        nameselection = namearray.get(index);
                        startAlarm();
                    }
                } else {
                    startAlarm();
                }
            }
        });

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        mButtonSearch = (ImageButton)findViewById(R.id.searchbutton2);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (null != mPlaceId) {
                    Log.d(TAG, "mPlaceId: " + mPlaceId);
                    requestPlaceInfo();
                } else {
                    showToast("Please enter a location before search.");
                }
            }
        });


        //###################
        // about the autoCompleteTextView
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.searchblank2);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_SINGAPORE, null);
        mAutocompleteView.setAdapter(mAdapter);
    }

    private void startAlarm() {
        Float latf = Float.parseFloat(latarray.get(index));
        Float longf = Float.parseFloat(longarray.get(index));

        BusAlarmActivity.wakerSettings.saveLatitude(latf);
        BusAlarmActivity.wakerSettings.saveLongitude(longf);
        BusAlarmActivity.wakerSettings.saveMrtPosition(index);

        Intent trackingscreen = new Intent(BusAlarmActivity.this, TrackingBusActivity.class);
        trackingscreen.putExtra("selection", nameselection);
        trackingscreen.putExtra("selectedradius", selectedradius);
        trackingscreen.putExtra("index", index);
        startActivityForResult(trackingscreen, 5);
        finish();
    }

    private void loadPref() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        selectedradius = mySharedPreferences.getString("radius_preference", "700");

    }

    public WakerSettings getWakerSettings() {
        return wakerSettings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {

            case R.id.menu_settings:
                Intent preferences = new Intent(BusAlarmActivity.this, SettingsActivity.class);
                startActivity(preferences);
                finish();
                return true;
            case R.id.menu_about:
                Intent about = new Intent(BusAlarmActivity.this, AboutActivity.class);
                startActivity(about);
            default:
                return super.onOptionsItemSelected(paramMenuItem);

        }
    }

    public void onBackPressed() {

        Intent mainmenu = new Intent(BusAlarmActivity.this, LandingActivity.class);
        startActivity(mainmenu);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            //IF RETURNING FROM STATION SELECTOR (RESULT CODE = 1)
            if (resultCode == 1) {
                nameselection = data.getStringExtra("selectedbusstop");
                numberselection = data.getStringExtra("selectedbusstopindex");

                AutoCompleteTextView stopnametextview = (AutoCompleteTextView) findViewById(R.id.busstoptextview);
                AutoCompleteTextView numbertextview = (AutoCompleteTextView) findViewById(R.id.numbertextview);

                stopnametextview.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, namearray);
                stopnametextview.setAdapter(adapter1);


                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stopnumberarray);
                numbertextview.setAdapter(adapter2);


                stopnametextview.setAdapter(null);
                stopnametextview.setText(nameselection);
                stopnametextview.setAdapter(adapter1);

                numbertextview.setAdapter(null);
                numbertextview.setText(numberselection);
                numbertextview.setAdapter(adapter2);


            }


        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    public class GetDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            // Showing progress dialog

            super.onPreExecute();
            pDialog = new ProgressDialog(BusAlarmActivity.this);
            pDialog.setMessage("Downloading bus stop database. Please wait...");
            pDialog.setCancelable(false);

            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            SharedPreferences sharedPref = getSharedPreferences("jsonstr", Context.MODE_PRIVATE);
            Editor editor = sharedPref.edit();
            editor.putString("jsonstring", jsonStr);  // value is the string you want to save
            editor.commit();


            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray array = new JSONArray(jsonStr);
                    //	stopnumberarray = new String[array.length()];
                    //	latarray = new String[array.length()];
                    //	longarray = new String[array.length()];
                    //	namearray = new String[array.length()];
                    for (int i = 0; i < array.length(); i++)  // using for loop for parsing
                    {

                        JSONObject c = array.getJSONObject(i);
                        String no = c.getString("no");
                        String lat = c.getString("lat");
                        String lon = c.getString("lng");
                        String name = c.getString("name");

                        stopnumberarray.add(no);
                        latarray.add(lat);
                        longarray.add(lon);
                        namearray.add(name);

                        //     latarray[i] = c.getString("lat");
                        //     longarray[i] = c.getString("lng");
                        //     namearray[i] = c.getString("name");
                        // If you want to show your parsed value in list view add the values into the array list

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_no, no);
                        map.put(TAG_lat, lat);
                        map.put(TAG_long, lon);
                        map.put(TAG_name, name);

                        // adding HashList to ArrayList
                        busList.add(map);

                	/*JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    services = jsonObj.getJSONArray(TAG_SERVICES);

                    // looping through All Contacts
                    for (int i = 0; i < services.length(); i++) {
                        JSONObject c = services.getJSONObject(i);



                        String no = c.getString(TAG_no);
                        String routes = c.getString(TAG_routes);
                        String type = c.getString(TAG_type);
                        String operator = c.getString(TAG_operator);
                        String name = c.getString(TAG_name);




                        // tmp hashmap for single contact
                        HashMap<String, String> bus = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        bus.put(TAG_no, no);
                        bus.put(TAG_routes, routes);
                        bus.put(TAG_type, type);
                        bus.put(TAG_operator, operator);
                        bus.put(TAG_name, name);

                        buslist.add(bus);
                    } */

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


        }
    }

    public class SetupDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            // Showing progress dialog

            super.onPreExecute();
            pDialog = new ProgressDialog(BusAlarmActivity.this);
            pDialog.setMessage("Setting up bus database. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            if (jsonStr != null) {
                try {
                    JSONArray array = new JSONArray(jsonStr);

                    for (int i = 0; i < array.length(); i++)  // using for loop for parsing
                    {

                        JSONObject c = array.getJSONObject(i);
                        String no = c.getString("no");
                        String lat = c.getString("lat");
                        String lon = c.getString("lng");
                        String name = c.getString("name");

                        stopnumberarray.add(no);
                        latarray.add(lat);
                        longarray.add(lon);
                        namearray.add(name);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


        }
    }
    //END OF ONACTIVITYRESULT

    // google map apis
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            mPlaceId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);
        }
    };

    private void requestPlaceInfo(){
        /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, mPlaceId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        mPlaceId = null;
        Log.i(TAG, "Called getPlaceById to get Place details for " + mPlaceId);
    }

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            showToast("Searching for locations ...");
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            Log.i(TAG, "Place details received: " + place.getName());

            LatLng latLng = place.getLatLng();
            showToast("latlng: " + latLng.toString());

            places.release();
        }
    };


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


    public void showToast(String text) {
        Toast toast;
        int duration = Toast.LENGTH_SHORT;

        if (null != text)
        {
            toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

}