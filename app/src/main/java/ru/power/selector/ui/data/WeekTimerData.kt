package ru.power.selector.ui.data

data class WeekTimerData(val id : WeekId, val sleepTime : Long = 0L, val wakeUpTime : Long = 0L, val switch : Boolean)
