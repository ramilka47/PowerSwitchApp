package ru.power.selector.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.power.selector.domain.DeviceSleepAppointmentImpl
import ru.power.selector.domain.DeviceWakeupAppointmentImpl
import ru.power.selector.domain.IDeviceSleepAppointment
import ru.power.selector.domain.IDeviceWakeupAppointment
import ru.power.selector.domain.store.IActionTimeStore
import ru.power.selector.domain.store.IAlarmTimerGetter
import ru.power.selector.domain.store.IAlarmTimerSetter
import ru.power.selector.domain.store.Store
import ru.power.selector.ui.view_model.ViewModelFactory

@Module
class DomainModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("storage", Context.MODE_PRIVATE)

    @Provides
    fun provideStore(sharedPreferences: SharedPreferences) = Store(sharedPreferences)

    @Provides
    fun provideIStore(store: Store) : IActionTimeStore = store

    @Provides
    fun provideIAlarmTimerGetter(store: Store) : IAlarmTimerGetter = store

    @Provides
    fun provideIAlarmTimerSetter(store: Store) : IAlarmTimerSetter = store

    @Provides
    fun provideIDeviceSleepAppointment(context: Context) : IDeviceSleepAppointment =
        DeviceSleepAppointmentImpl(context)

    @Provides
    fun provideIDeviceWakeupAppointment(context: Context) : IDeviceWakeupAppointment =
        DeviceWakeupAppointmentImpl()

}