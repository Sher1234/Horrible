package info.horriblesubs.sher.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notifications")
data class NotificationItem(
    @ColumnInfo(index = true) var page: String = "",
    @ColumnInfo var image_url: String = "",
    @ColumnInfo var episode: String = "",
    @ColumnInfo var show: String = "",
    @PrimaryKey var id: String = "",
)