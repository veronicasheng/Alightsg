package com.alwinyong.alightsg;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("notificationId", 0);

        if (notificationId == 1) {
            TrackingBusActivity localtrackingscreen = TrackingBusActivity.instance;
            localtrackingscreen.stoptracking();

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        } else if (notificationId == 2) {
            TrackingMRTActivity localtrackingscreen = TrackingMRTActivity.instance;
            localtrackingscreen.stoptracking();

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        } else if (notificationId == 3) {
            AlarmPlayerActivity localalarm = AlarmPlayerActivity.instance2;
            localalarm.stoptracking();

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }
    }


}
