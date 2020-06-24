package info.horriblesubs.sher.ui.e

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnimeModel: ViewModel() {

    private val livePosition = MutableLiveData<String>()
    val liveSharedId = MutableLiveData<Int>()

    internal var itemId: String?
        set(value) { livePosition.value = value }
        get() = livePosition.value

    var id: Int?
        get() = liveSharedId.value
        set(value) { liveSharedId.value = value }

}