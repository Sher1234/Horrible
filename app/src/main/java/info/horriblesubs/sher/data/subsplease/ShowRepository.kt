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
import info.horriblesubs.sher.data.subsplease.api.getShowDetail
import info.horriblesubs.sher.data.subsplease.api.model.ItemReleasePage
import info.horriblesubs.sher.data.subsplease.api.model.ItemShow
import info.horriblesubs.sher.data.subsplease.api.timezone
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ShowRepository {

    private val rReleases = SubsPleaseCache<ItemReleasePage>(FileType(FileType.SHOW))
    private val rDetail = SubsPleaseCache<ItemShow>(FileType(FileType.SHOW))

    val liveResourceReleases = MutableLiveData<RepositoryResult<ItemReleasePage>>()
    val liveResourceDetail = MutableLiveData<RepositoryResult<ItemShow>>()
    val liveResourceReleasesTime = MutableLiveData<ZonedDateTime>()
    val liveResourceDetailTime = MutableLiveData<ZonedDateTime>()

    private var releasesJob: Job? = null
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
    private fun callToWebServerDetail(link: String, cDetail: ShowResult?) {
        if(isNetworkAvailable) {
            liveResourceDetail.setLoading()
            liveResourceDetailTime.set(null)
            val detail = Api.SubsPlease.getShowDetail(link)
            val time = if (detail == null) {
                liveResourceDetail.setFailure("Invalid Data")
                liveResourceDetail.setSuccess(cDetail?.value)
                cDetail?.time?.zonedDateTimeISO
            } else {
                liveResourceDetail.setSuccess(detail)
                ZonedDateTime.now()
            }
            liveResourceDetailTime.set(time)
            ShowResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                detail
            ).onSaveDetail(link)
        } else {
            liveResourceDetail.setSuccess(cDetail?.value)
            liveResourceDetailTime.set(cDetail?.time?.zonedDateTimeISO)
        }
    }

    @WorkerThread
    private suspend fun callToWebServerReleases(link: String, sid: String, cEpisodes: ReleaseResult?) {
        if(isNetworkAvailable) {
            liveResourceReleasesTime.set()
            liveResourceReleases.setLoading()
            val episodes = Api.SubsPlease.getShowReleases(timezone, sid)
            val time = if (episodes == null) {
                liveResourceReleases.setFailure("Invalid Data")
                liveResourceReleases.setSuccess(cEpisodes?.value)
                cEpisodes?.time?.zonedDateTimeISO
            } else {
                liveResourceReleases.setSuccess(episodes)
                ZonedDateTime.now()
            }
            liveResourceReleasesTime.set(time)
            ReleaseResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                episodes
            ).onSaveReleases(link)
        } else {
            liveResourceReleases.setSuccess(cEpisodes?.value)
            liveResourceReleasesTime.set(cEpisodes?.time.zonedDateTimeISO)
        }
    }

    @WorkerThread
    private suspend fun callToWebServerFull(link: String, detail: ShowResult?, episodes: ReleaseResult?) {
        if(isNetworkAvailable) {
            liveResourceDetail.setLoading()
            liveResourceDetailTime.set(null)
            val detailNew = Api.SubsPlease.getShowDetail(link)
            val time = if (detailNew == null) {
                liveResourceDetail.setFailure("Invalid Data")
                liveResourceDetail.setSuccess(detail?.value)
                liveResourceReleases.setSuccess(episodes?.value)
                liveResourceReleasesTime.set(episodes?.time?.zonedDateTimeISO)
                detail?.time?.zonedDateTimeISO
            } else {
                liveResourceDetail.setSuccess(detailNew)
                callToWebServerReleases(link, detailNew.sid, episodes)
                ZonedDateTime.now()
            }
            liveResourceDetailTime.set(time)
            ShowResult(
                time?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                detailNew
            ).onSaveDetail(link)
        } else {
            liveResourceReleasesTime.set(episodes?.time.zonedDateTimeISO)
            liveResourceDetailTime.set(detail?.time?.zonedDateTimeISO)
            liveResourceReleases.setSuccess(episodes?.value)
            liveResourceDetail.setSuccess(detail?.value)
        }
    }

    fun refreshFromServer(
        link: String, detail: ShowResult? = getCacheDetail(link),
        episodes: ReleaseResult? = getReleases(link)
    ) {
        detailJob = CoroutineScope(Dispatchers.IO + detailHandler).launch {
            callToWebServerFull(link, detail, episodes)
        }
    }

    private fun onLoadDetail(
        link: String, detail: ShowResult? = getCacheDetail(link)
    ) {
        detailJob = CoroutineScope(Dispatchers.IO + detailHandler).launch {
            callToWebServerDetail(link, detail)
        }
    }

    fun onLoadEpisodes(
        link: String, sid: String,
        episodes: ReleaseResult? = getReleases(link)
    ) {
        releasesJob = CoroutineScope(Dispatchers.IO + episodesHandler).launch {
            callToWebServerReleases(link = link, sid = sid, cEpisodes = episodes)
        }
    }

    val stopServerCall: Unit get() {
        releasesJob?.cancel()
        detailJob?.cancel()
        releasesJob = null
        detailJob = null
    }

    private val detailHandler = CoroutineExceptionHandler { _, t ->
        liveResourceDetail.setFailure(t.message ?: "Error encountered while show!!!")
        t.printStackTrace()
    }

    private val episodesHandler = CoroutineExceptionHandler { _, t ->
        liveResourceReleases.setFailure(t.message ?: "Error encountered while fetching show releases!!!")
        liveResourceReleasesTime.set()
        t.printStackTrace()
    }

    fun initialize(link: String) {
        val episodes = getReleases(link)
        val detail = getCacheDetail(link)
        if (detail.isCacheInvalid { plusDays(2) } && episodes.isCacheInvalid { plusHours(4) }) {
            refreshFromServer(link = link, detail = detail, episodes = episodes)
        } else {
            if (episodes.isCacheInvalid { plusDays(3) }) {
                detail?.value?.sid?.let {
                    onLoadEpisodes(link = link, sid = it, episodes = episodes)
                }
            } else {
                liveResourceReleasesTime.set(episodes?.time?.zonedDateTimeISO)
                liveResourceReleases.setSuccess(episodes?.value)
            }
            if (detail.isCacheInvalid { plusDays(14) }) {
                onLoadDetail(link = link, detail = detail)
            } else {
                liveResourceDetailTime.set(detail?.time?.zonedDateTimeISO)
                liveResourceDetail.setSuccess(detail?.value)
            }
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

    private fun getReleases(link: String) =
        rReleases.onGetData(link, FileType.RELEASES, ReleaseResult::class.java)

    private fun ReleaseResult.onSaveReleases(link: String) =
        rReleases.onCacheData(link, FileType.RELEASES, this)

    class ShowResult(time: String?, t: ItemShow?):
        RepoResut<ItemShow>(time ?: "", t)

    class ReleaseResult(time: String?, t: ItemReleasePage?):
        RepoResut<ItemReleasePage>(time ?: "", t)
}