package info.horriblesubs.sher.data.common

import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.model.ItemBase

data class MediaObject<T> (val value: T?) {
    val imageUrl get() = when(value) {
        is BookmarkedShow -> value.image
        is ItemBase -> value.imageUrl
        else -> null
    }
    val searchTerm get() = when(value) {
        is BookmarkedShow -> value.title
        is ItemBase -> value.title
        else -> null
    }
    val header get() = when(value) {
        is BookmarkedShow -> value.title
        is ItemBase -> value.title
        else -> null
    }
    val url get() = when(value) {
        is BookmarkedShow -> value.link
        is ItemBase -> value.link
        else -> null
    }
    val id get() = when(value) {
        is BookmarkedShow -> value.id
        is ItemBase -> value.sid
        else -> null
    }
}