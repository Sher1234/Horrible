package info.horriblesubs.sher.data.subsplease

import info.horriblesubs.sher.data.database.AppDatabase
import info.horriblesubs.sher.data.database.model.NotificationItem

@Suppress("Unused")
object NotificationRepository {
    private val spNotifications get() = AppDatabase.database.getSpNotifications()

    suspend fun isPresent(id: String) =
        if (id.isBlank()) false else spNotifications.countById(id).items == 1

    suspend fun getById(id: String) =
        if (id.isBlank()) null else spNotifications.getById(id)

    suspend fun insertAll(items: List<NotificationItem>) =
        spNotifications.insertAll(items)

    suspend fun getAll() = spNotifications.getAll()

    suspend fun insert(vararg items: NotificationItem) =
        spNotifications.insert(*items)

    suspend fun deleteAll() =
        spNotifications.deleteAll()
}