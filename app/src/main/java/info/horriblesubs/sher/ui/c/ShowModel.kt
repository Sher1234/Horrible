package info.horriblesubs.sher.ui.c

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.horrible.ShowRepository
import info.horriblesubs.sher.data.horrible.api.HorribleApi
import info.horriblesubs.sher.data.horrible.api.model.ItemRelease

class ShowModel: ViewModel() {

    private val liveSharedShowLink = MutableLiveData<String>()
    val liveSharedItem = MutableLiveData<ItemRelease>()
    private val repository = ShowRepository()

    private val livePosition = MutableLiveData<String>()

    internal var itemId: String?
        set(value) { livePosition.value = value }
        get() = livePosition.value

    var showLink: String?
        get() = liveSharedShowLink.value
        set(value) {
            liveSharedShowLink.value = value
            if (!value.isNullOrBlank())
                repository.initialize(value)
        }

    val episodes = repository.liveResourceEpisodes
    val batches = repository.liveResourceBatches
    val detail = repository.liveResourceDetail

    val episodesTime = repository.liveResourceEpisodesTime
    val batchesTime = repository.liveResourceBatchesTime

    var sharedItem: ItemRelease?
        set(value) { liveSharedItem.value = value }
        get() = liveSharedItem.value

    val refreshDataFromServer: Unit get() {
        val link = showLink
        if (!link.isNullOrBlank())
            repository.refreshFromServer(link, true)
    }

    val refreshReleases: Unit get() {
        val link = showLink
        val sid = detail.value?.value?.sid
        if (!link.isNullOrBlank() && !sid.isNullOrBlank())
            repository.onLoadEpisodes(link, sid, reset = true)
    }

    val refreshBatches: Unit get() {
        val link = showLink
        val sid = detail.value?.value?.sid
        if (!link.isNullOrBlank() && !sid.isNullOrBlank())
            repository.onLoadBatches(link, sid, true)
    }

    val onLoadMoreEpisodes: Unit get() {
        val link = showLink
        val sid = detail.value?.value?.sid
        if (!link.isNullOrBlank() && !sid.isNullOrBlank())
            repository.onLoadEpisodes(link, sid, mode = HorribleApi.ApiEpisodes.SOME)
    }

    val onLoadAllEpisodes: Unit get() {
        val link = showLink
        val sid = detail.value?.value?.sid
        if (!link.isNullOrBlank() && !sid.isNullOrBlank())
            repository.onLoadEpisodes(link, sid, mode = HorribleApi.ApiEpisodes.ALL)
    }

    val stopServerCall: Unit get() {
        repository.stopServerCall
    }
}