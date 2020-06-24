package info.horriblesubs.sher.data.horrible

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.RepoResut
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.cache.*
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
            liveResourceDetail.setLoading()
            val detail = HorribleApi.api.getShow(link, reset.resetShowHorribleAPI)
            liveResourceDetail.setSuccess(detail)
            ShowResult(
                detail?.detailTimeStamp?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                detail
            ).onSaveDetail(link)
        } else liveResourceDetail.setSuccess(cDetail?.value)
    }

    @WorkerThread
    private suspend fun callToWebServerEpisodes(
        link: String, sid: String, reset: Boolean,
        mode: ApiEpisodes, cEpisodes: ReleaseResult?
    ) {
        if(isNetworkAvailable) {
            liveResourceEpisodesTime.set()
            liveResourceEpisodes.setLoading()
            val episodes = HorribleApi.api.getEpisodes(sid, mode.mode, reset.resetShowHorribleAPI)
            liveResourceEpisodes.setSuccess(episodes?.items)
            liveResourceEpisodesTime.set(episodes?.dateTime)
            ReleaseResult(
                episodes?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                episodes?.items
            ).onSaveReleases(link, FileType.EPISODES)
        } else {
            liveResourceEpisodes.setSuccess(cEpisodes?.value)
            liveResourceEpisodesTime.set(cEpisodes?.time.zonedDateTimeISO)
        }
    }

    @WorkerThread
    private suspend fun callToWebServerBatches(
        link: String, sid: String, reset: Boolean,
        cBatches: ReleaseResult?
    ) {
        if(isNetworkAvailable) {
            liveResourceBatchesTime.set()
            liveResourceBatches.setLoading()
            val batches = HorribleApi.api.getBatches(sid, reset.resetShowHorribleAPI)
            liveResourceBatches.setSuccess(batches?.items)
            liveResourceBatchesTime.set(batches?.dateTime)
            ReleaseResult(
                batches?.dateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                batches?.items
            ).onSaveReleases(link, FileType.BATCHES)
        } else {
            liveResourceBatches.setSuccess(cBatches?.value)
            liveResourceBatchesTime.set(cBatches?.time.zonedDateTimeISO)
        }
    }

    @WorkerThread
    private suspend fun callToWebServerFull(
        link: String, reset: Boolean = false, detail: ShowResult?,
        episodes: ReleaseResult?, batches: ReleaseResult?
    ) {
        if(isNetworkAvailable) {
            liveResourceBatchesTime.set()
            liveResourceEpisodesTime.set()
            liveResourceDetail.setLoading()
            liveResourceBatches.setLoading()
            liveResourceEpisodes.setLoading()
            val result = HorribleApi.api.getFullShow(link, reset = reset.resetShowHorribleAPI)
            liveResourceEpisodesTime.set(result?.episodes?.dateTime)
            liveResourceEpisodes.setSuccess(result?.episodes?.items)
            liveResourceBatches.setSuccess(result?.batches?.items)
            liveResourceBatchesTime.set(result?.batches?.dateTime)
            liveResourceDetail.setSuccess(result)
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
            liveResourceEpisodesTime.set(episodes?.time.zonedDateTimeISO)
            liveResourceBatchesTime.set(batches?.time?.zonedDateTimeISO)
            liveResourceEpisodes.setSuccess(episodes?.value)
            liveResourceBatches.setSuccess(batches?.value)
            liveResourceDetail.setSuccess(detail?.value)
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
        liveResourceDetail.setFailure(t.message ?: "Error encountered while show!!!")
        t.printStackTrace()
    }

    private val episodesHandler = CoroutineExceptionHandler { _, t ->
        liveResourceEpisodes.setFailure(t.message ?: "Error encountered while fetching show releases!!!")
        liveResourceEpisodesTime.set()
        t.printStackTrace()
    }

    private val batchesHandler = CoroutineExceptionHandler { _, t ->
        liveResourceBatches.setFailure(t.message ?: "Error encountered while fetching show batches!!!")
        liveResourceBatchesTime.set()
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
                liveResourceEpisodesTime.set(episodes?.time?.zonedDateTimeISO)
                liveResourceEpisodes.setSuccess(episodes?.value)
            }
            if (batches.isCacheInvalid { plusDays(14) }) {
                detail?.value?.sid?.let {
                    onLoadBatches(link = link, sid = it, batches = batches)
                }
            } else {
                liveResourceBatches.setSuccess(batches?.value)
                liveResourceBatchesTime.set(batches?.time?.zonedDateTimeISO)
            }
            if (detail.isCacheInvalid { plusDays(14) }) {
                onLoadDetail(link = link, detail = detail)
            } else liveResourceDetail.setSuccess(detail?.value)
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
        RepoResut<ItemShow>(time ?: "", t)

    class ReleaseResult(time: String?, t: List<ItemRelease>?):
        RepoResut<List<ItemRelease>>(time ?: "", t)
}