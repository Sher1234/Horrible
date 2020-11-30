package info.horriblesubs.sher.ui.b.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.ShowsRepository
import info.horriblesubs.sher.data.subsplease.api.model.ItemList
import java.time.ZonedDateTime

class ShowsModel: ViewModel() {
    val resourceShows: LiveData<RepositoryResult<List<ItemList>>> by lazy { ShowsRepository.liveShows }
    val resourceTime: LiveData<ZonedDateTime> by lazy { ShowsRepository.liveTime }
    val refreshDataFromServer: Unit get() = ShowsRepository.refreshFromServer()
    val stopServerCall: Unit get() = ShowsRepository.stopServerCall
}