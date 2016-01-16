package com.alwinyong.alightsg;

/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

public class SearchTestingActivity extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            showToast("Searching for car parks ...");
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.

            Log.i(TAG, "Place details received: " + place.getName());

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