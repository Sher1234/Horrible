package info.horriblesubs.sher.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bookmarks")
data class BookmarkedShow(
    @ColumnInfo(index = true) var link: String = "",
    @ColumnInfo(index = true) var id: String = "",
    @ColumnInfo var image: String? = null,
    @ColumnInfo var title: String = "",
    @ColumnInfo var body: String = "",
    @PrimaryKey var sid: String = ""
) {
    val uid get() = if (sid.isBlank()) if (link.isBlank()) "" else link else sid
}