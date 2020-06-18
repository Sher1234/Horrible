@file:Suppress("Unused")

package info.horriblesubs.sher.data.mal.api

import com.google.gson.GsonBuilder
import info.horriblesubs.sher.data.mal.api.model.common.media_character.MediaCharacter
import info.horriblesubs.sher.data.mal.api.model.common.media_character.MediaCharactersPage
import info.horriblesubs.sher.data.mal.api.model.common.search.AdvancedSearch
import info.horriblesubs.sher.data.mal.api.model.common.search.SearchPage
import info.horriblesubs.sher.data.mal.api.model.common.updates.UserUpdatesPage
import info.horriblesubs.sher.data.mal.api.model.common.user.AdvancedUserList
import info.horriblesubs.sher.data.mal.api.model.enums.Days
import info.horriblesubs.sher.data.mal.api.model.main.MoreInfo
import info.horriblesubs.sher.data.mal.api.model.main.anime.*
import info.horriblesubs.sher.data.mal.api.model.main.anime.episodes.AnimeEpisodesPage
import info.horriblesubs.sher.data.mal.api.model.main.anime.staff.AnimeCharactersStaffPage
import info.horriblesubs.sher.data.mal.api.model.main.anime.videos.AnimeVideosPage
import info.horriblesubs.sher.data.mal.api.model.main.character.CharacterPage
import info.horriblesubs.sher.data.mal.api.model.main.character.CharacterSearchItem
import info.horriblesubs.sher.data.mal.api.model.main.club.ClubPage
import info.horriblesubs.sher.data.mal.api.model.main.club.members.ClubMembersPage
import info.horriblesubs.sher.data.mal.api.model.main.forum.ForumsPage
import info.horriblesubs.sher.data.mal.api.model.main.genre.GenreAnimePage
import info.horriblesubs.sher.data.mal.api.model.main.genre.manga.GenreMangaPage
import info.horriblesubs.sher.data.mal.api.model.main.magazine.MagazinesPage
import info.horriblesubs.sher.data.mal.api.model.main.manga.*
import info.horriblesubs.sher.data.mal.api.model.main.news.NewsPage
import info.horriblesubs.sher.data.mal.api.model.main.person.PersonPage
import info.horriblesubs.sher.data.mal.api.model.main.person.PersonSearchItem
import info.horriblesubs.sher.data.mal.api.model.main.pictures.PicturesPage
import info.horriblesubs.sher.data.mal.api.model.main.producer.ProducerPage
import info.horriblesubs.sher.data.mal.api.model.main.recommendations.RecommendationsPage
import info.horriblesubs.sher.data.mal.api.model.main.reviews.ReviewPage
import info.horriblesubs.sher.data.mal.api.model.main.schedule.SchedulePage
import info.horriblesubs.sher.data.mal.api.model.main.season.SeasonalAnimePage
import info.horriblesubs.sher.data.mal.api.model.main.season.archive.SeasonArchivePage
import info.horriblesubs.sher.data.mal.api.model.main.top.*
import info.horriblesubs.sher.data.mal.api.model.main.user.UserPage
import info.horriblesubs.sher.data.mal.api.model.main.user.friends.UserFriendsPage
import info.horriblesubs.sher.data.mal.api.model.main.user.history.UserHistoryPage
import info.horriblesubs.sher.data.mal.api.model.main.user.lists.anime.UserAnimeListPage
import info.horriblesubs.sher.data.mal.api.model.main.user.lists.manga.UserMangaListPage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MalApi {
    companion object {
        private const val link = "https://api.jikan.moe/v3/"
        fun <T: MalApi> api(type: String, clazz: Class<T>): T {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl("$link$type").build().create(clazz)
        }
    }

    interface Anime: MalApi {
        companion object {
            val api: Anime get() = api("anime/", Anime::class.java)
        }

        @GET("{id}")
        suspend fun get(@Path(value = "id", encoded = true) id: Int): AnimePage?

        @GET("{id}/characters_staff")
        suspend fun getCharactersStaff(@Path(value = "id", encoded = true) id: Int): AnimeCharactersStaffPage?

        @GET("{id}/episodes/{page}")
        suspend fun getEpisodes(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): AnimeEpisodesPage?

        @GET("{id}/news")
        suspend fun getNews(@Path(value = "id", encoded = true) id: Int): NewsPage?

        @GET("{id}/pictures")
        suspend fun getPictures(@Path(value = "id", encoded = true) id: Int): PicturesPage?

        @GET("{id}/videos")
        suspend fun getVideos(@Path(value = "id", encoded = true) id: Int): AnimeVideosPage?

        @GET("{id}/stats")
        suspend fun getStats(@Path(value = "id", encoded = true) id: Int): AnimeStatsPage?

        @GET("{id}/forum")
        suspend fun getForum(@Path(value = "id", encoded = true) id: Int): ForumsPage?

        @GET("{id}/moreinfo")
        suspend fun getMoreInfo(@Path(value = "id", encoded = true) id: Int): MoreInfo?

        @GET("{id}/reviews/{page}")
        suspend fun getReviews(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): ReviewPage<AnimeScore>?

        @GET("{id}/recommendations")
        suspend fun getRecommendations(@Path(value = "id", encoded = true) id: Int): RecommendationsPage?

        @GET("{id}/userupdates/{page}")
        suspend fun getUserUpdates(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): UserUpdatesPage<AnimeUserUpdateItem>?
    }

    interface Manga: MalApi {
        companion object {
            val api: Manga get() = api("manga/", Manga::class.java)
        }

        @GET("{id}")
        suspend fun get(@Path(value = "id", encoded = true) id: Int): MangaPage?

        @GET("{id}/characters")
        suspend fun getCharacters(@Path(value = "id", encoded = true) id: Int): MediaCharactersPage<MediaCharacter>?

        @GET("{id}/news")
        suspend fun getNews(@Path(value = "id", encoded = true) id: Int): NewsPage?

        @GET("{id}/pictures")
        suspend fun getPictures(@Path(value = "id", encoded = true) id: Int): PicturesPage?

        @GET("{id}/stats")
        suspend fun getStats(@Path(value = "id", encoded = true) id: Int): MangaStatsPage?

        @GET("{id}/forum")
        suspend fun getForum(@Path(value = "id", encoded = true) id: Int): ForumsPage?

        @GET("{id}/moreinfo")
        suspend fun getMoreInfo(@Path(value = "id", encoded = true) id: Int): MoreInfo?

        @GET("{id}/reviews/{page}")
        suspend fun getReviews(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): ReviewPage<MangaScore>?

        @GET("{id}/recommendations")
        suspend fun getRecommendations(@Path(value = "id", encoded = true) id: Int): RecommendationsPage?

        @GET("{id}/userupdates/{page}")
        suspend fun getUserUpdates(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): UserUpdatesPage<MangaUserUpdateItem>?
    }

    interface Person: MalApi {
        companion object {
            val service: Person get() = api("person/", Person::class.java)
        }

        @GET("{id}")
        suspend fun get(@Path(value = "id", encoded = true) id: Int): PersonPage?

        @GET("{id}/pictures")
        suspend fun getPictures(@Path(value = "id", encoded = true) id: Int): PicturesPage?
    }

    interface Character: MalApi {
        companion object {
            val service: Character get() = api("character/", Character::class.java)
        }

        @GET("{id}")
        suspend fun get(@Path(value = "id", encoded = true) id: Int): CharacterPage?

        @GET("{id}/pictures")
        suspend fun getPictures(@Path(value = "id", encoded = true) id: Int): PicturesPage?
    }

    interface Search: MalApi {
        companion object {
            val service: Search get() = api("search/", Search::class.java)
        }

        @GET("anime")
        suspend fun getSearchAnime(
            @Query("q") q: String,
            @Query("page") page: Int = 1,
            @QueryMap queries: Map<String, String>
        ): SearchPage<AnimeSearchItem>?

        @GET("manga")
        suspend fun getSearchManga(
            @Query("q") q: String,
            @Query("page") page: Int = 1,
            @QueryMap queries: Map<String, String> = AdvancedSearch.Manga().get()
        ): SearchPage<MangaSearchItem>?

        @GET("character")
        suspend fun getSearchCharacter(
            @Query("q") q: String,
            @Query("page") page: Int = 1
        ): SearchPage<CharacterSearchItem>?

        @GET("person")
        suspend fun getSearchPerson(
            @Query("q") q: String,
            @Query("page") page: Int = 1
        ): SearchPage<PersonSearchItem>?
    }

    interface Season: MalApi {
        companion object {
            val service: Season get() = api("season/", Season::class.java)
        }

        @GET("{year}/{param}")
        suspend fun get(
            @Path(value = "year", encoded = true) year: Int,
            @Path(value = "param", encoded = true) season: String
        ): SeasonalAnimePage?

        @GET("archive")
        suspend fun getArchive(): SeasonArchivePage?

        @GET("later")
        suspend fun getLater(): SeasonalAnimePage?
    }

    interface Schedule: MalApi {
        companion object {
            val service: Schedule get() = api("schedule/", Schedule::class.java)
        }

        @GET("{day}")
        suspend fun get(@Path(value = "day", encoded = true) day: String = Days.ALL.toString()): SchedulePage?
    }

    interface Top: MalApi {
        companion object {
            val service: Top get() = api("top/", Top::class.java)
        }

        @GET("anime/{page}/{type}")
        suspend fun getAnimes(
            @Path(value = "page", encoded = true) page: Int = 1,
            @Path(value = "type", encoded = true) type: String = ""
        ): TopPage<TopAnimeItem>?

        @GET("manga/{page}/{type}")
        suspend fun getMangas(
            @Path(value = "page", encoded = true) page: Int = 1,
            @Path(value = "type", encoded = true) type: String = ""
        ): TopPage<TopMangaItem>?

        @GET("people/{page}")
        suspend fun getPeople(
            @Path(value = "page", encoded = true) page: Int = 1
        ): TopPage<TopPersonItem>?

        @GET("characters/{page}")
        suspend fun getCharacters(
            @Path(value = "page", encoded = true) page: Int = 1
        ): TopPage<TopCharacterItem>?
    }

    interface Genre: MalApi {
        companion object {
            val service: Genre get() = api("genre/", Genre::class.java)
        }

        @GET("anime/{id}/{page}")
        suspend fun getAnimes(
            @Path(value = "page", encoded = true) page: Int = 1,
            @Path(value = "id", encoded = true) genre: String
        ): GenreAnimePage?

        @GET("manga/{id}/{page}")
        suspend fun getMangas(
            @Path(value = "page", encoded = true) page: Int = 1,
            @Path(value = "id", encoded = true) genre: String
        ): GenreMangaPage?
    }

    interface Producer: MalApi {
        companion object {
            val service: Producer get() = api("producer/", Producer::class.java)
        }

        @GET("{id}/{page}")
        suspend fun get(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): ProducerPage?
    }

    interface Magazine: MalApi {
        companion object {
            val service: Magazine get() = api("magazine/", Magazine::class.java)
        }

        @GET("{id}/{page}")
        suspend fun get(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): MagazinesPage?
    }

    interface Club: MalApi {
        companion object {
            val service: Club get() = api("club/", Club::class.java)
        }

        @GET("{id}")
        suspend fun get(@Path(value = "id", encoded = true) id: Int): ClubPage?

        @GET("{id}/members/{page}")
        suspend fun getMembers(
            @Path(value = "id", encoded = true) id: Int,
            @Path(value = "page", encoded = true) page: Int = 1
        ): ClubMembersPage?
    }

    interface User: MalApi {
        companion object {
            val service: User get() = api("user/", User::class.java)
        }

        @GET("{username}")
        suspend fun get(@Path(value = "username", encoded = true) username: String): UserPage?

        @GET("{username}/history/{type}")
        suspend fun getHistory(
            @Path(value = "username", encoded = true) username: String,
            @Path(value = "type", encoded = true) type: String = ""
        ): UserHistoryPage?

        @GET("{username}/friends/{page}")
        suspend fun getFriends(
            @Path(value = "username", encoded = true) username: String,
            @Path(value = "page", encoded = true) page: Int = 1
        ): UserFriendsPage?

        @GET("{username}/animelist/{type}")
        suspend fun getAnimeList(
            @Path(value = "username", encoded = true) username: String,
            @Query("type") type: String = AdvancedUserList.Anime().filter.toString(),
            @QueryMap queries: Map<String, String> = AdvancedUserList.Anime().get()
        ): UserAnimeListPage?

        @GET("{username}/mangalist/{type}")
        suspend fun getMangaList(
            @Path(value = "username", encoded = true) username: String,
            @Query("type") type: String = AdvancedUserList.Manga().filter.toString(),
            @QueryMap queries: Map<String, String> = AdvancedUserList.Manga().get()
        ): UserMangaListPage?
    }
}