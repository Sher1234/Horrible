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
import info.horriblesubs.sher.data.database.toNotificationItem
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
        val item = msg.data.toNotificationItem()
        if (item.id.isBlank() or item.id.equals("null", true)) return
        isNotified(item) {
            if (!this@isNotified) {
                when (NotificationPreference.value) {
                    0 -> inLibrary(item.link ?: "NULL") {
                        if (this)
                            onNotifyRelease(item)
                    }
                    100 -> onNotifyRelease(item)
                }
            }
        }
    }

    private fun onNotifyRelease(item: NotificationItem) {
        item.release = when {
            item.release?.contains("-") == true -> "Batch ${item.release} is now available..."
            item.release.isNullOrBlank() -> "New release available..."
            else -> "Episode ${item.release} is now available..."
        }

        val intent = ShowActivity.getShowActivityIntent(applicationContext, item.link ?: "")
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
                    setContentTitle(item.title.parseAsHtml)
                    setSmallIcon(R.drawable.ic_new)
                    setContentText(item.release)
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