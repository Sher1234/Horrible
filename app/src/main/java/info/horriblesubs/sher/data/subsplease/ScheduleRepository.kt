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
import info.horriblesubs.sher.data.subsplease.api.model.ItemSchedule
import info.horriblesubs.sher.data.subsplease.api.timezone
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ScheduleRepository {

    private val cacheRequest = SubsPleaseCache<ItemSchedule>(FileType(FileType.SCHEDULE))
    val liveResource = MutableLiveData<RepositoryResult<ItemSchedule>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(cData: ScheduleResult?) {
        if(isNetworkAvailable) {
            liveTime.set()
            liveResource.setLoading()
            val data = Api.SubsPlease.getSchedule(timezone)
            val time = if (data != null) {
                liveResource.setSuccess(data)
                ZonedDateTime.now()
            } else {
                liveResource.setFailure("Invalid Response")
                liveResource.setSuccess(cData?.value)
                cData?.time?.zonedDateTimeISO
            }
            liveTime.set(time)
            cachedData = ScheduleResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                data
            )
        } else {
            liveTime.set(cData?.time.zonedDateTimeISO)
            liveResource.setSuccess(cData?.value)
        }
    }

    fun refreshFromServer(data: ScheduleResult? = cachedData) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(data)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveResource.setFailure(t.message ?: "Error encountered while fetching weekly schedule!!!")
        t.printStackTrace()
        liveTime.set()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusDays(1) }) refreshFromServer(this) else {
                liveResource.setSuccess(this?.value)
                liveTime.set(this?.time.zonedDateTimeISO)
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

    class ScheduleResult(time: String?, t: ItemSchedule?):
        RepoResut<ItemSchedule>(time ?: "", t)
}