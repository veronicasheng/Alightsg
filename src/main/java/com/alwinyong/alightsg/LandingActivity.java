package com.alwinyong.alightsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LandingActivity extends Activity {

    private View mrtbtn;
    private View busbtn;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.main_menu);

        this.mrtbtn = findViewById(R.id.btn_ride_mrt);
        this.mrtbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mrtscreen = new Intent(LandingActivity.this, SelectMRTActivity.class);
                startActivity(mrtscreen);
                finish();

            }
        });

        this.busbtn = findViewById(R.id.btn_ride_bus);
        this.busbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent busscreen = new Intent(LandingActivity.this, BusAlarmActivity.class);
                startActivity(busscreen);
                finish();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {

            case R.id.menu_settings:
                Intent preferences = new Intent(LandingActivity.this, SettingsActivity.class);
                startActivity(preferences);
                return true;
            case R.id.menu_about:
                Intent about = new Intent(LandingActivity.this, AboutActivity.class);
                startActivity(about);

            default:
                return super.onOptionsItemSelected(paramMenuItem);

        }

    }

    public void onBackPressed() {

        finish();

    }

}
