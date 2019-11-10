package info.horriblesubs.sher.ui.show

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.api.horrible.Horrible.Companion.service
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.common.LoadingListener
import info.horriblesubs.sher.common.Timer
import kotlinx.coroutines.*

class ShowVM: ViewModel(), Timer.TimerData {
    private val handler = CoroutineExceptionHandler {c, t ->
        Log.w("Handler", "Job#isActive: " + c.isActive, t)
        result.value = null
        listener?.stop()
    }
    internal val release: MutableLiveData<Release?> = MutableLiveData()
    internal val result: MutableLiveData<ItemShow?> = MutableLiveData()
    private val link: MutableLiveData<String?> = MutableLiveData()
    override val time = MutableLiveData<String?>()
    private var listener: LoadingListener? = null
    private val timer = Timer(this)
    val link2: String? get() = link.value
    private var job: Job? = null

    private suspend fun fetchOnIO(job: Job, link: String, reset: Boolean): ItemShow? {
        this.job = Job()
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO + job + handler) {
            if (reset) service.showReset(link) else service.show(link)
        }
    }

    internal fun initialize(listener: LoadingListener?, link: String?) {
        this.listener = listener
        this.link.value = link
    }

    internal fun refresh(reset: Boolean = false) {
        if (result.value == null || reset)
            viewModelScope.launch(Dispatchers.Main + handler) {
                listener?.start()
                result.value = link.value?.let{
                    if (it.isNotEmpty()) fetchOnIO(job?:Job(), it, reset)
                    else null
                }
                listener?.stop()
                timer.start()
            }
    }

    override fun timerEvent() {
        if (result.value == null) time.postValue("Never")
        else time.postValue(result.value?.timePassed())
    }

    internal fun stop() {
        listener?.stop()
        job?.cancel()
        timer.stop()
        job = null
    }
}