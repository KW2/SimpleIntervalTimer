package com.kw2.kw2.sit;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by SAMSUNG on 2018-01-28.
 */

public class Button_Listener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getExtras().getInt("noti_id"));

        Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show();
    }
}
