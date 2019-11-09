package info.horriblesubs.sher.ui.main.bookmarked

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.LoadingListener
import info.horriblesubs.sher.db.DataAccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkedVM: ViewModel() {
    internal val result = MutableLiveData<List<ItemShow>?>()
    private var listener: LoadingListener? = null
    private var job: Job? = null

    internal fun initialize(listener: LoadingListener) {
        this.listener = listener
    }

    internal fun refresh(reset: Boolean = false) {
        val job = job ?: Job()
        this.job = job
        if (result.value.isNullOrEmpty() || reset) {
            viewModelScope.launch(Dispatchers.Main + job) {
                listener?.start()
                val value = withContext(Dispatchers.IO) {DataAccess.bookmarks()}
                result.value = value
                listener?.stop()
            }
        }
    }

    internal fun stop() {
        listener?.stop()
        job?.cancel()
        job = null
    }
}