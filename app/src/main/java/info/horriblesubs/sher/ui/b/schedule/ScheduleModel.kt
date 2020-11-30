package info.horriblesubs.sher.ui.b.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.ScheduleRepository
import info.horriblesubs.sher.data.subsplease.api.model.ItemSchedule
import java.time.ZonedDateTime

class ScheduleModel: ViewModel() {
    val resource: LiveData<RepositoryResult<ItemSchedule>> = ScheduleRepository.liveResource
    val resourceTime: LiveData<ZonedDateTime> = ScheduleRepository.liveTime
    val stopServerCall: Unit get() = ScheduleRepository.stopServerCall
    val refreshDataFromServer: Unit get() = ScheduleRepository.refreshFromServer()
}