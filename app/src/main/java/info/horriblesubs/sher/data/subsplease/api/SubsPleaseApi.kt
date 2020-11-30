package info.horriblesubs.sher.data.subsplease.api

import info.horriblesubs.sher.data.subsplease.api.model.*
import org.jsoup.Jsoup
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.ZonedDateTime

@Suppress("Unused")
interface SubsPleaseApi {

    @GET("api/?f=latest")
    suspend fun getLatest(@Query(value = "tz") timezone: String): LinkedHashMap<String, ItemLatest>?

    @GET("api/?f=schedule&h=true")
    suspend fun getTodaySchedule(@Query(value = "tz") timezone: String): ItemTodaySchedule?

    @GET("api/?f=show")
    suspend fun getShowReleases(
        @Query(value = "tz") timezone: String,
        @Query(value = "sid") sid: String
    ): LinkedHashMap<String, ItemRelease>?

    @GET("api/?f=schedule")
    suspend fun getSchedule(@Query(value = "tz") timezone: String): ItemSchedule?

}

fun SubsPleaseApi.getShowDetail(url: String): ItemShow? = if (url.isBlank()) null else {
    val doc = Jsoup.connect("https://subsplease.org/shows/$url").get()
    val imageE = doc.selectFirst("img[class='img-responsive img-center']")
    val synopsisE = doc.selectFirst("div.series-syn")
    val synopsis = synopsisE.getElementsByTag("p").text()
    val titleE = doc.selectFirst("h1.entry-title")
    val sidE = doc.selectFirst("table[sid]")
    val image = imageE.attr("src")
    val sid = sidE.attr("sid")
    val title = titleE.text()
    if (title.isNullOrBlank() || sid.isNullOrBlank()) null else
        ItemShow(synopsis, image, title, sid, url)
}

fun SubsPleaseApi.getShows(): ArrayList<ItemList> {
    val shows = arrayListOf<ItemList>()
    val doc = Jsoup.connect("https://subsplease.org/shows/").get()
    val divs = doc.select("div.all-shows").select("a[title]")
    for (div in divs) {
        shows.add(
            ItemList(
                title = div.attr("title"),
                link = div.attr("href"),
            )
        )
    }
    return shows
}

val timezone get() = ZonedDateTime.now().zone.id ?: "Asia/Calcutta"