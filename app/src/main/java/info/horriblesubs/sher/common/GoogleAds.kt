package info.horriblesubs.sher.common

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest.Builder
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import kotlin.random.Random

enum class GoogleAds {
    BANNER, INTERSTITIAL;
    private val context: Context get() = App.instance
    private val id: String get() {
        val array = when(this) {
            INTERSTITIAL -> context.resources.getStringArray(R.array.interstitial)
            BANNER -> context.resources.getStringArray(R.array.footer)
        }
        return array[Random.nextInt(if(this == BANNER) 4 else 3)]
    }
    fun ad(view: View? = null) {
        when(this) {
            BANNER -> {
                val layout: FrameLayout? = view?.findViewById(R.id.adLayout)
                val request = Builder().build()
                val adView = AdView(context)
                adView.adUnitId = id
                adView.adSize = AdSize.SMART_BANNER
                layout?.addView(adView)
                adView.loadAd(request)
            }
            INTERSTITIAL -> {
                var viewed = false
                val interstitialAd = InterstitialAd(context)
                interstitialAd.adListener = object : AdListener() {
                    override fun onAdFailedToLoad(e: Int) {
                        interstitialAd.loadAd(Builder().build())
                    }
                    override fun onAdLoaded() {
                        Handler().postDelayed({
                            if (!viewed) {
                                interstitialAd.show()
                                viewed = true
                            }
                        }, 525)
                    }
                }
                val request = Builder().build()
                interstitialAd.adUnitId = id
                interstitialAd.loadAd(request)
            }
        }
    }
}