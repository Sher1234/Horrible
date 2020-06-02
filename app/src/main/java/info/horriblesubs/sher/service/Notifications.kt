package info.horriblesubs.sher.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

@Suppress("MemberVisibilityCanBePrivate")
object Notifications {

    const val CHANNEL_COMMON = "common_channel"
    const val GROUP_NEW_RELEASES: String = "new_releases"
    const val CHANNEL_NEW_RELEASE = "new_chapters_channel"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channels = listOf(
            NotificationChannel(
                CHANNEL_COMMON,
                "Common",
                NotificationManager.IMPORTANCE_LOW
            ),
            NotificationChannel(
                CHANNEL_NEW_RELEASE,
                "New releases",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
            ?.createNotificationChannels(channels)
    }
}
