package info.horriblesubs.sher.ui.b.explore.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.TrendingRepository
import info.horriblesubs.sher.data.horrible.api.model.ItemList

class TrendingModel: ViewModel() {
    val resource: LiveData<RepositoryResult<List<ItemList>>> = TrendingRepository.liveResource
    val refreshDataFromServer: Unit get() {
        TrendingRepository.refreshFromServer()
    }
    val stopServerCall: Unit get() {
        TrendingRepository.stopServerCall
    }
}