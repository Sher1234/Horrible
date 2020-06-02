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
import info.horriblesubs.sher.data.database.toNotifications
import info.horriblesubs.sher.data.horrible.HorribleCache.FileType
import info.horriblesubs.sher.data.horrible.api.HorribleApi
import info.horriblesubs.sher.data.horrible.api.dateTime
import info.horriblesubs.sher.data.horrible.api.model.ItemLatest
import info.horriblesubs.sher.data.horrible.api.resetHorribleAPI
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object LatestRepository {

    private val cacheRequest = HorribleCache<List<ItemLatest>>(FileType(FileType.LATEST))
    val liveResource = MutableLiveData<RepositoryResult<List<ItemLatest>>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(reset: Boolean = false, cData: LatestResult?) {
        if(isNetworkAvailable) {
            liveTime.postValue(null)
            liveResource.postValue(RepositoryResult.getLoading())
            val data = HorribleApi.api.getLatest(reset = reset.resetHorribleAPI)
            liveResource.postValue(RepositoryResult.getSuccess(data?.items))
            val time = data?.dateTime
            liveTime.postValue(time)
            cachedData = LatestResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                data?.items
            )
            data?.items?.let {
                NotificationRepository.apply {
                    deleteAll()
                    insertAll(it.toNotifications())
                }
            }
        } else {
            liveResource.postValue(RepositoryResult.getSuccess(cData?.value))
            liveTime.postValue(cData?.time?.zonedDateTimeISO)
        }
    }

    fun refreshFromServer(reset: Boolean = false, data: LatestResult? = cachedData) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(reset, data)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveResource.postValue(RepositoryResult.getFailure(t.message ?: "Error encountered while fetching latest releases!!!"))
        liveTime.postValue(null)
        t.printStackTrace()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusMinutes(30) }) {
                refreshFromServer(data = this)
            } else {
                liveResource.postValue(RepositoryResult.getSuccess(this?.value))
                liveTime.postValue(this?.time?.zonedDateTimeISO)
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
        get() = cacheRequest.onGetData(LatestResult::class.java)

    class LatestResult(time: String?, value: List<ItemLatest>?):
        RepositoryData<List<ItemLatest>>(time ?: "", value)
}