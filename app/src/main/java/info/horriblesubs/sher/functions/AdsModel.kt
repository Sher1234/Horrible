package info.horriblesubs.sher.functions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdsModel: ViewModel() {
    private val isInterstitialShownLive = MutableLiveData<Boolean>().apply { value = false }
    var isInterstitialShown: Boolean
        get() = isInterstitialShownLive.value ?: false
        set(value) { isInterstitialShownLive.value = value }
}