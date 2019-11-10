package info.horriblesubs.sher.ui.main.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.api.horrible.Horrible.Companion.service
import info.horriblesubs.sher.api.horrible.model.ItemSchedule
import info.horriblesubs.sher.api.horrible.model.Split
import info.horriblesubs.sher.api.horrible.result.Result
import info.horriblesubs.sher.api.horrible.result.isNull
import info.horriblesubs.sher.common.LoadingListener
import kotlinx.coroutines.*

class ScheduleVM : ViewModel() {
    private val handler = CoroutineExceptionHandler {c, t ->
        Log.w("Handler", "Job#isActive: " + c.isActive, t)
        schedule.value = null
        result.value = null
        listener?.stop()
    }
    internal val schedule = MutableLiveData<Map<String, List<ItemSchedule>>?>()
    internal val result = MutableLiveData<Result<ItemSchedule>?>()
    private var listener: LoadingListener? = null
    private var job: Job? = null

    private suspend fun fetchOnIO(job: Job, reset: Boolean): Map<String, List<ItemSchedule>>? {
        this.job = Job()
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO + job + handler) {
            val value = service.schedule(if(reset) "reset" else "0")
            result.postValue(value)
            Split.schedule(value?.items)
        }
    }

    internal fun initialize(listener: LoadingListener?) {
        this.listener = listener
    }

    internal fun refresh(reset: Boolean = false) {
        if (result.value.isNull() || reset)
            viewModelScope.launch(Dispatchers.Main + handler) {
                listener?.start()
                val value = fetchOnIO(job?:Job(), reset)
                schedule.value = value
                listener?.stop()
            }
    }

    internal fun stop() {
        listener?.stop()
        job?.cancel()
        job = null
    }
}
