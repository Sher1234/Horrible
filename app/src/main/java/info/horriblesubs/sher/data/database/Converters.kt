@file:JvmName("DatabaseConverters")

package info.horriblesubs.sher.data.database

import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.model.NotificationItem
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.model.ItemLatest
import info.horriblesubs.sher.data.horrible.api.model.ItemShow

fun BookmarkedShow.toItemShow() = ItemShow(
    image = image, id = id, title = title,
    link = link, sid = sid, body = body
)

fun ItemShow.toBookmarkedShow() = BookmarkedShow(
    link = link, sid = sid?:"", body = body,
    image = imageUrl, id = id, title = title
)

fun ItemLatest.toNotificationItem() = NotificationItem(
    release = release?:"", title = title,
    link = link, id = id
)

fun Map<String, String>.toNotificationItem() = NotificationItem(
    release = this["release"],
    id = this["id"] ?: "",
    title = this["title"],
    link = this["link"]
)

fun List<ItemLatest>.toNotifications(): List<NotificationItem> {
    val arrayList = arrayListOf<NotificationItem>()
    this.forEach { arrayList.add(it.toNotificationItem()) }
    return arrayList
}

fun List<ItemShow>.toBookmarks(): ArrayList<BookmarkedShow> {
    val arrayList = arrayListOf<BookmarkedShow>()
    this.forEach { arrayList.add(it.toBookmarkedShow()) }
    return arrayList
}
