package info.horriblesubs.sher.libs.preference.prefs

import info.horriblesubs.sher.App
import info.horriblesubs.sher.libs.preference.model.BasePreference
import java.io.File
import kotlin.math.roundToInt

object AppCachePreference: BasePreference<String>() {

    override val key: String get() = "app_cache"
    override val type: Int get() = DEFAULT
    override val defaultValue = "0"
    override var value = "0"
        get() {
            var size = 0F
            size += getDirSize(App.get().externalCacheDir)
            size += getDirSize(App.get().cacheDir)
            val float = (((size*100)/1024)/1024).roundToInt().toFloat()/100F
            return "$float"
        }

    init {
        summaryProvider = object : TextProvider<String> {
            override fun provideText(preference: BasePreference<String>): String =
                "Tap to clear ${(preference as AppCachePreference).value}MB of application cache."
        }
        title = "Reset Application Cache"
    }

    private fun getDirSize(dir: File?): Float {
        var size = 0F
        dir?.listFiles()?.forEach {
            size += if (it != null && it.isDirectory) getDirSize(it)
            else if (it != null && it.isFile) it.length().toFloat()
            else 0F
        }
        return size
    }

    fun deleteCache() = try {
        deleteDir(App.get().externalCacheDir)
        deleteDir(App.get().cacheDir)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    private fun deleteDir(dir: File?) {
        when {
            dir?.isDirectory == true -> {
                (dir.list() ?: arrayOf()).forEach {
                    deleteDir(File(dir, it))
                }
                dir.delete()
            }
            dir?.isFile == true -> dir.delete()
        }
    }


    override fun migrate() {
        deleteCache()
    }
}