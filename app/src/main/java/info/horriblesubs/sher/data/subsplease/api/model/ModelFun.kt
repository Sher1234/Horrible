package info.horriblesubs.sher.data.subsplease.api.model

import info.horriblesubs.sher.data.database.model.BookmarkedShow

val ItemLatest.resolutions: String get() {
    val string = StringBuilder("")
    downloads.forEach { string.append(it.res) }
    return string.toString()
}

val ItemLatest.imageUrl get() = if (image_url.contains("subsplease.org")) image_url else
    "https://subsplease.org$image_url"

val ItemSchedule.Show.imageUrl get() = if (image_url.contains("subsplease.org")) image_url else
    "https://subsplease.org$image_url"

val ItemTodaySchedule.Show.imageUrl get() = if (image_url.contains("subsplease.org")) image_url else
    "https://subsplease.org$image_url"

val ItemShow.imageUrl get() = if (image.contains("subsplease.org")) image else
    "https://subsplease.org$image"

val BookmarkedShow.imageUrl get() = if (image.contains("subsplease.org")) image else
    "https://subsplease.org$image"

val ItemRelease.SD: ItemRelease.Links? get() {
    var link: ItemRelease.Links? = null
    downloads.forEach {
        if (it.res.contains("360") || it.res.contains("480"))
            link = it
    }
    return link
}

val ItemRelease.HD: ItemRelease.Links? get() {
    var link: ItemRelease.Links? = null
    downloads.forEach {
        if (it.res.contains("720"))
            link = it
    }
    return link
}

val ItemRelease.FHD: ItemRelease.Links? get() {
    var link: ItemRelease.Links? = null
    downloads.forEach {
        if (it.res.contains("1080"))
            link = it
    }
    return link
}

fun <T> HashMap<String, T>.toArrayList(): List<T> {
    val arrayList = arrayListOf<T>()
    forEach { arrayList.add(it.value) }
    return arrayList
}