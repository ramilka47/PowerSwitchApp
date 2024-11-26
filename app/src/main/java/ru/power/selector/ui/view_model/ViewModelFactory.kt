package ru.power.selector.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.power.selector.di.AppComponent
import ru.power.selector.domain.use_case.ClearAlarmTimersUseCase
import ru.power.selector.domain.use_case.GetTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetAllTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetTimeToAlarmUseCase
import javax.inject.Inject

class ViewModelFactory(appComponent: AppComponent) : ViewModelProvider.Factory {

    @Inject
    lateinit var clearAlarmTimersUseCase: ClearAlarmTimersUseCase

    @Inject
    lateinit var getTimeToAlarmUseCase: GetTimeToAlarmUseCase

    @Inject
    lateinit var setAllTimeToAlarmUseCase: SetAllTimeToAlarmUseCase

    @Inject
    lateinit var setTimeToAlarmUseCase: SetTimeToAlarmUseCase

    init {
        appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            PowerViewModel::class.java -> PowerViewModel(
                clearAlarmTimersUseCase,
                getTimeToAlarmUseCase,
                setAllTimeToAlarmUseCase,
                setTimeToAlarmUseCase
            )
            SplashViewModel::class.java -> SplashViewModel()
            else -> throw Exception("view model factory with name=$modelClass don't supported")
        } as T
    }
}