package ru.power.selector.domain

import java.io.DataOutputStream

class DeviceWakeupAppointmentImpl : IDeviceWakeupAppointment {

    override fun setTime(time: Long) {
        if (time < 0) {
            return
        }
        cancelTime()

        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            val command = "echo +$time > /sys/class/rtc/rtc0/wakealarm"
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