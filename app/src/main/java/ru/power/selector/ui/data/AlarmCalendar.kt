package ru.power.selector.ui.data

data class AlarmCalendar(val timers : List<AlarmTimer>)

class AlarmTimer(val id : WeekId, val switch: Switch, val hour : Int = Int.MAX_VALUE, val min : Int = Int.MAX_VALUE){

    fun isCheck() = hour == Int.MAX_VALUE && min == Int.MAX_VALUE
}

enum class WeekId{
    Monthly, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
}

enum class Switch{
    On, Off
}