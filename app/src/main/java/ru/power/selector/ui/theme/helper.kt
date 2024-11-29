package ru.power.selector.ui.theme

import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.Switch.*
import ru.power.selector.ui.data.WeekId
import ru.power.selector.ui.data.WeekId.Friday
import ru.power.selector.ui.data.WeekId.Monthly
import ru.power.selector.ui.data.WeekId.Saturday
import ru.power.selector.ui.data.WeekId.Sunday
import ru.power.selector.ui.data.WeekId.Thursday
import ru.power.selector.ui.data.WeekId.Tuesday
import ru.power.selector.ui.data.WeekId.Wednesday

fun WeekId.getWeekName() =
    when(this){
        Monthly -> "Пн"
        Tuesday -> "Вт"
        Wednesday -> "Ср"
        Thursday -> "Чт"
        Friday -> "Пт"
        Saturday -> "Сб"
        Sunday -> "Вс"
    }

fun Boolean.toSwitch() =
    when(this){
        true -> On
        false -> Off
    }

fun Switch.toBoolean() =
    when(this){
        On -> true
        Off -> false
    }