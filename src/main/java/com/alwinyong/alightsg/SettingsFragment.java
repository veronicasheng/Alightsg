package com.alwinyong.alightsg;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.alwinyong.alightsg.R;


public class SettingsFragment extends PreferenceFragment {

    private static String url = "http://cheeaun.github.io/busrouter-sg/data/2/bus-stops.json";
    private String jsonStr;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);

        Preference database_pref = (Preference) findPreference("download_database");
        database_pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                new getdatabase2().execute();

                return true;
            }
        });

    }


    public void getdatabase() {

        ProgressDialog pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Downloading required data. Please wait...");
        pDialog.setCancelable(false);
        // Toast.makeText(this.getActivity(), "No MRT station selected. Select a station to get started.", Toast.LENGTH_LONG).show();

        pDialog.show();
        ServiceHandler sh = new ServiceHandler();
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("SomeName", Context.MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.putString("String1", jsonStr);  // value is the string you want to save
        editor.commit();
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public class getdatabase2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            // Showing progress dialog

            //    super.onPreExecute();
            //   pDialog = new ProgressDialog(getActivity().getBaseContext());
            //    pDialog.setMessage("Downloading required data. Please wait...");
            //   pDialog.setCancelable(false);

            //   pDialog.show();

            Toast.makeText(getActivity().getBaseContext(), "Downloading database. Please wait a moment.", Toast.LENGTH_LONG).show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            SharedPreferences sharedPref = getActivity().getSharedPreferences("SomeName", Context.MODE_PRIVATE);
            Editor editor = sharedPref.edit();
            editor.putString("String1", jsonStr);  // value is the string you want to save
            editor.commit();
            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            Toast.makeText(getActivity().getBaseContext(), "Database download successful.", Toast.LENGTH_LONG).show();


        }
    }


}