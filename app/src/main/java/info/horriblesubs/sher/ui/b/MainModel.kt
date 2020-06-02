package info.horriblesubs.sher.ui.b

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainModel: ViewModel() {
    private val livePosition = MutableLiveData<Int>()

    internal var itemId: Int?
        set(value) { livePosition.value = value }
        get() = livePosition.value
}