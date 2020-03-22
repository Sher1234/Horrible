package info.horriblesubs.sher.api.mal.model

import com.google.gson.annotations.SerializedName

class Related (
    @SerializedName("Alternative version")
    val alternativeVersion: List<BaseMal>?,
    @SerializedName("Alternative setting")
    val alternativeSetting: List<BaseMal>?,
    @SerializedName("Side story")
    val sideStory: List<BaseMal>?,
    @SerializedName("Adaptation")
    val adaptations: List<BaseMal>?,
    @SerializedName("Summary")
    val summary: List<BaseMal>?,
    @SerializedName("Other")
    val other: List<BaseMal>?
)

class Aired (
    @SerializedName("from")
    private val f: String?,
    @SerializedName("to")
    private val t: String?
) {
    @SerializedName("___")
    var from: DateMAL? = null
        get() {
            if (field == null)
                field = DateMAL(f)
            return field
        }
    @SerializedName("___")
    var to: DateMAL? = null
        get() {
            if (field == null)
                field = DateMAL(t)
            return field
        }
}

class MalAnime (
    val producers: MutableList<BaseMal>?,
    val licensors: MutableList<BaseMal>?,
    @SerializedName("title_synonyms")
    val tSynonyms: MutableList<String>?,
    val studios: MutableList<BaseMal>?,
    @SerializedName("opening_themes")
    val openings: MutableList<String>?,
    @SerializedName("ending_themes")
    val endings: MutableList<String>?,
    val genres: MutableList<BaseMal>?,
    val background: String?,
    val premiered: String?,
    val broadcast: String?,
    @SerializedName("title_japanese")
    val tJapanese: String?,
    @SerializedName("title_english")
    val tEnglish: String?,
    val related: Related?,
    val episodes: String?,
    val duration: String?,
    val synopsis: String?,
    @SerializedName("trailer_url")
    val trailer: String?,
    val popularity: Int?,
    val airing: Boolean?,
    val favorites: Int?,
    val status: String?,
    val rating: String?,
    val source: String?,
    @SerializedName("scored_by")
    val scoredBy: Int?,
    val score: Double?,
    @SerializedName("image_url")
    val image: String?,
    val title: String?,
    val members: Int?,
    val aired: Aired?,
    val rank: String?,
    mal_id: String?,
    type: String?,
    url: String?
): BaseMal(mal_id, type, title, url)

class MalAnimeCharStaff (
    val characters: MutableList<MalAnimeCharacter>?,
    val staff: MutableList<MalAnimeStaff>?
)

open class BaseMalAnimePerson (
    @SerializedName("image_url")
    val image: String?,
    mal_id: String?,
    name: String?,
    url: String?
): BaseMal(mal_id, null, name, url)

class MalAnimeStaff (
    val positions: MutableList<String>?,
    image_url: String?,
    mal_id: String?,
    name: String?,
    url: String?
): BaseMalAnimePerson(image_url, mal_id, name, url)

class MalAnimeCharacter (
    @SerializedName("voice_actors")
    val voiceActors: MutableList<MalAnimeVoiceActors>?,
    image_url: String?,
    val role: String?,
    mal_id: String?,
    name: String?,
    url: String?
): BaseMalAnimePerson (image_url, mal_id, name, url)

class MalAnimeVoiceActors (
    val language: String?,
    image_url: String?,
    mal_id: String?,
    name: String?,
    url: String?
): BaseMalAnimePerson(image_url, mal_id, name, url)

class MalAnimeEpisode (
    @SerializedName("aired")
    private val dateTime: String?,
    @SerializedName("title_japanese")
    val tJapanese: String?,
    @SerializedName("title_romanji")
    val tRomanji: String?,
    @SerializedName("video_url")
    val videoUrl: String?,
    @SerializedName("forum_url")
    val forumUrl: String?,
    val filler: Boolean?,
    @SerializedName("episode_id")
    val episodeId: Int?,
    val recap: Boolean?,
    val title: String?
) {
    @SerializedName("___")
    var aired: DateMAL? = null
        get() {
            if (field == null)
                field = DateMAL(dateTime)
            return field
        }
}

class MalAnimeEpisodes (
    val episodes: MutableList<MalAnimeEpisode>?,
    val episodes_last_page: Int?
)

class MalAnimeNews (
    val articles: MutableList<MalAnimeNewsArticle>?
)

class MalAnimeNewsArticle (
    @SerializedName("date")
    private val dateTime: String?,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("author_url")
    val authorUrl: String?,
    @SerializedName("forum_url")
    val forumUrl: String?,
    @SerializedName("image_url")
    val image: String?,
    val comments: Int?,
    val intro: String?,
    val title: String?,
    val url: String?
) {
    @SerializedName("___")
    var date: DateMAL? = null
        get() {
            if (field == null)
                field = DateMAL(dateTime)
            return field
        }
}

class MalAnimePictures (
    val pictures: MutableList<MalAnimeImages>?
)

class MalAnimeImages (
    val large: String?,
    val small: String?
)

class MalAnimeVideo (
    val episode: String?,
    @SerializedName("image_url")
    val image: String?,
    val title: String?,
    val url: String?
)

class MalAnimeVideos (
    val episodes: MutableList<MalAnimeVideo>?,
    val promo: MutableList<MalAnimeVideo>?
)

class MalAnimeStats (
    val scores: MutableList<MalAnimeStatsScore>?,
    val completed: Int?,
    val watching: Int?,
    val dropped: Int?,
    val onHold: Int?,
    val total: Int?,
    val ptw: Int?
)

class MalAnimeStatsScore (
    val percentage: Int?,
    val votes: Int?
)

class MalAnimeForum (
    val topics: MutableList<MalAnimeForumTopic>?
)

class MalAnimeForumTopic (
    val lastPost: MalAnimeForumTopicLastPost,
    private val date_posted: String?,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("author_url")
    val authorUrl: String?,
    @SerializedName("topic_id")
    val topicId: String?,
    val title: String?,
    val replies: Int?,
    val url: String?
) {
    @SerializedName("__")
    var postedOn: DateMAL? = null
        get() {
            if (field == null)
                field = DateMAL(date_posted)
            return field
        }
}

class MalAnimeForumTopicLastPost (
    private val date_posted: String?,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("author_url")
    val authorUrl: String?,
    val url: String?
) {
    @SerializedName("__")
    var postedOn: DateMAL? = null
        get() {
            if (field == null)
                field = DateMAL(date_posted)
            return field
        }
}