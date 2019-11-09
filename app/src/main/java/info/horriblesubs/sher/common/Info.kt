package info.horriblesubs.sher.common

import android.text.Spanned
import info.horriblesubs.sher.ui.main.settings.KeySettings

enum class Info {
    TRENDING, RECENT, SHOWS, SCHEDULE, BOOKMARKS;
    val title = "Information".fromHtml()
    fun info(time: String, isPassed: Boolean = Constants.value(KeySettings.TimeLeft) as Boolean): Spanned {
        val t = if (isPassed) "" else "on "
        val  s = when(this) {
            SCHEDULE -> "<b>Weekly Schedule</b> fetches the release schedule of shows from <i>HorribleSubs</i> & displays them to user in an interactive way. The release time of the shows are displayed device default time zone & also supports displaying time in airing next show.<br><br><i><b>Note:</b><br>1. This schedule is only a guideline as release times are subject to change.<br>2. To prevent DDoS there's a network-caching enabled in the API [Cache expiry time 24 hours].</i><br>"
            TRENDING -> "<b>Trending this season</b> features top viewed shows which are <i>currently airing (ongoing)</i>. These results might be different from actual show's popularity. This list isn't based on MyAnimeList or any other anime site's database. And the view counter to generate this list is stored in a separate database.<br><br><b>#AppExclusive</b>"
            RECENT -> "<b>Recent Releases</b> fetches the latest releases available on <i>HorribleSubs</i> website. It can access more than 40+ recent releases (Limited to 10 releases but can be toggled in the info tag).<br><br><i><b>Note:</b> To prevent DDoS there's a network-caching enabled in the API [Cache expiry time 30 minutes].</i><br>"
            SHOWS -> "<b>Shows (All Shows/ Current Season)</b> fetches the shows list (both) available on <i>HorribleSubs</i> website. It features more detailed list of shows cached in the database.<br><br><i><b>Note:</b> To prevent DDoS there's a network-caching enabled in the API [Cache expiry time 72 hours]</i><br>"
            BOOKMARKS -> "<b>Bookmarks</b> lists the anime/shows that you bookmark. These bookmarks are stored locally on device and provide handy access to bookmarked shows faster.<br><br><b>#AppExclusive</b>"
        } + when(this) {
            SHOWS -> "<br>The cache for Shows was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://horriblesubs.info/shows/\"><b><i>All Shows</i></b></a> OR <a href=\"https://horriblesubs.info/current-season/\"><b><i>Current Season</i></b></a>."
            SCHEDULE -> "<br>The cache for Weekly Schedule was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://horriblesubs.info/release-schedule/\"><b><i>Release Schedule</i></b></a>."
            RECENT -> "<br>The cache for Recent Releases was last updated $t<i>$time</i>.<br><br>Click on the link to view it in browser. <a href=\"https://horriblesubs.info/\"><b><i>HorribleSubs</i></b></a>."
            BOOKMARKS -> ""
            TRENDING -> ""
        }
        return  s.fromHtml()
    }
}