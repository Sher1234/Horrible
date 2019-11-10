package info.horriblesubs.sher.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.play.core.appupdate.AppUpdateInfo

class MainVM : ViewModel() {
    internal val uInfo: MutableLiveData<AppUpdateInfo?> = MutableLiveData()
    internal val uAvailable: MutableLiveData<Boolean?> = MutableLiveData()
    private val livePosition: MutableLiveData<Int> = MutableLiveData()
    internal var position: Int?
        get() = livePosition.value
        set(value) {livePosition.value = value}
}
