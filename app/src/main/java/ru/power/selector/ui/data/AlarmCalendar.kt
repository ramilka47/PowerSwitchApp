package ru.power.selector.ui.data

data class AlarmCalendar(val timers : List<AlarmTimer>)

class AlarmTimer(val id : WeekId, val switch: Switch, val time : Long)

enum class WeekId{
    Monthly, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
}

enum class Switch{
    On, Off
}