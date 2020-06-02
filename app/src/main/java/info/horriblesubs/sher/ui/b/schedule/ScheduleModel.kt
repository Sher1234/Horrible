package info.horriblesubs.sher.ui.b.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.horrible.ScheduleRepository
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule

class ScheduleModel: ViewModel() {
    val resource: LiveData<RepositoryResult<List<List<ItemSchedule>>>> = ScheduleRepository.liveResource
    val resourceTime = ScheduleRepository.liveTime
    val refreshDataFromServer: Unit get() {
        ScheduleRepository.refreshFromServer(true)
    }

    val stopServerCall: Unit get() {
        ScheduleRepository.stopServerCall
    }
}