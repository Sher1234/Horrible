package info.horriblesubs.sher.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.messaging.FirebaseMessaging
import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.main.settings.KeySettings
import info.horriblesubs.sher.ui.show.Show
import java.util.*

object Constants {
    val imageOptions = RequestOptions().transform(FitCenter(), RoundedCorners(10))
    val preferences: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(App.instance)

    fun subscribe(b: Boolean = value(KeySettings.Notifications) as Boolean) {
        if (!b) FirebaseMessaging.getInstance().unsubscribeFromTopic("anime.ongoing")
        else FirebaseMessaging.getInstance().subscribeToTopic("anime.ongoing")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("hs.new.rls")
    }

    fun theme(theme: String? = value(KeySettings.Theme).toString()) {
        when (theme) {
            "dark" -> setDefaultNightMode(MODE_NIGHT_YES)
            "light" -> setDefaultNightMode(MODE_NIGHT_NO)
            else -> setDefaultNightMode(if (SDK_INT >= 29) MODE_NIGHT_FOLLOW_SYSTEM else MODE_NIGHT_AUTO_BATTERY)
        }
    }

    fun value(key: KeySettings): Any {
        return when (key.defaultValue) {
            is Boolean -> preferences.getBoolean(key.key, key.defaultValue)
            is String -> preferences.getString(key.key, key.defaultValue) ?:key.defaultValue
            else -> false
        }
    }

    fun notify(title: String, desc: String = "New release available...", link: String? = null,
               channel: String = "Ongoing Notifications") {
        val intent = Intent(App.instance, Show::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("link", link)
        val code = Random().nextInt(99999)
        val builder = NotificationCompat.Builder(App.instance, "An!me")
            .setContentIntent(getActivity(App.instance, code, intent, FLAG_ONE_SHOT))
            .setSmallIcon(R.drawable.ic_new_releases)
            .setContentTitle(title.fromHtml())
            .setContentText(desc)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(App.instance)) {
            if (SDK_INT >= Build.VERSION_CODES.O)
                createNotificationChannel(
                    NotificationChannel("An!me", channel,
                        NotificationManager.IMPORTANCE_DEFAULT)
                )
            notify(code, builder.build())
        }
    }
}