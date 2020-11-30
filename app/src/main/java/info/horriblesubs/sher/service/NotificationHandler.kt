package info.horriblesubs.sher.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.inLibrary
import info.horriblesubs.sher.data.database.isNotified
import info.horriblesubs.sher.data.database.model.NotificationItem
import info.horriblesubs.sher.data.database.toNotificationItems2
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.libs.preference.prefs.NotificationPreference
import info.horriblesubs.sher.libs.preference.prefs.TokenPreference
import info.horriblesubs.sher.ui.c.ShowActivity
import kotlin.random.Random

class NotificationHandler: FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        TokenPreference.value = s
    }

    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        val items = msg.data.toNotificationItems2()
        for (it in items) {
            if (it.id.isBlank() or it.id.equals("null", true)) return
            isNotified(it) {
                if (!this@isNotified) {
                    when (NotificationPreference.value) {
                        0 -> inLibrary(it.page) {
                            if (this)
                                onNotifyRelease(it)
                        }
                        100 -> onNotifyRelease(it)
                    }
                }
            }
        }
    }

    private fun onNotifyRelease(item: NotificationItem) {
        item.episode = when {
            item.episode.contains("-") -> "Batch ${item.episode} is now available..."
            item.episode.isBlank() -> "New release available..."
            else -> "Episode ${item.episode} is now available..."
        }

        val intent = ShowActivity.getShowActivityIntent(applicationContext, item.page)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val notificationCode = Random.nextInt(99999)

        NotificationManagerCompat.from(applicationContext).apply {
            notify(
                notificationCode,
                application.notification(Notifications.CHANNEL_NEW_RELEASE) {
                    setContentIntent(PendingIntent.getActivity(applicationContext,
                        notificationCode,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT))
                    setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                    priority = NotificationCompat.PRIORITY_HIGH
                    setGroup(Notifications.GROUP_NEW_RELEASES)
                    setContentTitle(item.show.parseAsHtml)
                    setSmallIcon(R.drawable.ic_new)
                    setContentText(item.episode)
                    setGroupSummary(true)
                    setAutoCancel(true)
                }
            )
        }
    }

    private inline fun Context.notification(
        channelId: String,
        func: NotificationCompat.Builder.() -> Unit
    ): Notification {
        val builder = NotificationCompat.Builder(this, channelId)
        builder.func()
        return builder.build()
    }
}