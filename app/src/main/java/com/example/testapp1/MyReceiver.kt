package com.example.testapp1

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService


class MyReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent?) {
        val task = Task()
        val  uri = task.uri
        val intent1 = Intent(context, Destination::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_IMMUTABLE)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)

        val builder = NotificationCompat.Builder(context,"TaskManagement")

            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("TaskManager")
            .setContentText("Manage to handle some task")

            //.setDefaults(NotificationCompat.DEFAULT_ALL)
            //.setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(pattern)
            //.setStyle(NotificationCompat.InboxStyle())
            //.setSound(alarmSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)



        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        //val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())

    }
}