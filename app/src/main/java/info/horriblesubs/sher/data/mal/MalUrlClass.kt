package info.horriblesubs.sher.data.mal

import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimePage
import info.horriblesubs.sher.data.mal.api.model.main.character.CharacterPage
import info.horriblesubs.sher.data.mal.api.model.main.manga.MangaPage
import info.horriblesubs.sher.data.mal.api.model.main.person.PersonPage
import info.horriblesubs.sher.data.mal.api.model.main.user.UserPage

object MalUrlClass {

    fun get(url: String?): Class<out Base>? {
        if(url.isNullOrBlank()) return null
        return when(parseUrl(url)) {
            "character" -> CharacterPage::class.java
            "people" -> PersonPage::class.java
            "anime" -> AnimePage::class.java
            "manga" -> MangaPage::class.java
            "user" -> UserPage::class.java
            else -> null
        }
    }

    private fun parseUrl(url: String): String {
        return when {
            url.contains("https://myanimelist.net/character/", true) -> "character"
            url.contains("https://myanimelist.net/people/", true) -> "people"
            url.contains("https://myanimelist.net/profile/", true) -> "user"
            url.contains("https://myanimelist.net/anime/", true) -> "anime"
            url.contains("https://myanimelist.net/manga/", true) -> "manga"
            else -> ""
        }
    }
}