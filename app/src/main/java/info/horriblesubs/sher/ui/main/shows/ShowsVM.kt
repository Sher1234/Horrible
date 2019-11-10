package info.horriblesubs.sher.ui.main.shows

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.api.horrible.Horrible
import info.horriblesubs.sher.api.horrible.result.ResultShows
import info.horriblesubs.sher.api.horrible.result.isNull
import info.horriblesubs.sher.common.LoadingListener
import kotlinx.coroutines.*

class ShowsVM : ViewModel() {
    private val handler = CoroutineExceptionHandler {c, t ->
        Log.w("Handler", "Job#isActive: " + c.isActive, t)
        result.value = null
        listener?.stop()
    }
    internal val result = MutableLiveData<ResultShows?>()
    internal val allShowing = MutableLiveData<Boolean>()
    private var listener: LoadingListener? = null
    private var job: Job? = null

    init {
        allShowing.value = false
    }

    private suspend fun fetchOnIO(job: Job, reset: Boolean): ResultShows? {
        this.job = Job()
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO + job + handler) {
            Horrible.service.shows(if(reset) "reset" else "0")
        }
    }

    internal fun initialize(listener: LoadingListener?) {
        this.listener = listener
    }

    internal fun refresh(reset: Boolean = false) {
        if (result.value.isNull() || reset)
            viewModelScope.launch(Dispatchers.Main + handler) {
                listener?.start()
                result.value = fetchOnIO(job?:Job(), reset)
                listener?.stop()
            }
    }

    internal fun toggle() {
        allShowing.value = !(allShowing.value?:true)
    }

    internal fun stop() {
        listener?.stop()
        job?.cancel()
        job = null
    }
}