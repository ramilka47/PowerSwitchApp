package ru.power.selector.domain

interface IDeviceWakeupAppointment {
    
    fun setTime(time : Long)
    
    fun cancelTime()
}