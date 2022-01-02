package com.example.test2

import android.R
import android.R.id.message
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat


// broadcast receivers are system events that occuers wheather the app is on or not
// serice is background app thread.
class AirplaneModeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (isAirplaneModeOn(context.getApplicationContext())) {
            Toast.makeText(context, "AirPlane mode is on", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "AirPlane mode is off", Toast.LENGTH_SHORT).show()
        }

    }



    companion object {
        private fun isAirplaneModeOn(context: Context): Boolean {
            return Settings.System.getInt(
                context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) !== 0
        }
    }
}