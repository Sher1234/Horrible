package info.horriblesubs.sher.data.mal.anime

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.RepoResut
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.cache.*
import info.horriblesubs.sher.data.mal.MalCache
import info.horriblesubs.sher.data.mal.MalCache.FileType
import info.horriblesubs.sher.data.mal.api.MalApi
import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimePage
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharactersStaffPage as AcsPage

sealed class AnimeRepository {
    class Page {
        private val page = MalCache<AnimePage>(FileType(FileType.ANIME))
        val livePage = MutableLiveData<RepositoryResult<AnimePage>>()
        val liveTime = MutableLiveData<ZonedDateTime>()

        private var pageJob: Job? = null
            set(value) {
                field?.cancel()
                field = value
            }

        @WorkerThread
        private suspend fun callToWebServer(id: Int, cPage: PageResult?) {
            if(isNetworkAvailable) {
                liveTime.set()
                livePage.setLoading()
                val page = MalApi.Anime.api.get(id)
                val time = ZonedDateTime.now()
                livePage.setSuccess(page)
                liveTime.set(time)
                PageResult(
                    time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                    page
                ).onSaveDetail(id)
            } else {
                livePage.setSuccess(cPage?.value)
                liveTime.set(cPage?.time?.zonedDateTimeISO)
            }
        }

        @Suppress("deprecation")
        private val isNetworkAvailable: Boolean get() {
            return (App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
                ?.activeNetworkInfo?.isConnectedOrConnecting ?: false
        }

        fun refreshFromServer(id: Int, detail: PageResult? = getCache(id)) {
            pageJob = CoroutineScope(Dispatchers.IO + pageHandler).launch {
                callToWebServer(id, detail)
            }
        }

        private val pageHandler = CoroutineExceptionHandler { _, t ->
            livePage.setFailure(t.message ?: "Error encountered while show!!!")
            t.printStackTrace()
        }

        val stopServerCall: Unit get() {
            pageJob?.cancel()
            pageJob = null
        }

        private fun getCache(id: Int) =
            page.onGetData("$id", FileType.PAGE, PageResult::class.java)

        private fun PageResult.onSaveDetail(id: Int) =
            page.onCacheData("$id", FileType.PAGE, this)

        fun initialize(id: Int) {
            val pageResult = getCache(id)
            if (pageResult.isCacheInvalid { plusDays(1) }) {
                refreshFromServer(id, pageResult)
            } else {
                liveTime.set(pageResult?.time?.zonedDateTimeISO)
                livePage.setSuccess(pageResult?.value)
            }
        }

        class PageResult(time: String?, t: AnimePage?): RepoResut<AnimePage>(time ?: "", t)
    }
    class CharacterStaff {
        private val characterStaff = MalCache<AcsPage>(FileType(FileType.ANIME))
        val livePage = MutableLiveData<RepositoryResult<AcsPage>>()
        val liveTime = MutableLiveData<ZonedDateTime>()

        private var pageJob: Job? = null
            set(value) {
                field?.cancel()
                field = value
            }

        @WorkerThread
        private suspend fun callToWebServer(id: Int, cPage: PageResult?) {
            if(isNetworkAvailable) {
                liveTime.set()
                livePage.setLoading()
                val page = MalApi.Anime.api.getCharactersStaff(id)
                val time = ZonedDateTime.now()
                livePage.setSuccess(page)
                liveTime.set(time)
                PageResult(
                    time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                    page
                ).onSaveDetail(id)
            } else {
                livePage.setSuccess(cPage?.value)
                liveTime.set(cPage?.time?.zonedDateTimeISO)
            }
        }

        @Suppress("deprecation")
        private val isNetworkAvailable: Boolean get() {
            return (App.get().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
                ?.activeNetworkInfo?.isConnectedOrConnecting ?: false
        }

        fun refreshFromServer(id: Int, detail: PageResult? = getCache(id)) {
            pageJob = CoroutineScope(Dispatchers.IO + pageHandler).launch {
                callToWebServer(id, detail)
            }
        }

        private val pageHandler = CoroutineExceptionHandler { _, t ->
            livePage.setFailure(t.message ?: "Error encountered while show!!!")
            t.printStackTrace()
        }

        val stopServerCall: Unit get() {
            pageJob?.cancel()
            pageJob = null
        }

        private fun getCache(id: Int) =
            characterStaff.onGetData("$id", FileType.CharacterStaff, PageResult::class.java)

        private fun PageResult.onSaveDetail(id: Int) =
            characterStaff.onCacheData("$id", FileType.CharacterStaff, this)

        fun initialize(id: Int) {
            val pageResult = getCache(id)
            if (pageResult.isCacheInvalid { plusDays(1) }) {
                refreshFromServer(id, pageResult)
            } else {
                liveTime.set(pageResult?.time?.zonedDateTimeISO)
                livePage.setSuccess(pageResult?.value)
            }
        }

        class PageResult(time: String?, t: AcsPage?): RepoResut<AcsPage>(time ?: "", t)
    }
}