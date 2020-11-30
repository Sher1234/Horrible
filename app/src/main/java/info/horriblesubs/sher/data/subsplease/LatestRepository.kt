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
import info.horriblesubs.sher.data.database.toNotifications
import info.horriblesubs.sher.data.subsplease.SubsPleaseCache.FileType
import info.horriblesubs.sher.data.subsplease.api.model.ItemLatest
import info.horriblesubs.sher.data.subsplease.api.timezone
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object LatestRepository {

    private val cacheRequest = SubsPleaseCache<LinkedHashMap<String, ItemLatest>>(FileType(FileType.LATEST))
    val liveResource = MutableLiveData<RepositoryResult<LinkedHashMap<String, ItemLatest>>>()
    val liveTime = MutableLiveData<ZonedDateTime>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(cData: LatestResult?) {
        if(isNetworkAvailable) {
            liveTime.set()
            liveResource.setLoading()
            val data = Api.SubsPlease.getLatest(timezone)
            val time = if (data != null) {
                liveResource.setSuccess(data)
                ZonedDateTime.now()
            } else {
                liveResource.setFailure("Error fetching data.")
                liveResource.setSuccess(cData?.value)
                cData?.time?.zonedDateTimeISO
            }
            liveTime.set(time)
            cachedData = LatestResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                data
            )
            data?.let {
                NotificationRepository.apply {
                    deleteAll()
                    insertAll(it.toNotifications())
                }
            }
        } else {
            liveResource.setSuccess(cData?.value)
            liveTime.set(cData?.time?.zonedDateTimeISO)
        }
    }

    fun refreshFromServer(data: LatestResult? = cachedData) {
        job = CoroutineScope(Dispatchers.IO + handler).launch {
            callToWebServer(data)
        }
    }

    val stopServerCall: Unit get() {
        job?.cancel()
        job = null
    }

    private val handler = CoroutineExceptionHandler { _, t ->
        liveResource.setFailure(t.message ?: "Error encountered while fetching latest releases!!!")
        t.printStackTrace()
        liveTime.set()
    }

    init {
        cachedData.apply {
            if (isCacheInvalid { plusMinutes(30) }) {
                refreshFromServer(data = this)
            } else {
                liveResource.setSuccess(this?.value)
                liveTime.set(this?.time?.zonedDateTimeISO)
            }
        }
    }

    @Suppress("deprecation")
    private val isNetworkAvailable: Boolean get() {
        return (App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
            ?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    private var cachedData: LatestResult?
        set(value) = value?.let { cacheRequest.onCacheData(it) } ?: Unit
        get() = cacheRequest.onGetData(LatestResult::class.java)

    class LatestResult(time: String?, value: LinkedHashMap<String, ItemLatest>?):
        RepoResut<LinkedHashMap<String, ItemLatest>>(time ?: "", value)
}