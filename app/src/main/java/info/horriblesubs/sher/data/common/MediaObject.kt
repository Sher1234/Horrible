package info.horriblesubs.sher.data.common

import info.horriblesubs.sher.data.database.model.BookmarkedShow
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
import info.horriblesubs.sher.data.subsplease.api.model.*
import java.util.*

@Suppress("Unused")
data class MediaObject<T> (val value: T?) {
    val imageUrl get() = when(value) {
        is BaseWithImage -> if (value.imageUrl == null ||
            value.imageUrl?.contains("questionmark_23.gif") == true) null else value.imageUrl
        is ItemTodaySchedule.Show -> value.imageUrl
        is ItemSchedule.Show -> value.imageUrl
        is BookmarkedShow -> value.imageUrl
        is ItemLatest -> value.imageUrl
        is SearchItem -> value.imageUrl
        is ItemShow -> value.image
        else -> null
    }
    val searchTerm get() = when(value) {
        is AnimeCharacter -> "${value.name}   ${value.role}   ${value.voiceActors?.joinToString("   ") ?: ""}"
            .toLowerCase(Locale.getDefault())
        is BeingSearchItem -> "${value.name}   ${value.alternativeNames?.joinToString("   ") ?: ""}"
            .toLowerCase(Locale.getDefault())
        is AnimeStaff -> "${value.name}   ${value.positions?.joinToString("   ") ?: ""}"
            .toLowerCase(Locale.getDefault())
        is MediaSearchItem -> "${value.title}   ${value.synopsis}".toLowerCase(Locale.getDefault())
        is VoiceActor -> "${value.name}   ${value.language}".toLowerCase(Locale.getDefault())
        is ItemTodaySchedule.Show -> value.title.toLowerCase(Locale.getDefault())
        is ItemSchedule.Show -> value.title.toLowerCase(Locale.getDefault())
        is BookmarkedShow -> value.title.toLowerCase(Locale.getDefault())
        is ItemLatest -> value.show.toLowerCase(Locale.getDefault())
        is ItemList -> value.title.toLowerCase(Locale.getDefault())
        is ItemShow -> value.title.toLowerCase(Locale.getDefault())
        else -> null
    }
    val header get() = when(value) {
        is ItemTodaySchedule.Show -> value.title
        is ItemSchedule.Show -> value.title
        is MediaSearchItem -> value.title
        is BeingSearchItem -> value.name
        is BookmarkedShow -> value.title
        is MediaCharacter -> value.name
        is BaseWithName -> value.name
        is ItemLatest -> value.show
        is ItemShow -> value.title
        is ItemList -> value.title
        else -> null
    }
    val subhead get() = when(value) {
        is AnimeStaff -> value.positions?.joinToString(limit = 2)
        is ItemTodaySchedule.Show -> value.time
        is ItemSchedule.Show -> value.time
        is AnimeCharacter -> value.role
        is VoiceActor -> value.language
        is ItemShow -> value.synopsis
        else -> null
    }
    val url get() = when(value) {
        is ItemTodaySchedule.Show -> value.page
        is ItemSchedule.Show -> value.page
        is BookmarkedShow -> value.url
        is MediaCharacter -> value.url
        is ItemLatest -> value.page
        is SearchItem -> value.url
        is ItemList -> value.link
        is ItemShow -> value.url
        is Base -> value.url
        else -> null
    }
    val id get() = when(value) {
        is ItemLatest -> "${value.page}${value.episode}${value.show}"
        is ItemTodaySchedule.Show -> value.page
        is ItemSchedule.Show -> value.page
        is MediaCharacter -> value.malId
        is BookmarkedShow -> value.sid
        is ItemList -> value.link
        is ItemShow -> value.sid
        is Base -> value.malId
        else -> null
    }
}