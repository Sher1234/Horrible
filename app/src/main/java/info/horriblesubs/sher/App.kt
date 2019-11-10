package info.horriblesubs.sher

import android.app.Application
import com.google.android.gms.ads.MobileAds
import info.horriblesubs.sher.common.Constants

class App: Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Constants.theme()
        Constants.subscribe()
        MobileAds.initialize(this, resources.getString(R.string.ad_mob_app_id))
    }
}