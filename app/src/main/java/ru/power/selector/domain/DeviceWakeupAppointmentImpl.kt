package ru.power.selector.domain

import android.util.Log
import java.io.DataOutputStream

class DeviceWakeupAppointmentImpl : IDeviceWakeupAppointment {

    override fun setTime(time: Long) {
        Log.d(this::class.java.name, "set time ${(time - System.currentTimeMillis()) / 1000}")
        if (time < 0) {
            return
        }

        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            val command = "echo +${(time - System.currentTimeMillis()) / 1000} > /sys/class/rtc/rtc0/wakealarm"
            process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes(
                """
    $command
    
    """.trimIndent()
            )
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()
            Log.d(this::class.java.name, "exit value ${process?.exitValue()}; wait for ${process.waitFor()}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                process!!.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun cancelTime() {
        var process: Process? = null

        var os: DataOutputStream? = null
        try {
            val command = "echo 0 > /sys/class/rtc/rtc0/wakealarm"
            process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes(
                """
    $command
    
    """.trimIndent()
            )
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                process!!.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}