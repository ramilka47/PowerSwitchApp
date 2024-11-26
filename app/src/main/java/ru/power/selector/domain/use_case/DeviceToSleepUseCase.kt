package ru.power.selector.domain.use_case

import android.content.Context
import android.os.PowerManager
import kotlinx.coroutines.CoroutineScope

class DeviceToSleepUseCase(private val context: Context) : UseCase<Unit, Unit> {

    override suspend fun execute(unit : Unit, scope: CoroutineScope){
        try {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            try {
                val method = pm.javaClass.getMethod(
                    "shutdown",
                    Boolean::class.java,
                    String::class.java,
                    Boolean::class.java
                )
                method.invoke(pm, false, null, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}