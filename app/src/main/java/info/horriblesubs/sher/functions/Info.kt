package info.horriblesubs.sher.functions

import android.text.Spanned
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference

object Info {
    val title = "Information".parseAsHtml
    const val BOOKMARKS = 8246
    const val SCHEDULE = 8943
    const val TRENDING = 4881
    const val LATEST = 8733
    const val SHOWS = 3481
    fun getInfo(
        type: Int,
        time: String = ""
    ): Spanned {
        val t = if (time.isBlank()) "" else if (TimeLeftPreference.value) "" else "on "
        return when(type) {
            TRENDING -> "<b>Trending this season</b> features top viewed shows which are <i>currently airing (ongoing)</i>. These results might be different from actual show's popularity. This list isn't based on MyAnimeList or any other anime site's database. And the view counter to generate this list is stored in a separate database.<br><br><b>#AppExclusive</b>"
            SCHEDULE -> "<b>Weekly Schedule</b> fetches the release schedule of shows from <i>HorribleSubs</i> & displays them to user in an interactive way. The release time of the shows are displayed device default time zone & also supports displaying time in airing next show.<br><br><i><b>Note:</b><br>1. This schedule is only a guideline as release times are subject to change.<br>2. To prevent DDoS there's a network-caching enabled in the API [Cache expiry time 24 hours].</i><br>" +
                    "<br>The cache for Weekly Schedule was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://horriblesubs.info/release-schedule/\"><b><i>Release Schedule</i></b></a>."
            LATEST -> "<b>Recent Releases</b> fetches the latest releases available on <i>HorribleSubs</i> website. It can access more than 40+ recent releases (Limited to 10 releases but can be toggled in the info tag).<br><br><i><b>Note:</b> To prevent DDoS there's a network-caching enabled in the API [Cache expiry time 30 minutes].</i><br>" +
                    "<br>The cache for Recent Releases was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://horriblesubs.info/\"><b><i>HorribleSubs</i></b></a>."
            SHOWS -> "<b>Shows (All Shows/ Current Season)</b> fetches the shows list (both) available on <i>HorribleSubs</i> website. It features more detailed list of shows cached in the database.<br><br><i><b>Note:</b> To prevent DDoS there's a network-caching enabled in the API [Cache expiry time 72 hours]</i><br>" +
                    "<br>The cache for Shows was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://horriblesubs.info/shows/\"><b><i>All Shows</i></b></a> OR <a href=\"https://horriblesubs.info/current-season/\"><b><i>Current Season</i></b></a>."
            BOOKMARKS -> "<b>Bookmarks</b> lists the anime/shows that you bookmark. These bookmarks are stored locally on device and provide handy access to bookmarked shows faster.<br><br><b>#AppExclusive</b>"
            else -> "<b>an!me - better ongoing</b> is a free to access android app used for maintaining watch list and get notified when new releases are available on <a href=\"https://horriblesubs.info/\"><i>HorribleSubs</i></a> using Unofficial (in-house) API. It also includes support for accessing <a href=\"https://myanimelist.net/\"><i>MyAnimeList DB</i></a> using Unofficial Jikan API.<br>To prevent DDoS there's a network-caching enabled in both the APIs.<br>"
        }.parseAsHtml
    }
}