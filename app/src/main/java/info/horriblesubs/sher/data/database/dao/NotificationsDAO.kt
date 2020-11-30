package info.horriblesubs.sher.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import info.horriblesubs.sher.data.database.model.CountItem
import info.horriblesubs.sher.data.database.model.NotificationItem

@Dao
interface NotificationsDAO {
    @Query("SELECT COUNT(*) AS items FROM Notifications WHERE id = :id")
    suspend fun countById(id: String): CountItem

    @Query("SELECT * FROM Notifications WHERE page = :id LIMIT 1")
    suspend fun getById(id: String): NotificationItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<NotificationItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg items: NotificationItem)

    @Query("DELETE FROM Notifications")
    suspend fun deleteAll()

    @Query("SELECT * FROM Notifications")
    suspend fun getAll(): List<NotificationItem>
}