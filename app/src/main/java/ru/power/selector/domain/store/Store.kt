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
                setTime(AlarmTimer(week, switch, 0L))
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
        val day = calendar[Calendar.DAY_OF_WEEK]
        val weekDay : (Int)->WeekId = {
            when(it){
                0 -> { WeekId.Monthly }
                1 -> { WeekId.Tuesday }
                2 -> { WeekId.Wednesday }
                3 -> { WeekId.Thursday }
                4 -> { WeekId.Friday }
                5-> { WeekId.Saturday }
                6 -> { WeekId.Sunday }
                else -> {
                    WeekId.Monthly
                }
            }
        }

        val time = getTime(weekDay(day), switch)?.time
        return if (time != null && time <= System.currentTimeMillis()){
            getTime(weekDay(day + 1), switch)?.time
        } else {
            time
        }
    }

    private fun setTime(alarmTimer: AlarmTimer){
        preferences.edit().putLong("${alarmTimer.id}_${alarmTimer.switch}", alarmTimer.time).apply()
    }

    private fun getTime(weekId: WeekId, switch: Switch) : AlarmTimer? {
        val time = preferences.getLong("${weekId}_$switch", 0L)
        if (time == 0L){
            return null
        }
        return AlarmTimer(weekId, switch, time)
    }

    private fun getCurrentTime() : Calendar =
        Calendar.getInstance()
}