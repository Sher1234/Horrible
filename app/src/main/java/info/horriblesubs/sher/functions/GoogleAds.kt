package info.horriblesubs.sher.functions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.viewModels
import kotlin.random.Random
import com.google.android.gms.ads.AdRequest.Builder as AdBuilder

class GoogleAds(storeOwner: ViewModelStoreOwner) {

    private val model by storeOwner.viewModels<AdsModel>()

    companion object {
        private const val INTERSTITIAL = 87256
        private const val BANNER = 98713
    }

    private fun getAdId(type: Int): String? {
        val array = when(type) {
            INTERSTITIAL -> App.get().resources.getStringArray(R.array.interstitial)
            BANNER -> App.get().resources.getStringArray(R.array.footer)
            else -> null
        }
        return array?.get(Random.nextInt(array.size))
    }

    fun getInterstitialAd(context: Context) {
        if (model.isInterstitialShown) return
        val interstitialAd = InterstitialAd(context)
        interstitialAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(e: Int) {
                interstitialAd.loadAd(AdBuilder().build())
            }
            override fun onAdLoaded() {
                Looper.myLooper()?.let {
                    Handler(it).postDelayed({
                        if (!model.isInterstitialShown) {
                            interstitialAd.show()
                            model.isInterstitialShown = true
                        }
                    }, 525)
                }
            }
        }
        interstitialAd.adUnitId = getAdId(INTERSTITIAL)
        interstitialAd.loadAd(AdBuilder().build())
    }

    fun getBannerAd(context: Context, layout: FrameLayout?) {
        layout?.addView(AdView(context).apply {
            adSize = AdSize.SMART_BANNER
            adUnitId = getAdId(BANNER)
            loadAd(AdBuilder().build())
        })
    }
}