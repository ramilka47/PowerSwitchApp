package ru.power.selector.domain.store

interface IActionTimeStore {

    fun getNextWakeUpTime() : Long?

    fun getNextSleepTime() : Long?
}