package ru.power.selector.domain.store

import android.content.SharedPreferences
import ru.power.selector.domain.SWITCH
import ru.power.selector.domain.WEEK_LIST
import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.WeekId
import java.util.Calendar

class Store(private val preferences: SharedPreferences) :
    IAlarmTimerSetter,
    IAlarmTimerGetter,
    IActionTimeStore {

    override fun setAll(timers: List<AlarmTimer>) {
        timers.forEach {
            setTime(it)
        }
    }

    override fun set(timer: AlarmTimer) {
        setTime(timer)
    }

    override fun clear() {
        WEEK_LIST.forEach { week ->
            SWITCH.forEach { switch ->
                setTime(AlarmTimer(week, switch, Int.MAX_VALUE, Int.MAX_VALUE))
            }
        }
    }

    override fun getAll(): List<AlarmTimer> {
        val result = mutableListOf<AlarmTimer>()
        WEEK_LIST.forEach { week ->
            SWITCH.forEach { switch ->
                getTime(week, switch)?.let {
                    result.add(it)
                }
            }
        }
        return result
    }

    override fun get(id: WeekId): List<AlarmTimer> {
        val result = mutableListOf<AlarmTimer>()

        SWITCH.forEach {
            getTime(id, it)?.let { result.add(it) }
        }

        return result
    }

    override fun get(switch: Switch): List<AlarmTimer> {
        val result = mutableListOf<AlarmTimer>()

        WEEK_LIST.forEach {
            getTime(it, switch)?.let { result.add(it) }
        }

        return result
    }

    override fun get(id: WeekId, switch: Switch): AlarmTimer? =
        getTime(id, switch)

    override fun getNextWakeUpTime(): Long? =
        getTime(switch = Switch.On)

    override fun getNextSleepTime(): Long? =
        getTime(switch = Switch.Off)

    private fun getTime(switch: Switch) : Long? {
        val calendar = getCurrentTime()

        return getTimeByDay(calendar, switch)
    }

    private fun getTimeByDay(calendar: Calendar, switch: Switch, iter : Int = 0) : Long? {
        if (iter == 7) {
            return null
        }

        val weekId = weekDay(calendar[Calendar.DAY_OF_WEEK])
        val time = getTime(weekId, switch)
        return time?.let {
            val hour = it.hour
            val min = it.min

            return if (hour >= 24 || min >= 60)
                getTimeByDay(calendar.apply {
                    isYearIterEnd()
                    calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] + 1
                }, switch, iter + 1)
            else {
                //val calendar = getCurrentTime()
                val trueHour = calendar[Calendar.HOUR_OF_DAY]
                val trueMinute = calendar[Calendar.MINUTE]

                if (hour > trueHour) {
                    getCurrentTime().apply {
                        set(Calendar.DAY_OF_YEAR, calendar[Calendar.DAY_OF_YEAR])
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, min)
                    }.timeInMillis
                } else if (hour == trueHour) {
                    if (min > trueMinute) {
                        getCurrentTime().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, min)
                        }.timeInMillis
                    } else {
                        getTimeByDay(calendar.isYearIterEnd().apply {
                            calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] + 1
                        }, switch, iter + 1)
                    }
                } else {
                    getTimeByDay(calendar.isYearIterEnd().apply {
                        calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] + 1
                    }, switch, iter + 1)
                }
            }
        } ?: getTimeByDay(calendar.isYearIterEnd().apply {
            calendar[Calendar.DAY_OF_YEAR] = calendar[Calendar.DAY_OF_YEAR] + 1
        }, switch, iter + 1)
    }

    private fun Calendar.isYearIterEnd() : Calendar {
        val month = get(Calendar.MONTH)
        if (month != Calendar.DECEMBER)
            return this
        val day = get(Calendar.DAY_OF_MONTH)
        if (day != 31){
            return this
        }
        set(Calendar.YEAR, get(Calendar.YEAR) + 1)
        set(Calendar.DAY_OF_YEAR, 1)
        return this
    }

    private fun weekDay(day : Int) : WeekId = when(day){
        Calendar.MONDAY -> { WeekId.Monthly }
        Calendar.TUESDAY -> { WeekId.Tuesday }
        Calendar.WEDNESDAY -> { WeekId.Wednesday }
        Calendar.THURSDAY -> { WeekId.Thursday }
        Calendar.FRIDAY -> { WeekId.Friday }
        Calendar.SATURDAY-> { WeekId.Saturday }
        Calendar.SUNDAY -> { WeekId.Sunday }
        else -> {
            WeekId.Monthly
        }
    }

    private fun setTime(alarmTimer: AlarmTimer){
        preferences.edit().putString("${alarmTimer.id}_${alarmTimer.switch}", "${alarmTimer.hour}:${alarmTimer.min}").apply()
    }

    private fun getTime(weekId: WeekId, switch: Switch) : AlarmTimer? {
        val time = preferences.getString("${weekId}_$switch", "")
        if (time == null || time == "${Int.MAX_VALUE}:${Int.MAX_VALUE}") {
            return null
        }
        val times = time.split(":")

        if (times.size > 1)
            return AlarmTimer(
                weekId,
                switch,
                if (times[0].isNotBlank()) times[0].toInt() else 0,
                if (times[1].isNotBlank()) times[1].toInt() else 0
            ) else return null
    }

    private fun getCurrentTime() : Calendar =
        Calendar.getInstance()
}