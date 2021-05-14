package com.bangkitproject.gituser.Alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bangkitproject.gituser.MainActivity
import com.bangkitproject.gituser.R
import java.util.*

class AlarmReceiver: BroadcastReceiver() {

    companion object{
        const val EXTRA_DAILY = "Daily Reminder"
        private const val EXTRA_MESSAGE = "message"
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_TIME = "15:08"
        private const val ID_DAILY = 100
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        showAlarmNotification(context, message)
    }

    private fun showAlarmNotification(context: Context?, message: String?) {

        val Id = "gituser"
        val name = "daily notification"

        val new = Intent(context, MainActivity::class.java)
        new.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context,0,new, PendingIntent.FLAG_ONE_SHOT)
        val notificationManagerCompat = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, Id)
            .setContentTitle("Daily Reminder")
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Id, name, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(Id)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(100, notification)

    }

    fun setReminder(context: Context, extraDaily: String, s: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val new = Intent(context, AlarmReceiver::class.java)

        new.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        new.putExtra(EXTRA_MESSAGE, s)
        new.putExtra(EXTRA_TYPE, extraDaily)

        val time = EXTRA_TIME.split(":".toRegex()).dropLastWhile {
            it.isEmpty()
            }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time[0]))
        calendar.set(Calendar.MINUTE,Integer.parseInt(time[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, new, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, "Reminder Set!", Toast.LENGTH_SHORT).show()
    }

    fun unsetReminder(context: Context?){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val new = Intent(context, AlarmReceiver::class.java)

        val requestCode = ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, new, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Reminder Off", Toast.LENGTH_SHORT).show()
    }


}