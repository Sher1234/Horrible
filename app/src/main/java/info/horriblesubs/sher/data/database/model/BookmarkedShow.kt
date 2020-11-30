package info.horriblesubs.sher.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bookmarks")

data class BookmarkedShow(
    @ColumnInfo(index = true) var url: String = "",
    @ColumnInfo var synopsis: String = "",
    @ColumnInfo var image: String = "",
    @ColumnInfo var title: String = "",
    @PrimaryKey var sid: String = "",
)