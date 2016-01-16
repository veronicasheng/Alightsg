package com.alwinyong.alightsg;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProximityIntentReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 4097;
    private NotificationManager notificationManager;

    private Notification createNotification() {
        Notification localNotification = new Notification();
        localNotification.icon = 2130837507;
        localNotification.when = System.currentTimeMillis();
        localNotification.flags = (0x10 | localNotification.flags);
        localNotification.flags = (0x4 | localNotification.flags);
        localNotification.defaults = (0x1 | localNotification.defaults);
        localNotification.defaults = (0x2 | localNotification.defaults);
        localNotification.defaults = (0x4 | localNotification.defaults);
        return localNotification;
    }

    public void onReceive(Context paramContext, Intent paramIntent) {
        TrackingBusActivity localtrackingscreen = (TrackingBusActivity) paramContext;
        localtrackingscreen.proximityOn = 1;
    }
}