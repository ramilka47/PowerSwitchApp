package ru.power.selector.ui.screen

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.Switch.*
import ru.power.selector.ui.data.WeekId
import ru.power.selector.ui.theme.getWeekName
import ru.power.selector.ui.theme.lightGray
import ru.power.selector.ui.theme.silver
import ru.power.selector.ui.view_model.AlarmWeek
import ru.power.selector.ui.view_model.PowerViewModel

@Composable
fun PowerScreen(powerViewModel: PowerViewModel, navHostController: NavHostController, modifier: Modifier = Modifier.fillMaxSize()) {
    Column(modifier = modifier) {
        Header(
            clear = powerViewModel.clearState.observeAsState(),
            clearFun = powerViewModel::clear,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(lightGray)
        )

        WeekAlarm(
            state = powerViewModel.alarmWeek.observeAsState(),
            checkFunc = powerViewModel::setSwitchOnAll,
            timeFunc = {
                showDialogForAll(navHostController.context) { hour, min ->
                    powerViewModel.setTimeForAll(it, hour, min)
                }
            },
            verticalPadding = 16.dp
        )

        ObserveList(
            context = navHostController.context,
            powerViewModel = powerViewModel,
            verticalPadding = 16.dp
        )
    }
    powerViewModel.onCreate()
}

private fun showDialogForAll(context : Context, onClickTimer : (Int, Int)->Unit){
    val dialog = TimePickerDialog(
        /* context = */ context,
        /* listener = */ { _, hour : Int, minute: Int ->
            onClickTimer(hour, minute)
            //powerViewModel.setTimeForAll(it, hour, minute)
        },
        /* hourOfDay = */ 0,
        /* minute = */ 0,
        /* is24HourView = */ false
    )
    dialog.show()
}

@Composable
private fun WeekAlarm(state : State<AlarmWeek?>,
                      checkFunc : (Boolean)->Unit,
                      timeFunc : (Switch)->Unit,
                      verticalPadding: Dp){
    val value = state.value

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        Text(
            text = "Для всех",
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        Switch(
            checked = value != null,
            modifier = Modifier.padding(8.dp),
            onCheckedChange = {
                checkFunc(it)
            })

        if (value != null){
            Text(
                text = getStringTime(value.wakeUpHour, value.wakeUpMin),
                color = Color.Green,
                fontSize = 15.sp,
                modifier =
                Modifier.padding(8.dp)
                    .clickable {
                        timeFunc(Switch.On)
                    })

            Text(
                text = getStringTime(value.sleepHour, value.sleepMin),
                color = Color.Red,
                fontSize = 15.sp,
                modifier =
                Modifier.padding(8.dp)
                    .clickable {
                        timeFunc(Switch.Off)
                    })
        }
    }
}

@Composable
private fun Header(clear : State<Boolean?>, clearFun : ()->Unit, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(text = "Очистить",
            fontSize = 14.sp,
            color = if (clear.value == true) Color.Black else Color.Gray,
            modifier = Modifier
                .clickable {
                    if (clear.value == true) {
                        clearFun()
                    }
                }
                .padding(6.dp)
                .background(silver))
    }
}

@Composable
private fun ObserveList(context: Context, powerViewModel: PowerViewModel, verticalPadding : Dp) {
    val data = powerViewModel.data.observeAsState()

    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        val onList = data.value?.filter { it.switch == On } ?: listOf()
        val offList = data.value?.filter { it.switch == Off } ?: listOf()
        LazyList(
            list = onList,
            onClickTime = { timer ->
                showDialogForAll(context) { hour, min ->
                    powerViewModel.addTimer(AlarmTimer(timer.id, timer.switch, hour, min))
                }
            },
            onClickSwitch = { timer, state ->
                powerViewModel.onClickTime(timer.id, timer.switch, state)
            },
            name = "Включение",
            modifier = Modifier.fillMaxHeight()
                .padding(verticalPadding)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxHeight().width(1.dp)
                .padding(verticalPadding)
        )

        LazyList(
            list = offList,
            onClickTime = { timer ->
                showDialogForAll(context) { hour, min ->
                    powerViewModel.addTimer(AlarmTimer(timer.id, timer.switch, hour, min))
                }
            },
            onClickSwitch = { timer, state ->
                powerViewModel.onClickTime(timer.id, timer.switch, state)
            },
            name = "Выключение",
            modifier = Modifier.fillMaxHeight()
                .padding(verticalPadding)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyList(list : List<AlarmTimer>, onClickTime: (AlarmTimer) -> Unit, onClickSwitch: (AlarmTimer, Boolean)->Unit, name : String, modifier: Modifier){
    LazyColumn(modifier = modifier) {
        stickyHeader{
            Text(name, color = Color.Black, fontSize = 17.sp)
        }
        items(items = list) { timer ->
            AlarmItem(
                weekId = timer.id,
                switch = timer.switch,
                state = !timer.isCheck(),
                timeWakeUp = getStringTime(timer.hour, timer.min),
                onClickSwitch = { state ->
                    onClickSwitch(timer, state)
                },
                onClickTime = {
                    onClickTime(timer)
                }
            )
        }
    }
}

private fun getStringTime(hour : Int, min : Int) : String {
    return "${getStringInt(hour)}:${getStringInt(min)}"
}

private fun getStringInt(value : Int) : String {
    return if (value < 10){
        "0$value"
    } else if (value == Int.MAX_VALUE)
        "00"
    else value.toString()
}

@Composable
private fun AlarmItem(
    weekId: WeekId,
    switch: Switch,
    state : Boolean,
    timeWakeUp : String = "00:00",
    onClickSwitch : (Boolean)->Unit,
    onClickTime : ()->Unit) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = state,
            modifier = Modifier.padding(8.dp),
            onCheckedChange = {
                onClickSwitch(it)
            })

        Text(
            text = weekId.getWeekName(),
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        if (state) {
            Text(
                text = timeWakeUp,
                color = when (switch) {
                    On -> Color.Green
                    Off -> Color.Red
                },
                fontSize = 15.sp,
                modifier =
                Modifier.padding(8.dp)
                    .clickable {
                        onClickTime()
                    })
        }
    }
}