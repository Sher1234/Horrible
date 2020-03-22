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
    if (this == null) return "?"
    val centuries = CENTURIES.between(this, now)
    var minutes = MINUTES.between(this, now)
    var seconds = SECONDS.between(this, now)
    var months = MONTHS.between(this, now)
    var weeks = WEEKS.between(this, now)
    var hours = HOURS.between(this, now)
    var years = YEARS.between(this, now)
    var days = DAYS.between(this, now)
    seconds -= (minutes*60)
    minutes -= (hours*60)
    hours -= (days*24)
    days -= (weeks*7)
    weeks -= (months*4.348214).toLong()
    months -= (years*12)
    years -= (centuries*100)
    var str = ""
    str += if (centuries > 0) centuries+"C " else ""
    str += if (years > 0) years+"Y " else ""
    str += if (months > 0) months+"M " else ""
    str += if (weeks > 0) weeks+"w " else ""
    str += if (days > 0) days+"d " else ""
    str += if (hours > 0) hours+"h " else ""
    str += if (minutes > 0) minutes+"m " else ""
    str += if (seconds > 0) seconds+"s " else ""
    return when {
        centuries > 0 || years > 0 || months > 0 || weeks > 0 || days > 0 || hours > 0 || minutes > 0 || seconds > 0 -> str+"ago"
        else -> return "Just Now"
    }
}

private operator fun Long.plus(s: String): String {
    return "$this$s"
}
fun ZonedDateTime?.timeLeft(now: ZonedDateTime): String {
    if(this == null) return "?"
    var minutes = MINUTES.between(now, this)
    var seconds = SECONDS.between(now, this)
    var months = MONTHS.between(now, this)
    var hours = HOURS.between(now, this)
    val years = YEARS.between(now, this)
    var weeks = WEEKS.between(now, this)
    var days = DAYS.between(now, this)
    seconds -= (minutes*60)
    minutes -= (hours*60)
    hours -= (days*24)
    days -= (weeks*7)
    weeks -= (months*4.348214).toLong()
    months -= (years*12)
    var str = "in"
    str += if (years > 0) " $years Y" else ""
    str += if (months > 0) " $months M" else ""
    str += if (weeks > 0) " $weeks w" else ""
    str += if (days > 0) " $days d" else ""
    str += if (hours > 0) " $hours h" else ""
    str += if (minutes > 0) " $minutes m" else ""
    return when {
        years > 0 || months > 0 || weeks > 0 || days > 0 || hours > 0 || minutes > 0 || seconds > 0  -> str
        minutes <= 0 -> "Aired"
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