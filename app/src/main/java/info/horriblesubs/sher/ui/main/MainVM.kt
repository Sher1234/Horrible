package info.horriblesubs.sher.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVM : ViewModel() {
    private val livePosition: MutableLiveData<Int> = MutableLiveData()
    internal var position: Int?
        get() = livePosition.value
        set(value) {livePosition.value = value}
}
