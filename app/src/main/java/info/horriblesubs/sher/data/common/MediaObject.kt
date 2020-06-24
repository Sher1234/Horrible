package info.horriblesubs.sher.data.common

import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.horrible.api.imageUrl
import info.horriblesubs.sher.data.horrible.api.model.ItemBase
import info.horriblesubs.sher.data.mal.api.model.common.VoiceActor
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithName
import info.horriblesubs.sher.data.mal.api.model.common.media_character.MediaCharacter
import info.horriblesubs.sher.data.mal.api.model.common.search.BeingSearchItem
import info.horriblesubs.sher.data.mal.api.model.common.search.MediaSearchItem
import info.horriblesubs.sher.data.mal.api.model.common.search.SearchItem
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharacter
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeStaff

@Suppress("Unused")
data class MediaObject<T> (val value: T?) {
    val imageUrl get() = when(value) {
        is BaseWithImage -> if (value.imageUrl == null ||
            value.imageUrl?.contains("questionmark_23.gif") == true) null else value.imageUrl
        is BookmarkedShow -> value.image
        is SearchItem -> value.imageUrl
        is ItemBase -> value.imageUrl
        else -> null
    }
    val searchTerm get() = when(value) {
        is AnimeCharacter -> "${value.name}   ${value.role}   ${value.voiceActors?.joinToString("   ") ?: ""}"
        is BeingSearchItem -> "${value.name}   ${value.alternativeNames?.joinToString("   ") ?: ""}"
        is AnimeStaff -> "${value.name}   ${value.positions?.joinToString("   ") ?: ""}"
        is MediaSearchItem -> value.title + "   " + value.synopsis
        is VoiceActor -> "${value.name}   ${value.language}"
        is BookmarkedShow -> value.title
        is ItemBase -> value.title
        else -> null
    }
    val header get() = when(value) {
        is MediaSearchItem -> value.title
        is BeingSearchItem -> value.name
        is BookmarkedShow -> value.title
        is MediaCharacter -> value.name
        is BaseWithName -> value.name
        is ItemBase -> value.title
        else -> null
    }
    val subhead get() = when(value) {
        is AnimeStaff -> value.positions?.joinToString(limit = 2)
        is AnimeCharacter -> value.role
        is VoiceActor -> value.language
        else -> null
    }
    val url get() = when(value) {
        is BookmarkedShow -> value.link
        is MediaCharacter -> value.url
        is SearchItem -> value.url
        is ItemBase -> value.link
        is Base -> value.url
        else -> null
    }
    val id get() = when(value) {
        is MediaCharacter -> value.malId
        is BookmarkedShow -> value.id
        is ItemBase -> value.sid
        is Base -> value.malId
        else -> null
    }
}