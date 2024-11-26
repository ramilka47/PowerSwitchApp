package ru.power.selector.di

import android.content.Context
import ru.power.selector.di.module.ContextModule

object Initializer {

    fun init(context: Context) : AppComponent =
        DaggerAppComponent.builder()
            .contextModule(ContextModule(context))
            .build()

}