package ru.power.selector

import android.app.Application
import ru.power.selector.di.AppComponent
import ru.power.selector.di.Initializer

class Application : Application() {

    companion object{
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = Initializer.init(this)
    }
}