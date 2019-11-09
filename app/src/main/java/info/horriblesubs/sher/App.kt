package info.horriblesubs.sher

import android.app.Application
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
    }
}