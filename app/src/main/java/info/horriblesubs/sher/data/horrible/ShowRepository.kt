package info.horriblesubs.sher.data.horrible

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.RepositoryData
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.cache.isCacheInvalid
import info.horriblesubs.sher.data.cache.zonedDateTimeISO
import info.horriblesubs.sher.data.horrible.HorribleCache.FileType
import info.horriblesubs.sher.data.horrible.api.HorribleApi
import info.horriblesubs.sher.data.horrible.api.HorribleApi.ApiEpisodes
import info.horriblesubs.sher.data.horrible.api.dateTime
import info.horriblesubs.sher.data.horrible.api.detailTimeStamp
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease
import info.horriblesubs.sher.data.horrible.api.model.ItemShow
import info.horriblesubs.sher.data.horrible.api.resetShowHorribleAPI
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ShowRepository {

    private val rEpisodes = HorribleCache<List<ItemRelease>>(FileType(FileType.SHOW))
    private val rBatches = HorribleCache<List<ItemRelease>>(FileType(FileType.SHOW))
    private val rDetail = HorribleCache<ItemShow>(FileType(FileType.SHOW))

    val liveResourceEpisodes = MutableLiveData<RepositoryResult<List<ItemRelease>>>()
    val liveResourceBatches = MutableLiveData<RepositoryResult<List<ItemRelease>>>()
    val liveResourceDetail = MutableLiveData<RepositoryResult<ItemShow>>()
    val liveResourceEpisodesTime = MutableLiveData<ZonedDateTime>()
    val liveResourceBatchesTime = MutableLiveData<ZonedDateTime>()
    val link = MutableLiveData<String>()

    private var episodesJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private var batchesJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private var detailJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    @WorkerThread
    private suspend fun callToWebServerDetail(link: String, reset: Boolean, cDetail: ShowResult?) {
        if(isNetworkAvailable) {
            liveResourceDetail.postValue(RepositoryResult.getLoading())
            val detail = HorribleApi.api.getShow(link, reset.resetShowHorribleAPI)
            Log.e("ServerDetail:", detail?.toString() ?: "NULL")
            liveResourceDetail.postValue(RepositoryResult.getSuccess(detail))
            ShowResult(
                detail?.detailTimeStamp?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                detail
            ).onSaveDetail(link)
        } else {
            liveResourceDetail.postValue(RepositoryResult.getSuccess(cDetail?.value))
        }
    }

    @WorkerThread
    private suspend fun callToWebServerEpisodes(
        link: String, sid: String, reset: Boolean,
        mode: ApiEpisodes, cEpisodes: ReleaseResult?
    ) {
        if(isNetworkAvailable) {
            liveResourceEpisodesTime.postValue(null)
            liveResourceEpisodes.postValue(RepositoryResult.getLoading())
            val episodes = HorribleApi.api.getEpisodes(sid, mode.mode, reset.resetShowHorribleAPI)
            Log.e("ServerEpisodes:", episodes?.toString() ?: "NULL")
            liveResourceEpisodes.postValue(RepositoryResult.getSuccess(episodes?.items))
            liveResourceEpisodesTime.postValue(episodes?.dateTime)
            ReleaseResult(
                episodes?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                episodes?.items
            ).onSaveReleases(link, FileType.EPISODES)
        } else {
            liveResourceEpisodes.postValue(RepositoryResult.getSuccess(cEpisodes?.value))
            liveResourceEpisodesTime.postValue(cEpisodes?.time.zonedDateTimeISO)
        }
    }

    @WorkerThread
    private suspend fun callToWebServerBatches(
        link: String, sid: String, reset: Boolean,
        cBatches: ReleaseResult?
    ) {
        if(isNetworkAvailable) {
            liveResourceBatches.postValue(RepositoryResult.getLoading())
            liveResourceBatchesTime.postValue(null)
            val batches = HorribleApi.api.getBatches(sid, reset.resetShowHorribleAPI)
            Log.e("ServerBatches:", batches?.toString() ?: "NULL")
            liveResourceBatches.postValue(RepositoryResult.getSuccess(batches?.items))
            liveResourceBatchesTime.postValue(batches?.dateTime)
            ReleaseResult(
                batches?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                batches?.items
            ).onSaveReleases(link, FileType.BATCHES)
        } else {
            liveResourceBatches.postValue(RepositoryResult.getSuccess(cBatches?.value))
            liveResourceBatchesTime.postValue(cBatches?.time.zonedDateTimeISO)
        }
    }

    @WorkerThread
    private suspend fun callToWebServerFull(
        link: String, reset: Boolean = false, detail: ShowResult?,
        episodes: ReleaseResult?, batches: ReleaseResult?
    ) {
        if(isNetworkAvailable) {
            liveResourceEpisodes.postValue(RepositoryResult.getLoading())
            liveResourceBatches.postValue(RepositoryResult.getLoading())
            liveResourceDetail.postValue(RepositoryResult.getLoading())
            liveResourceEpisodesTime.postValue(null)
            liveResourceBatchesTime.postValue(null)
            val result = HorribleApi.api.getFullShow(link, reset = reset.resetShowHorribleAPI)
            liveResourceEpisodes.postValue(RepositoryResult.getSuccess(result?.episodes?.items))
            liveResourceBatches.postValue(RepositoryResult.getSuccess(result?.batches?.items))
            liveResourceDetail.postValue(RepositoryResult.getSuccess(result))
            liveResourceEpisodesTime.postValue(result?.episodes?.dateTime)
            liveResourceBatchesTime.postValue(result?.batches?.dateTime)
            ShowResult(
                result?.detailTimeStamp?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                result as? ItemShow
            ).onSaveDetail(link)
            ReleaseResult(
                result?.episodes?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                result?.episodes?.items
            ).onSaveReleases(link, FileType.EPISODES)
            ReleaseResult(
                result?.batches?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                result?.batches?.items
            ).onSaveReleases(link, FileType.BATCHES)
        } else {
            liveResourceEpisodes.postValue(RepositoryResult.getSuccess(episodes?.value))
            liveResourceBatches.postValue(RepositoryResult.getSuccess(batches?.value))
            liveResourceDetail.postValue(RepositoryResult.getSuccess(detail?.value))
            liveResourceEpisodesTime.postValue(episodes?.time.zonedDateTimeISO)
            liveResourceBatchesTime.postValue(batches?.time?.zonedDateTimeISO)
        }
    }

    fun refreshFromServer(
        link: String, reset: Boolean = false,
        detail: ShowResult? = getCacheDetail(link),
        episodes: ReleaseResult? = getReleases(link, FileType.EPISODES),
        batches: ReleaseResult? = getReleases(link, FileType.BATCHES)
    ) {
        detailJob = CoroutineScope(Dispatchers.IO + detailHandler).launch {
            callToWebServerFull(link, reset, detail, episodes, batches)
        }
    }

    private fun onLoadDetail(
        link: String, reset: Boolean = false,
        detail: ShowResult? = getCacheDetail(link)
    ) {
        detailJob = CoroutineScope(Dispatchers.IO + detailHandler).launch {
            callToWebServerDetail(link, reset, detail)
        }
    }

    fun onLoadEpisodes(
        link: String, sid: String,
        reset: Boolean = false, mode: ApiEpisodes = ApiEpisodes.NEW,
        episodes: ReleaseResult? = getReleases(link, FileType.EPISODES)
    ) {
        episodesJob = CoroutineScope(Dispatchers.IO + episodesHandler).launch {
            callToWebServerEpisodes(
                link = link, sid = sid, mode = mode,
                reset = reset, cEpisodes = episodes
            )
        }
    }

    fun onLoadBatches(
        link: String, sid: String, reset: Boolean = false,
        batches: ReleaseResult? = getReleases(link, FileType.BATCHES)
    ) {
        batchesJob = CoroutineScope(Dispatchers.IO + batchesHandler).launch {
            callToWebServerBatches(link, sid, reset, cBatches = batches)
        }
    }

    val stopServerCall: Unit get() {
        episodesJob?.cancel()
        batchesJob?.cancel()
        detailJob?.cancel()
        episodesJob = null
        batchesJob = null
        detailJob = null
    }

    private val detailHandler = CoroutineExceptionHandler { _, t ->
        liveResourceDetail.postValue(RepositoryResult.getFailure(t.message ?: "Error encountered while show!!!"))
        t.printStackTrace()
    }

    private val episodesHandler = CoroutineExceptionHandler { _, t ->
        liveResourceEpisodes.postValue(RepositoryResult.getFailure(t.message ?: "Error encountered while fetching show releases!!!"))
        liveResourceEpisodesTime.postValue(null)
        t.printStackTrace()
    }

    private val batchesHandler = CoroutineExceptionHandler { _, t ->
        liveResourceBatches.postValue(RepositoryResult.getFailure(t.message ?: "Error encountered while fetching show batches!!!"))
        liveResourceBatchesTime.postValue(null)
        t.printStackTrace()
    }

    fun initialize(link: String) {
        val episodes = getReleases(link, FileType.EPISODES)
        val batches = getReleases(link, FileType.BATCHES)
        val detail = getCacheDetail(link)
        if (detail.isCacheInvalid { plusDays(14) } && batches.isCacheInvalid { plusDays(14) } && episodes.isCacheInvalid { plusDays(3) }) {
            refreshFromServer(link = link, detail = detail, episodes = episodes, batches = batches)
        } else {
            if (episodes.isCacheInvalid { plusDays(3) }) {
                detail?.value?.sid?.let {
                    onLoadEpisodes(link = link, sid = it, episodes = episodes)
                }
            } else {
                liveResourceEpisodes.postValue(RepositoryResult.getSuccess(episodes?.value))
                liveResourceEpisodesTime.postValue(episodes?.time?.zonedDateTimeISO)
            }
            if (batches.isCacheInvalid { plusDays(14) }) {
                detail?.value?.sid?.let {
                    onLoadBatches(link = link, sid = it, batches = batches)
                }
            } else {
                liveResourceBatches.postValue(RepositoryResult.getSuccess(batches?.value))
                liveResourceBatchesTime.postValue(batches?.time?.zonedDateTimeISO)
            }
            if (detail.isCacheInvalid { plusDays(14) }) {
                onLoadDetail(link = link, detail = detail)
            } else liveResourceDetail.postValue(RepositoryResult.getSuccess(detail?.value))
        }
    }

    @Suppress("deprecation")
    private val isNetworkAvailable: Boolean get() {
        return (App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
            ?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    private fun getCacheDetail(link: String) =
        rDetail.onGetData(link, FileType.DETAIL, ShowResult::class.java)

    private fun ShowResult.onSaveDetail(link: String) =
        rDetail.onCacheData(link, FileType.DETAIL, this)

    private fun getReleases(link: String, type: String) = when(type) {
        FileType.EPISODES -> rEpisodes.onGetData(link, type, ReleaseResult::class.java)
        FileType.BATCHES -> rBatches.onGetData(link, type, ReleaseResult::class.java)
        else -> null
    }

    private fun ReleaseResult.onSaveReleases(link: String, type: String) = when(type) {
        FileType.EPISODES -> rEpisodes.onCacheData(link, type, this)
        FileType.BATCHES -> rBatches.onCacheData(link, type, this)
        else -> Unit
    }

    class ShowResult(time: String?, t: ItemShow?):
        RepositoryData<ItemShow>(time ?: "", t)

    class ReleaseResult(time: String?, t: List<ItemRelease>?):
        RepositoryData<List<ItemRelease>>(time ?: "", t)
}