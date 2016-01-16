package com.alwinyong.alightsg;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class AlarmPlayerActivity extends Activity {

    public static final String TAG = "AlarmActivity";
    public static int nID;
    public static AlarmPlayerActivity instance2 = null;
    private String selection;
    private String transport;
    private MediaPlayer mMediaPlayer;
    private WakeLock wl;
    private boolean mVibrateEnabled;
    private boolean mSoundEnabled;
    private OnClickListener okListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                vibrator.cancel();
            } catch (NullPointerException e) {
            }
            if (mSoundEnabled) {
                mMediaPlayer.stop();
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(2);
            Intent mainactivity = new Intent(AlarmPlayerActivity.this, LandingActivity.class);
            setResult(2, mainactivity);
            startActivity(mainactivity);
            finish();

        }
    };
    private Vibrator vibrator;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        instance2 = this;

        loadSettings();

        SharedPreferences spOptions;
        SharedPreferences.Editor spOptionEditor;

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_alarm_player);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "My Tag");
        wl.acquire();


        selection = getIntent().getStringExtra("selection");
        transport = getIntent().getStringExtra("transport");
        TextView textView = (TextView) findViewById(R.id.mrtname);
        if (selection != null) {
            textView.setText(selection);
        }
        TextView transportLabel = (TextView) findViewById(R.id.transport_type_label);
        transportLabel.setText((transport.equals("bus") ? "Bus Stop" : "MRT Station"));

        updateNotification();
        Button okB = (Button) findViewById(R.id.ok);
        okB.setOnClickListener(okListener);

        wl.release();
    }

    public void updateNotification() {

        //SETTING NOTIFICATION ID AS 3 SO THAT NEW NOTIFICATIONS ARE NOT CREATED
        nID = 3;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //SETTING ACTION TO RETURN TO TRACKING SCREEN WHEN THE NOTIFICATION IS CLICKED
        //	Intent intent= new Intent(this, Alarm.class);
        //	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


        //SETTING ACTION TO STOP TRACKING WHEN THE STOP TRACKING BUTTON IS CLICKED IN NOTIFICATION ACTION BAR
        Intent stoptracking = new Intent(AlarmPlayerActivity.instance2, NotificationReceiver.class);
        stoptracking.putExtra("notificationId", nID);
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(this, 0,
                stoptracking, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(btPendingIntent).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("You are approaching " + selection + " " + (transport.equals("bus") ? "" : "MRT"))
                .setContentText("Swipe to dismiss notification.")
                .setOnlyAlertOnce(true)
                .setWhen(0);

        if (mVibrateEnabled) {
            long pattern[] = {0, 200, 100, 300, 400};

            //Start the vibration
            vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            //start vibration with repeated count, use -1 if you don't want to repeat the vibration
            vibrator.vibrate(pattern, 0);

        }

        if (mSoundEnabled) {
            playRingtone();
        }

        mNotificationManager.notify(nID, mBuilder.build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(AlarmPlayerActivity.this, TrackingBusActivity.class));
    }

    public void stoptracking() {
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (mSoundEnabled) {
            mMediaPlayer.stop();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
        Intent mainactivity = new Intent(AlarmPlayerActivity.this, LandingActivity.class);
        setResult(2, mainactivity);
        startActivity(mainactivity);
        finish();
    }

    private void loadSettings() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mVibrateEnabled = mySharedPreferences.getBoolean("vibrate_preference", true);
        mSoundEnabled = mySharedPreferences.getBoolean("sound_preference", false);

    }

    private void playRingtone() {
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        String alarms = prefs.getString("ringtone_preference", null);
        Uri uri = Uri.parse(alarms);
        playSound(this, uri);
    }


    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to play sound", e);
        }
    }

    public void onBackPressed() {
        stoptracking();
    }
}