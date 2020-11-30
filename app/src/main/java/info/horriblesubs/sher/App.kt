package info.horriblesubs.sher

import android.app.Application
import com.google.android.gms.ads.MobileAds
import info.horriblesubs.sher.service.Notifications

class App: Application() {
    companion object {
        private lateinit var instance: App
        fun get() = instance
    }
    init { instance = this }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        Notifications.createChannels(this)
//        DatabaseMigrationHelper.onMigrate()
        SettingsMigrationHelper.onMigrate()
    }
}