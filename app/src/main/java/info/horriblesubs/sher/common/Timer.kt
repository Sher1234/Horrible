package info.horriblesubs.sher.common

import android.os.Handler
import androidx.lifecycle.MutableLiveData

class Timer(private val timerData: TimerData): Runnable {
    private var timer: Handler? = null
    override fun run() {
        timer?.postDelayed(this, 60000)
        timerData.timerEvent()
    }
    fun start() {
        stop()
        timer = Handler()
        run()
    }
    fun stop() {
        timer?.removeCallbacks(this)
        timer = null
    }
    interface TimerData {
        val time: MutableLiveData<String?>
        fun timerEvent()
    }
}