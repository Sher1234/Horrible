package info.horriblesubs.sher.db

import android.content.ContentValues
import android.database.DatabaseUtils.queryNumEntries
import android.database.sqlite.SQLiteConstraintException
import info.horriblesubs.sher.App
import info.horriblesubs.sher.api.horrible.Horrible.Companion.service
import info.horriblesubs.sher.api.horrible.model.ItemRecent
import info.horriblesubs.sher.api.horrible.model.ItemShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

object DataAccess {

    private val context get() = App.instance

    fun notify(map: Map<String?, String?>): Long {
        try {
            Database(context).writableDatabase.use { db ->
                val values = ContentValues()
                values.put(Horrible.Release, map["release"])
                values.put(Horrible.Title, map["title"])
                values.put(Horrible.Link, map["link"])
                values.put(Horrible.ID, map["id"])
                return db.insert(Horrible.Notify, null, values)
            }
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            return -1L
        }
    }

    fun resetNotify(list: MutableList<ItemRecent>?) {
        try {
            Database(context).writableDatabase.use {db ->
                db.delete(Horrible.Notify, null, null)
                list?.forEach {
                    val values = ContentValues()
                    values.put(Horrible.Release, it.release)
                    values.put(Horrible.Title, it.title)
                    values.put(Horrible.Link, it.link)
                    values.put(Horrible.ID, it.id)
                    db.insert(Horrible.Notify, null, values)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bookmarks(): List<ItemShow>? {
        val projection = arrayOf(Horrible.ShowID, Horrible.Title, Horrible.Image, Horrible.Link, Horrible.ID)
        try {
            Database(context).readableDatabase.use {it ->
                try {
                    it.query(Horrible.Favourites, projection, null, null,
                        null, null, null).use {
                        val shows: MutableList<ItemShow> = ArrayList()
                        val b = it.getColumnIndex(Horrible.ShowID)
                        val d = it.getColumnIndex(Horrible.Image)
                        val c = it.getColumnIndex(Horrible.Title)
                        val e = it.getColumnIndex(Horrible.Link)
                        val a = it.getColumnIndex(Horrible.ID)
                        it.moveToFirst()
                        while (!it.isAfterLast) {
                            val showDetail =
                                ItemShow(
                                    title = it.getString(c),
                                    image = it.getString(d),
                                    link = it.getString(e),
                                    sid = it.getString(b),
                                    id = it.getString(a),
                                    schedule = null,
                                    episodes = null,
                                    featured = null,
                                    batches = null,
                                    ongoing = null,
                                    mal_id = null,
                                    rating = null,
                                    users = null,
                                    views = null,
                                    body = null,
                                    time = null,
                                    favs = null
                                )
                            shows.add(showDetail)
                            it.moveToNext()
                        }
                        return shows
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun autoBookmark(t: ItemShow?) {
        if(bookmarked(t?.sid)) unMark(t?.sid)
        else bookmark(t)
    }

    fun bookmarked(link_sid: String?): Boolean {
        try {
            val s = link_sid?:"null"
            Database(context).readableDatabase.use {
                return queryNumEntries(it, Horrible.Favourites, Horrible.Link + " = ? OR "
                            + Horrible.ShowID + " = ?", arrayOf(s, s)) > 0L
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun notified(id: String?): Boolean {
        try {
            Database(context).readableDatabase.use{
                return queryNumEntries(it, Horrible.Notify, Horrible.ID + " = ?", arrayOf(id)) == 1L
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun bookmark(detail: ItemShow?) {
        if (detail == null || detail.title.isNullOrEmpty() || detail.sid.isNullOrEmpty()) return
        try {
            Database(context).writableDatabase.use {
                val values = ContentValues()
                values.put(Horrible.Title, detail.title)
                values.put(Horrible.Image, detail.image)
                values.put(Horrible.ShowID, detail.sid)
                values.put(Horrible.Link, detail.link)
                values.put(Horrible.ID, detail.id)
                it.insert(Horrible.Favourites, null, values)
                pingBookmark(detail.sid)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unMark(sid: String?) {
        try {Database(context).writableDatabase.use {
            it.delete(Horrible.Favourites, Horrible.ShowID + " = ?", arrayOf(sid?:"null"))
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pingBookmark(sid: String): Any? {
        return GlobalScope.launch(Dispatchers.IO) {service.bookmark(sid)}
    }
}