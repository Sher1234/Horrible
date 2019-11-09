package info.horriblesubs.sher.ui.main.explore.random

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.api.horrible.Horrible.Companion.service
import info.horriblesubs.sher.api.horrible.model.ItemShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomVM : ViewModel() {
    internal val result: MutableLiveData<ItemShow?> = MutableLiveData()
    private var job: Job? = null

    private suspend fun fetchOnIO(job: Job): ItemShow? {
        this.job = job
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO){service.random()}
    }

    internal fun refresh() {
        viewModelScope.launch(Dispatchers.Main) {result.value = fetchOnIO(job?:Job())}
    }

    internal fun stop() {
        job?.cancel()
        job = null
    }
}
