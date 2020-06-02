@file:JvmName("CommonObjectOperations")
@file:Suppress("Unused")

package info.horriblesubs.sher.data.common

fun <T> List<T>?.toMediaObjects(): ArrayList<MediaObject<T>> {
    val arrayList = arrayListOf<MediaObject<T>>()
    this?.forEach { arrayList.add(MediaObject(it)) }
    return arrayList
}

fun <T> List<MediaObject<T>>?.getSuggestions(): List<String> {
    val arrayList = arrayListOf<String>()
    this?.forEach { mediaObject ->
        mediaObject.searchTerm?.let {
            arrayList.add(it)
        }
    }
    return arrayList
}