package info.horriblesubs.sher.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notifications")
data class NotificationItem(
    @ColumnInfo(index = true) var link: String?,
    @ColumnInfo var release: String?,
    @PrimaryKey var id: String = "",
    @ColumnInfo var title: String?
)