package info.horriblesubs.sher.common

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import info.horriblesubs.sher.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.*

fun ZonedDateTime?.timePassed(now: ZonedDateTime): String {
    if (this == null) return "Never"
    var m = MINUTES.between(this, now)
    var h = HOURS.between(this, now)
    val d = DAYS.between(this, now)
    m -= ((d*24*60) + (h*60))
    h -= (d*24)
    var s = ""
    s += if (d > 0) d+"d " else ""
    s += if (h > 0) h+"h " else ""
    s += if (m > 0) m+"m " else ""
    return when {
        d > 0 || h > 0 || m > 0 -> s+"ago"
        else -> return "Just Now"
    }
}

private operator fun Long.plus(s: String): String {
    return "$this$s"
}
fun ZonedDateTime?.timeLeft(now: ZonedDateTime): String {
    if(this == null) return "Aired | Error"
    var m = MINUTES.between(now, this)
    var h = HOURS.between(now, this)
    val d = DAYS.between(now, this)
    m -= ((d*24*60) + (h*60))
    h -= (d*24)
    var s = "in"
    s += if (d > 0) " $d d" else ""
    s += if (h > 0) " $h h" else ""
    s += if (m > 0) " $m m" else ""
    return when {
        d > 0 || h > 0 || m > 0 -> s
        m <= 0 -> "Aired"
        else -> return "Aired | Error"
    }
}
fun String.toZonedDateTime(formats: Formats = Formats.SERVER_FORMAT): ZonedDateTime? {
    val dateTime = LocalDateTime.parse(this, formats.formatter)
    return ZonedDateTime.of(dateTime, ZoneId.of("+0000")).withZoneSameInstant(ZoneId.systemDefault())
} //Only "yyyy-MM-d HH:mm:ss" Otherwise Throws Exception
fun AppCompatImageView.load(s: String?, isAsset: Boolean = false) {
    val u = if(isAsset) "file:///android_asset/$s" else if(s?.contains("show_placeholder.jpg", true) == true) null else s
    val glide = Glide.with(this).load(u).placeholder(R.drawable.app_placeholder)
    if(!isAsset) glide.apply(Constants.imageOptions)
    glide.into(this)
}
fun String?.fromHtml(): Spanned {
    return HtmlCompat.fromHtml(this?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
}
fun ViewGroup.inflate(int: Int, inflater: LayoutInflater = LayoutInflater.from(context)): View {
    return inflater.inflate(int, this, false)
}