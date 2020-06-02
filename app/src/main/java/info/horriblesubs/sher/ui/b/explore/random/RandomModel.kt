package info.horriblesubs.sher.ui.b.explore.random

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.data.horrible.api.HorribleApi
import info.horriblesubs.sher.data.horrible.api.model.ItemShow
import kotlinx.coroutines.*

class RandomModel : ViewModel() {
    private val handler = CoroutineExceptionHandler { c, t ->
        Log.w("Handler", "Job#isActive: " + c.isActive, t)
        result.value = null
    }
    internal val result: MutableLiveData<ItemShow?> = MutableLiveData()
    private var job: Job? = null

    private suspend fun fetchOnIO(job: Job): ItemShow? {
        this.job = job
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            HorribleApi.api.getRandom()
        }
    }

    internal fun refresh() {
        viewModelScope.launch(Dispatchers.Main + handler) {result.value = fetchOnIO(job?:Job())}
    }

    internal fun stop() {
        job?.cancel()
        job = null
    }
}
