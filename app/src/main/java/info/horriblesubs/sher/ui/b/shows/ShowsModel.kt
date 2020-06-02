package info.horriblesubs.sher.ui.b.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.ShowsRepository
import info.horriblesubs.sher.data.horrible.api.model.ItemList

class ShowsModel: ViewModel() {
    val resourceCurrent: LiveData<RepositoryResult<List<ItemList>>> = ShowsRepository.liveCurrent
    val resourceAll: LiveData<RepositoryResult<List<ItemList>>> = ShowsRepository.liveAllShows
    val resourceTime = ShowsRepository.liveTime
    val allShowing = MutableLiveData<Boolean>()

    val refreshDataFromServer: Unit get() {
        ShowsRepository.refreshFromServer(true)
    }

    val stopServerCall: Unit get() {
        ShowsRepository.stopServerCall
    }
}