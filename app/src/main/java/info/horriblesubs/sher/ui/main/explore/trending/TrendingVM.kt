package info.horriblesubs.sher.ui.main.explore.trending

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.api.horrible.Horrible
import info.horriblesubs.sher.api.horrible.model.ItemShow
import kotlinx.coroutines.*

class TrendingVM : ViewModel() {

    private val handler = CoroutineExceptionHandler {c, t ->
        Log.w("Handler", "Job#isActive: " + c.isActive, t)
        result.value = null
    }
    internal val result: MutableLiveData<List<ItemShow>?> = MutableLiveData()
    private var job: Job? = null

    private suspend fun fetchOnIO(job: Job): List<ItemShow>? {
        this.job = Job()
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO + job + handler) {
            Horrible.service.trending()
        }
    }

    internal fun refresh(reset: Boolean = false) {
        if (result.value == null || reset)
            viewModelScope.launch(Dispatchers.Main + handler) {result.value = fetchOnIO(job?:Job())}
    }

    internal fun stop() {
        job?.cancel()
        job = null
    }
}