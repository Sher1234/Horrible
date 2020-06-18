package info.horriblesubs.sher.data.horrible

import info.horriblesubs.sher.data.database.AppDatabase
import info.horriblesubs.sher.data.database.model.NotificationItem

@Suppress("Unused")
object NotificationRepository {
    private val horribleNotifications get() = AppDatabase.database.getHorribleNotifications()

    suspend fun isPresent(id: String) =
        if (id.isBlank()) false else horribleNotifications.countById(id).items == 1

    suspend fun getById(id: String) =
        if (id.isBlank()) null else horribleNotifications.getById(id)

    suspend fun insertAll(items: List<NotificationItem>) =
        horribleNotifications.insertAll(items)

    suspend fun getAll() = horribleNotifications.getAll()

    suspend fun insert(vararg items: NotificationItem) =
        horribleNotifications.insert(*items)

    suspend fun deleteAll() =
        horribleNotifications.deleteAll()
}