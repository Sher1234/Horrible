package info.horriblesubs.sher.ui.c

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.subsplease.ShowRepository
import info.horriblesubs.sher.data.subsplease.api.model.ItemRelease

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

    val episodes = repository.liveResourceReleases
    val detail = repository.liveResourceDetail

    val episodesTime = repository.liveResourceReleasesTime
    val detailTime = repository.liveResourceDetailTime

    var sharedItem: ItemRelease?
        set(value) { liveSharedItem.value = value }
        get() = liveSharedItem.value

    val refreshDataFromServer: Unit get() {
        val link = showLink
        if (!link.isNullOrBlank())
            repository.refreshFromServer(link)
    }

    val refreshReleases: Unit get() {
        val link = showLink
        val sid = detail.value?.value?.sid
        if (!link.isNullOrBlank() && !sid.isNullOrBlank())
            repository.onLoadEpisodes(link, sid)
    }

    val stopServerCall: Unit get() {
        repository.stopServerCall
    }
}