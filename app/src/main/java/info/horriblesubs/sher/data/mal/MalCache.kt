package info.horriblesubs.sher.data.mal

import android.util.Log
import com.google.gson.GsonBuilder
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.RepoResut
import info.horriblesubs.sher.data.cache.CacheHelper

class MalCache<T> (
    val type: FileType,
    val app: App = App.get()
) {

    fun <E: RepoResut<T>>onCacheData(jsonObject: E) {
        val jsonString = GsonBuilder().excludeFieldsWithModifiers()
            .create().toJson(jsonObject)
        CacheHelper.onSaveToCache(FileType.FOLDER, jsonString, type.fileName)
    }

    fun <E: RepoResut<T>>onCacheData(folderName: String, fileName: String, jsonObject: E) {
        val jsonString = GsonBuilder().excludeFieldsWithModifiers()
            .create().toJson(jsonObject)
        val folder = "${FileType.FOLDER}/${type.fileName}/$folderName"
        CacheHelper.onSaveToCache(folder, jsonString, "$fileName.json")
    }

    fun <E: RepoResut<T>> onGetData(clazz: Class<E>): E? {
        val jsonString = CacheHelper.onReadFromCache(FileType.FOLDER, type.fileName)
        return if (jsonString.isNullOrBlank()) null else
            GsonBuilder().excludeFieldsWithModifiers()
                .create().fromJson(jsonString, clazz)
    }

    fun <E: RepoResut<T>> onGetData(folderName: String, fileName: String, clazz: Class<E>): E? {
        val folder = "${FileType.FOLDER}/${type.fileName}/$folderName"
        val jsonString = CacheHelper.onReadFromCache(folder, "$fileName.json")
        Log.e("JsonString", jsonString?:"NULL")
        return if (jsonString.isNullOrBlank()) null else
            GsonBuilder().excludeFieldsWithModifiers()
                .create().fromJson(jsonString, clazz)
    }

    class FileType(private val type: String) {
        companion object {
            const val ANIME = "anime"

            const val FOLDER = "mal"

            const val PAGE = "page"
            const val CharacterStaff = "character-staff"
        }

        val fileName get() = type + if (type != ANIME) ".json" else ""
    }
}