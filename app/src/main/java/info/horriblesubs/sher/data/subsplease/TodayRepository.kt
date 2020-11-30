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
import info.horriblesubs.sher.data.subsplease.api.model.ItemTodaySchedule
import info.horriblesubs.sher.data.subsplease.api.timezone
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TodayRepository {

    private val cacheRequest = SubsPleaseCache<ItemTodaySchedule>(FileType(FileType.TODAY))
    val liveResource = MutableLiveData<RepositoryResult<ItemTodaySchedule>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(cResult: TodayResult?) {
        if(isNetworkAvailable) {
            liveTime.set()
            liveResource.setLoading()
            val data = Api.SubsPlease.getTodaySchedule(timezone)
            val time = if (data != null) {
                liveResource.setSuccess(data)
                ZonedDateTime.now()
            } else {
                liveResource.setFailure("Invalid Response")
                liveResource.setSuccess(data)
                cResult?.time?.zonedDateTimeISO
            }
            liveTime.set(time)
            cachedData = TodayResult(time, data)
        } else {
            liveTime.set(cResult?.time?.zonedDateTimeISO)
            liveResource.setSuccess(cResult?.value)
        }
    }

    fun refreshFromServer(cResult: TodayResult? = cachedData) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(cResult = cResult)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveResource.setFailure(t.message ?: "Error encountered while fetching trending!!!")
        t.printStackTrace()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusMinutes(15) }) refreshFromServer(this)
            else liveResource.setSuccess(this?.value)
        }
    }

    @Suppress("deprecation")
    private val isNetworkAvailable: Boolean get() {
        return (App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
            ?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    private var cachedData
        set(value) = value?.let { cacheRequest.onCacheData(it) } ?: Unit
        get() = cacheRequest.onGetData(TodayResult::class.java)

    class TodayResult(time: ZonedDateTime?, value: ItemTodaySchedule?):
        RepoResut<ItemTodaySchedule>(time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) ?: "", value)
}