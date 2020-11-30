package info.horriblesubs.sher.ui.b.explore.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.TodayRepository
import info.horriblesubs.sher.data.subsplease.api.model.ItemTodaySchedule

class TodayModel: ViewModel() {
    val resource: LiveData<RepositoryResult<ItemTodaySchedule>> = TodayRepository.liveResource
    val refreshDataFromServer: Unit get() {
        TodayRepository.refreshFromServer()
    }
    val stopServerCall: Unit get() {
        TodayRepository.stopServerCall
    }
}