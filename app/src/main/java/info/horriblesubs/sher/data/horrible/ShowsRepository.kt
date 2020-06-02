package info.horriblesubs.sher.data.horrible

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.RepositoryData
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.cache.isCacheInvalid
import info.horriblesubs.sher.data.cache.zonedDateTimeISO
import info.horriblesubs.sher.data.horrible.HorribleCache.FileType
import info.horriblesubs.sher.data.horrible.api.HorribleApi
import info.horriblesubs.sher.data.horrible.api.current
import info.horriblesubs.sher.data.horrible.api.dateTime
import info.horriblesubs.sher.data.horrible.api.model.ItemList
import info.horriblesubs.sher.data.horrible.api.resetHorribleAPI
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ShowsRepository {

    private val cacheRequest = HorribleCache<List<ItemList>>(FileType(FileType.SHOWS))
    val liveAllShows = MutableLiveData<RepositoryResult<List<ItemList>>>()
    val liveCurrent = MutableLiveData<RepositoryResult<List<ItemList>>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(
        reset: Boolean = false,
        cResult: ShowsResult?
    ) {
        if(isNetworkAvailable) {
            liveCurrent.postValue(RepositoryResult.getLoading())
            liveAllShows.postValue(RepositoryResult.getLoading())
            val data = HorribleApi.api.getShows(reset = reset.resetHorribleAPI)
            liveCurrent.postValue(RepositoryResult.getSuccess(data?.items.current()))
            liveAllShows.postValue(RepositoryResult.getSuccess(data?.items))
            liveTime.postValue(data?.dateTime)
            cachedData = ShowsResult(
                data?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                data?.items ?: emptyList()
            )
        } else {
            liveAllShows.postValue(RepositoryResult.getSuccess(cResult?.value ?: emptyList()))
            liveCurrent.postValue(RepositoryResult.getSuccess(cResult?.value.current()))
            liveTime.postValue(cResult?.time.zonedDateTimeISO)
        }
    }

    fun refreshFromServer(
        reset: Boolean = false,
        cResult: ShowsResult? = cachedData
    ) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(reset, cResult = cResult)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveCurrent.postValue(RepositoryResult.getFailure(t.message ?: "Error encountered while fetching shows!!!"))
        t.printStackTrace()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusDays(10) }) {
                refreshFromServer(cResult = this)
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    liveAllShows.postValue(RepositoryResult.getSuccess(this@apply?.value ?: emptyList()))
                    liveCurrent.postValue(RepositoryResult.getSuccess(this@apply?.value.current()))
                    liveTime.postValue(this@apply?.time.zonedDateTimeISO)
                }
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
        RepositoryData<List<ItemList>>(time ?: "", t)
}