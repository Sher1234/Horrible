package info.horriblesubs.sher.ui.b.explore.latest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.LatestRepository
import info.horriblesubs.sher.data.subsplease.api.model.ItemLatest
import info.horriblesubs.sher.functions.getRelativeTime
import java.time.ZonedDateTime
import java.util.*
import kotlin.concurrent.timerTask

class LatestModel: ViewModel() {
    val resource: LiveData<RepositoryResult<LinkedHashMap<String, ItemLatest>>> = LatestRepository.liveResource
    val resourceTimeLive = MutableLiveData<String>()
    private var currentTime = ZonedDateTime.now()
    val resourceTime = LatestRepository.liveTime
    private val timer = Timer().apply {
        scheduleAtFixedRate(timerTask {
            currentTime = currentTime.plusSeconds(1)
            resourceTimeLive.postValue(getRelativeTime(currentTime, resourceTime.value))
        }, 0, 1000)
    }

    val refreshDataFromServer: Unit get() {
        LatestRepository.refreshFromServer()
    }

    val stopServerCall: Unit get() {
        LatestRepository.stopServerCall
    }

    val killTimer: Unit get() {
        timer.cancel()
    }
}