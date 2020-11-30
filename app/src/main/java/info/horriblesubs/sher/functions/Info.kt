package info.horriblesubs.sher.functions

import android.text.Spanned
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference

object Info {
    val title = "Information".parseAsHtml
    const val BOOKMARKS = 8246
    const val SCHEDULE = 8943
    const val LATEST = 8733
    const val SHOWS = 3481
    const val TODAY = 4881
    fun getInfo(
        type: Int,
        time: String = ""
    ): Spanned {
        val t = if (time.isBlank()) "" else if (TimeLeftPreference.value) "" else "on "
        return when(type) {
            TODAY -> "<b>Today's Schedule</b> shows the list of shows to be aired today. The release time of the shows are displayed device default time zone & also supports displaying time in airing next show.<br><br><i><b>Note:</b><br>1. This schedule is only a guideline as release times are subject to change.<br>2. To prevent DDoS there's a network-caching enabled in the API [15 minutes].</i><br>" +
                    "<br>The local cache for Today's Schedule was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://subsplease.org/schedule/\"><b><i>Schedule</i></b></a>."
            SCHEDULE -> "<b>Weekly Schedule</b> fetches the release schedule of shows from <i>SubsPlease</i> & displays them to user in an interactive way. The release time of the shows are displayed device default time zone & also supports displaying time in airing next show.<br><br><i><b>Note:</b><br>1. This schedule is only a guideline as release times are subject to change.<br>2. To prevent DDoS there's a network-caching enabled in the API [1 day].</i><br>" +
                    "<br>The cache for Weekly Schedule was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://subsplease.org/schedule/\"><b><i>Schedule</i></b></a>."
            LATEST -> "<b>Recent Releases</b> fetches the latest releases available on <i>HorribleSubs</i> website. It can access more than 40+ recent releases (Limited to 10 releases but can be toggled in the info tag).<br><br><i><b>Note:</b> To prevent DDoS there's a network-caching enabled in the API [30 minutes].</i><br>" +
                    "<br>The cache for Recent Releases was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://subsplease.org/\"><b><i>SubsPlease</i></b></a>."
            SHOWS -> "<b>Shows</b> fetches the shows list of shows available on <i>SubsPlease</i> website.<br><br><i><b>Note:</b> To prevent DDoS there's a network-caching enabled in the API [3 days]</i><br>" +
                    "<br>The cache for Shows was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://subsplease.org/shows/\"><b><i>Shows</i></b></a>."
            BOOKMARKS -> "<b>Library</b> lists the shows that are bookmarked by you. This list is stored locally on device and provide handy access to bookmarked shows faster.<br><br><b>#AppExclusive</b>"
            else -> "<b>an!me - better ongoing</b> is a free to access android app used for maintaining watch list and get notified when new releases are available on <a href=\"https://subsplease.org/\"><i>SubsPlease</i></a> using Unofficial API. It also includes support for accessing <a href=\"https://myanimelist.net/\"><i>MyAnimeList</i></a> using Unofficial Jikan API.<br>To prevent DDoS there's a network-caching enabled in both the APIs.<br>"
        }.parseAsHtml
    }
}