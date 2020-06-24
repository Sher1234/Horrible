package info.horriblesubs.sher.ui.e.character_staff

import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.mal.anime.AnimeRepository

class CharacterStaffModel: ViewModel() {

    private val repository = AnimeRepository.CharacterStaff()

    val liveData = repository.livePage
    val liveTime = repository.liveTime

    fun refreshDataFromServer(id: Int?) {
        if (id != null && id > 0)
            repository.refreshFromServer(id)
    }

    fun initialize(id: Int?) {
        if (id != null && id > 0 && liveData.value == null)
            repository.initialize(id)
    }

    val stopServerCall: Unit get() {
        repository.stopServerCall
    }
}