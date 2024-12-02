package ru.power.selector.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.power.selector.domain.IDeviceSleepAppointment
import ru.power.selector.domain.IDeviceWakeupAppointment
import ru.power.selector.domain.store.IActionTimeStore
import ru.power.selector.domain.store.IAlarmTimerGetter
import ru.power.selector.domain.store.IAlarmTimerSetter
import ru.power.selector.domain.use_case.ClearAlarmTimersUseCase
import ru.power.selector.domain.use_case.DeviceBootCompleteUseCase
import ru.power.selector.domain.use_case.DeviceToSleepUseCase
import ru.power.selector.domain.use_case.GetTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetAllTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetAllTimerBySwitchUseCase
import ru.power.selector.domain.use_case.SetTimeToAlarmUseCase

@Module
class UseCaseModule {

    @Provides
    fun provideDeviceBootComplete(
        iStore: IActionTimeStore,
        iDeviceSleepAppointment: IDeviceSleepAppointment
    ) =
        DeviceBootCompleteUseCase(iStore, iDeviceSleepAppointment)

    @Provides
    fun provideDeviceToSleep(context: Context,
                             iActionTimeStore : IActionTimeStore,
                             iDeviceWakeupAppointment: IDeviceWakeupAppointment) =
        DeviceToSleepUseCase(context, iActionTimeStore, iDeviceWakeupAppointment)

    @Provides
    fun provideClearAlarmTimersUseCase(iAlarmTimerSetter: IAlarmTimerSetter) =
        ClearAlarmTimersUseCase(iAlarmTimerSetter)

    @Provides
    fun provideGetTimeToAlarmUseCase(iAlarmTimerGetter: IAlarmTimerGetter) =
        GetTimeToAlarmUseCase(iAlarmTimerGetter)

    @Provides
    fun provideSetAllTimeToAlarmUseCase(iAlarmTimerSetter: IAlarmTimerSetter) =
        SetAllTimeToAlarmUseCase(iAlarmTimerSetter)

    @Provides
    fun provideSetTimeToAlarmUseCase(iAlarmTimerSetter: IAlarmTimerSetter) =
        SetTimeToAlarmUseCase(iAlarmTimerSetter)

    @Provides
    fun provideSetAllTimerUseCase(setAllTimeToAlarmUseCase: SetAllTimeToAlarmUseCase) =
        SetAllTimerBySwitchUseCase(setAllTimeToAlarmUseCase)
}