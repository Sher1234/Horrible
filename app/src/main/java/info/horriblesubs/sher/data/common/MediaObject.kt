package info.horriblesubs.sher.data.common

import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.model.ItemBase
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.search.BeingSearchItem
import info.horriblesubs.sher.data.mal.api.model.common.search.MediaSearchItem
import info.horriblesubs.sher.data.mal.api.model.common.search.SearchItem

@Suppress("Unused")
data class MediaObject<T> (val value: T?) {
    val imageUrl get() = when(value) {
        is BookmarkedShow -> value.image
        is SearchItem -> value.imageUrl
        is ItemBase -> value.imageUrl
        else -> null
    }
    val searchTerm get() = when(value) {
        is BeingSearchItem -> value.name + "   " + value.alternativeNames?.joinToString("   ")
        is MediaSearchItem -> value.title + "   " + value.synopsis
        is BookmarkedShow -> value.title
        is ItemBase -> value.title
        else -> null
    }
    val header get() = when(value) {
        is MediaSearchItem -> value.title
        is BeingSearchItem -> value.name
        is BookmarkedShow -> value.title
        is ItemBase -> value.title
        else -> null
    }
    val url get() = when(value) {
        is BookmarkedShow -> value.link
        is SearchItem -> value.url
        is ItemBase -> value.link
        else -> null
    }
    val id get() = when(value) {
        is BookmarkedShow -> value.id
        is ItemBase -> value.sid
        is Base -> value.malId
        else -> null
    }
}