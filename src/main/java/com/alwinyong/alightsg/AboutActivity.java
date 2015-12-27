package com.alwinyong.alightsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alwinyong.alightsg.R;

public class AboutActivity extends Activity {

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_about);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        menu.add(0, 0, 0, "Save").setIcon(android.R.drawable.ic_menu_revert).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case 0:
                Intent main = new Intent(AboutActivity.this, LandingActivity.class);
                startActivity(main);
                return true;
            default:
                return super.onOptionsItemSelected(paramMenuItem);

        }

    }

    public void onBackPressed() {
        finish();
    }

}
