package ru.power.selector.di

import dagger.Component
import ru.power.selector.Application
import ru.power.selector.di.module.ContextModule
import ru.power.selector.di.module.DomainModule
import ru.power.selector.di.module.UseCaseModule
import ru.power.selector.receiver.PowerOffReceiver
import ru.power.selector.receiver.PowerOnReceiver
import ru.power.selector.receiver.SetTimerReceiver
import ru.power.selector.ui.MainActivity
import ru.power.selector.ui.view_model.ViewModelFactory
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    UseCaseModule::class,
    DomainModule::class
])
@Singleton
interface AppComponent {

    fun inject(application: Application)

    fun inject(powerOffReceiver: PowerOffReceiver)

    fun inject(powerOnReceiver: PowerOnReceiver)

    fun inject(receiverActivity : ReceiverActivity)

    fun inject(setTimerReceiver : SetTimerReceiver)

    fun inject(mainActivity: MainActivity)

    fun inject(viewModelFactory: ViewModelFactory)
}