package ru.power.selector.ui
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.power.selector.Application
import ru.power.selector.route.Destination
import ru.power.selector.route.navigator
import ru.power.selector.ui.theme.PowerSwitchAppTheme
import ru.power.selector.ui.view_model.ViewModelFactory


class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Application.appComponent.inject(this)

        setContent {
            navHostController = navigator(viewModelFactory = ViewModelFactory(Application.appComponent))

            PowerSwitchAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    navHostController.navigate(Destination.Splash)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    private val TAG = this::class.java.simpleName

}