package info.horriblesubs.sher.data.subsplease

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.Api
import info.horriblesubs.sher.data.RepoResut
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.cache.*
import info.horriblesubs.sher.data.subsplease.SubsPleaseCache.FileType
import info.horriblesubs.sher.data.subsplease.api.getShows
import info.horriblesubs.sher.data.subsplease.api.model.ItemList
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ShowsRepository {

    private val cacheRequest = SubsPleaseCache<List<ItemList>>(FileType(FileType.SHOWS))
    val liveShows = MutableLiveData<RepositoryResult<List<ItemList>>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private fun callToWebServer(cResult: ShowsResult?) {
        if(isNetworkAvailable) {
            liveTime.set()
            liveShows.setLoading()
            val data = Api.SubsPlease.getShows()
            val time = if (data.isNullOrEmpty()) {
                liveShows.setFailure("Invalid Response")
                liveShows.setSuccess(cResult?.value)
                cResult?.time?.zonedDateTimeISO
            } else {
                liveShows.setSuccess(data)
                ZonedDateTime.now()
            }
            liveTime.set(time)
            cachedData = ShowsResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                data
            )
        } else {
            liveShows.setSuccess(cResult?.value ?: emptyList())
            liveTime.set(cResult?.time.zonedDateTimeISO)
        }
    }

    fun refreshFromServer(cResult: ShowsResult? = cachedData) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(cResult = cResult)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveShows.setFailure(t.message ?: "Error encountered while fetching shows!!!")
        t.printStackTrace()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusDays(3) }) {
                refreshFromServer(cResult = this)
            } else {
                liveShows.setSuccess(this?.value ?: emptyList())
                liveTime.set(this?.time?.zonedDateTimeISO)
            }
        }
    }

    @Suppress("deprecation")
    private val isNetworkAvailable: Boolean get() {
        return (App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
            ?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    private var cachedData
        set(value) = value?.let { cacheRequest.onCacheData(it) } ?: Unit
        get() = cacheRequest.onGetData(ShowsResult::class.java)

    class ShowsResult(time: String?, t: List<ItemList>?):
        RepoResut<List<ItemList>>(time ?: "", t)
}