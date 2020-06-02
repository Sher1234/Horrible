package info.horriblesubs.sher.functions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdsModel: ViewModel() {
    private val isInterstitialShownLive = MutableLiveData<Boolean>()
    var isInterstitialShown: Boolean
        get() = isInterstitialShownLive.value ?: false
        set(value) { isInterstitialShownLive.value = value }

    init { isInterstitialShownLive.value = false }
}