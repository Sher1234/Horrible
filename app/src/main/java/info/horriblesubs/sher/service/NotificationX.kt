package info.horriblesubs.sher.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.db.DataAccess
import info.horriblesubs.sher.ui.main.settings.KeySettings

class NotificationX : FirebaseMessagingService() {

    private val b = Constants.value(KeySettings.FavNotifications) as Boolean

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Constants.preferences.edit().putString(KeySettings.Token.key, s).apply()
        //TODO: {FirebaseInstanceId.getInstance().instanceId} //Reset Notification Id in Settings
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (isNotNotified(message.data)) {
            if ((b && DataAccess.bookmarked(message.data["link"])) ||  !b)
                notify(message.data)
        }
    }

    private fun isNotNotified(map: Map<String?, String?>): Boolean {
        if (DataAccess.notified(map["id"])) return false
        DataAccess.notify(map)
        return true
    }

    private fun notify(map: Map<String, String>) {
        val release = map["release"] ?:""
        val s = when {
            release.isEmpty() -> "New release available."
            release.contains("-") -> "Batch of $release is now available..."
            else -> "Episode $release is now available..."
        }
        Constants.notify(title = map["title"]?:"New release", desc = s, link = map["link"])
    }
}