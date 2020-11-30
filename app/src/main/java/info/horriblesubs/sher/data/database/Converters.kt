@file:JvmName("DatabaseConverters")
@file:Suppress("Unused")

package info.horriblesubs.sher.data.database

import com.google.gson.Gson
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.model.NotificationItem
import info.horriblesubs.sher.data.subsplease.api.model.ItemLatest
import info.horriblesubs.sher.data.subsplease.api.model.ItemShow
import info.horriblesubs.sher.data.subsplease.api.model.imageUrl

fun BookmarkedShow.toItemShow() = ItemShow(
    image = image, title = title, url = url,
    sid = sid, synopsis = synopsis
)

fun ItemShow.toBookmarkedShow() = BookmarkedShow(
    image = image, title = title, url = url,
    sid = sid, synopsis = synopsis
)

fun ItemLatest.toNotificationItem() = NotificationItem(
    episode = episode, show = show, image_url = imageUrl, page = page, id = "$page-$episode"
)

fun Map<String, String>.toNotificationItem() = NotificationItem(
    image_url = (this["image_url"] ?: ""),
    episode = (this["episode"] ?: ""),
    show = (this["show"] ?: ""),
    page = (this["page"] ?: ""),
    id = (this["id"] ?: "")
)

inline fun <reified T> stringToArray(s: String?, clazz: Class<Array<T>>): List<T> {
    val arr = Gson().fromJson(s, clazz) ?: emptyArray()
    return arr.toList()
}

fun Map<String, String>.toNotificationItems2(): List<NotificationItem> =
    stringToArray(this["releases"], Array<NotificationItem>::class.java)

fun HashMap<String, ItemLatest>.toNotifications(): List<NotificationItem> {
    val arrayList = arrayListOf<NotificationItem>()
    this.forEach { arrayList.add(it.value.toNotificationItem()) }
    return arrayList
}

fun List<ItemShow>.toBookmarks(): ArrayList<BookmarkedShow> {
    val arrayList = arrayListOf<BookmarkedShow>()
    this.forEach { arrayList.add(it.toBookmarkedShow()) }
    return arrayList
}
