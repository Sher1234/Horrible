package info.horriblesubs.sher.data.horrible

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.RepoResut
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.cache.isCacheInvalid
import info.horriblesubs.sher.data.cache.setFailure
import info.horriblesubs.sher.data.cache.setLoading
import info.horriblesubs.sher.data.cache.setSuccess
import info.horriblesubs.sher.data.horrible.HorribleCache.FileType
import info.horriblesubs.sher.data.horrible.api.HorribleApi
import info.horriblesubs.sher.data.horrible.api.model.ItemList
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TrendingRepository {

    private val cacheRequest = HorribleCache<List<ItemList>>(FileType(FileType.TRENDING))
    val liveResource = MutableLiveData<RepositoryResult<List<ItemList>>>()

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServer(cResult: TrendingResult?) {
        if(isNetworkAvailable) {
            liveResource.setLoading()
            val data = HorribleApi.api.getTrending()
            liveResource.setSuccess(data)
            cachedData = TrendingResult(data)
        } else liveResource.setSuccess(cResult?.value)
    }

    fun refreshFromServer(cResult: TrendingResult? = cachedData) {
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
            if (isCacheInvalid { plusHours(4) }) refreshFromServer(this)
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
        get() = cacheRequest.onGetData(TrendingResult::class.java)

    class TrendingResult(value: List<ItemList>?): RepoResut<List<ItemList>>(
        ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
        value
    )
}