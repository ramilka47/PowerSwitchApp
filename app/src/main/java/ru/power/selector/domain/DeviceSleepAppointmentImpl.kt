package ru.power.selector.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.power.selector.receiver.PowerOffReceiver

class DeviceSleepAppointmentImpl(private val context: Context) : IDeviceSleepAppointment {

    private val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setAlarm(time: Long) {
        val intent = Intent(context.applicationContext, PowerOffReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarm.set(AlarmManager.RTC_WAKEUP, time, pendingIntent!!)
    }
}