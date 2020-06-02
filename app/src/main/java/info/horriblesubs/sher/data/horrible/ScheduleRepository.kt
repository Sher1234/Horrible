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
import info.horriblesubs.sher.data.horrible.api.dateTime
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule
import info.horriblesubs.sher.data.horrible.api.resetHorribleAPI
import info.horriblesubs.sher.data.horrible.api.result.ScheduleSplit
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ScheduleRepository {

    private val cacheRequest = HorribleCache<List<List<ItemSchedule>>>(FileType(FileType.SCHEDULE))
    val liveResource = MutableLiveData<RepositoryResult<List<List<ItemSchedule>>>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(reset: Boolean = false, cData: ScheduleResult?) {
        if(isNetworkAvailable) {
            liveTime.postValue(null)
            liveResource.postValue(RepositoryResult.getLoading())
            val data = HorribleApi.api.getSchedule(reset = reset.resetHorribleAPI)
            val result = ScheduleSplit.getSplit(data?.items)
            liveResource.postValue(RepositoryResult.getSuccess(result))
            liveTime.postValue(data?.dateTime)
            cachedData = ScheduleResult(
                data?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                result
            )
        } else {
            liveResource.postValue(RepositoryResult.getSuccess(cData?.value))
            liveTime.postValue(cData?.time.zonedDateTimeISO)
        }
    }

    fun refreshFromServer(reset: Boolean = false, data: ScheduleResult? = cachedData) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(reset, data)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveResource.postValue(RepositoryResult.getFailure(t.message ?: "Error encountered while fetching weekly schedule!!!"))
        liveTime.postValue(null)
        t.printStackTrace()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusDays(2) }) {
                refreshFromServer(data = this)
            } else {
                liveResource.postValue(RepositoryResult.getSuccess(this?.value))
                liveTime.postValue(this?.time.zonedDateTimeISO)
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
        get() = cacheRequest.onGetData(ScheduleResult::class.java)

    class ScheduleResult(time: String?, t: List<List<ItemSchedule>>?):
        RepositoryData<List<List<ItemSchedule>>>(time ?: "", t)
}