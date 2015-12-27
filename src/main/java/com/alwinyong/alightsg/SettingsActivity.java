package com.alwinyong.alightsg;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alwinyong.alightsg.R;

public class SettingsActivity extends Activity {

    private static String url = "http://cheeaun.github.io/busrouter-sg/data/2/bus-stops.json";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // load up the preferences fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);

        menu.add(0, 0, 0, "Save").setIcon(android.R.drawable.ic_menu_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {

            case 0:
                Intent main = new Intent(SettingsActivity.this, LandingActivity.class);
                startActivity(main);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(paramMenuItem);

        }

    }

    public void onBackPressed() {
        Intent main = new Intent(SettingsActivity.this, LandingActivity.class);
        startActivity(main);
        finish();

    }

    public void getdatabase() {
        ProgressDialog pDialog = new ProgressDialog(SettingsActivity.this);
        pDialog.setMessage("Downloading required data. Please wait...");
        pDialog.setCancelable(false);
        //  Toast.makeText(SettingsActivity.this.getBaseContext(), "No MRT station selected. Select a station to get started.", Toast.LENGTH_LONG).show();

        //    pDialog.show();
        //   ServiceHandler sh = new ServiceHandler();
        //   String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

        SharedPreferences sharedPref = getSharedPreferences("SomeName", Context.MODE_PRIVATE);
        //   Editor editor = sharedPref.edit();
        //    editor.putString("String1", jsonStr);  // value is the string you want to save
        //    editor.commit();
        //     if (pDialog.isShowing())
        //          pDialog.dismiss();
    }

}