package info.horriblesubs.sher.data.cache

import info.horriblesubs.sher.App
import java.io.File

object CacheHelper {
    fun onSaveToCache(folderName: String, fileContents: String, fileName: String) {
        val folder = File(App.get().cacheDir, folderName)
        if (!folder.exists()) folder.mkdirs()
        val file = File(folder, fileName)
        file.writeText(fileContents, Charsets.UTF_8)
    }

    fun onReadFromCache(folderName: String, fileName: String): String? {
        val folder = File(App.get().cacheDir, folderName)
        if (!folder.exists()) folder.mkdirs()
        val file = File(folder, fileName)
        return if (file.exists()) file.readText() else null
    }
}