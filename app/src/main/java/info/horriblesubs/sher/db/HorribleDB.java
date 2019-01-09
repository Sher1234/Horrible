package info.horriblesubs.sher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import info.horriblesubs.sher.api.horrible.model.Item;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.response.ShowsItems;

public class HorribleDB implements Database.Horrible {

    private final Context context;

    public HorribleDB(Context context) {
        this.context = context;
    }

    private void onCacheShows(@NotNull SQLiteDatabase db, @NotNull List<Item> items, String table) {
        db.delete(table, null, null);
        for (Item item : items) {
            ContentValues values = new ContentValues();
            values.put(Title, item.title);
            values.put(Link, item.link);
            values.put(ID, item.id);
            db.insert(table, null, values);
        }
    }

    public List<Item> getCurrentCachedShows() {
        return getCachedShows(H_Current);
    }

    public List<Item> getAllCachedShows() {
        return getCachedShows(H_All);
    }

    public void onCacheSchedule(@NotNull List<ScheduleItem> items) {
        SQLiteDatabase db = new Database(context).getWritableDatabase();
        db.delete(H_Schedule, null, null);
        for (ScheduleItem item : items) {
            ContentValues values = new ContentValues();
            values.put(ID, item.id);
            values.put(Link, item.link);
            values.put(S_Time, item.time);
            values.put(Title, item.title);
            values.put(S_Scheduled, item.scheduled ? 1 : 0);
            db.insert(H_Schedule, null, values);
        }
        ContentValues values = new ContentValues();
        values.put("Time", Calendar.getInstance().getTime().getTime());
        db.update(H_Updates, values, "Item = ?", new String[]{"Schedule"});
    }

    public void onCacheReleases(List<ListItem> items) {
        SQLiteDatabase database = new Database(context).getWritableDatabase();
        database.delete(H_Latest, null, null);
        database.delete(H_Notify, null, null);
        for (ListItem item : items) {
            ContentValues values = new ContentValues();
            values.put(ID, item.id);
            values.put(Link, item.link);
            values.put(Title, item.title);
            values.put(L_Release, item.release);
            database.insert(H_Notify, null, values);
            if (item.quality.get(0)) values.put(L_SD, 1);
            if (item.quality.get(1)) values.put(L_HD, 1);
            if (item.quality.get(2)) values.put(L_FHD, 1);
            database.insert(H_Latest, null, values);
        }
        ContentValues values = new ContentValues();
        values.put("Time", Calendar.getInstance().getTime().getTime());
        database.update(H_Updates, values, "Item = ?", new String[]{"Releases"});
        database.close();
    }

    private List<Item> getCachedShows(String table) {
        SQLiteDatabase database = new Database(context).getReadableDatabase();
        String[] projection = {ID, Link, Title};
        Cursor cursor = database.query(table, projection, null, null,
                null, null, null);
        int[] a = new int[]{cursor.getColumnIndex(ID), cursor.getColumnIndex(Title), cursor.getColumnIndex(Link)};
        List<Item> items = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Item item = new Item();
            item.title = cursor.getString(a[1]);
            item.link = cursor.getString(a[2]);
            item.id = cursor.getString(a[0]);
            items.add(item);
        }
        cursor.close();
        database.close();
        return items;
    }

    public void addToFavourites(ShowDetail detail) {
        try (SQLiteDatabase db = new Database(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ID, detail.id);
            values.put(Link, detail.link);
            values.put(Title, detail.title);
            values.put(F_ShowID, detail.sid);
            values.put(F_Image, detail.image);
            db.insert(H_Favourites, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
    }

    public long onNotify(Map<String, String> map) {
        try (SQLiteDatabase db = new Database(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(L_Release, map.get("release"));
            values.put(Title, map.get("title"));
            values.put(Link, map.get("link"));
            values.put(ID, map.get("id"));
            return db.insert(H_Favourites, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public List<ScheduleItem> getCachedSchedule() {
        SQLiteDatabase database = new Database(context).getReadableDatabase();
        String[] projection = {ID, Link, Title, S_Time, S_Scheduled};
        Cursor cursor = database.query(H_Schedule, projection, null, null,
                null, null, null);
        int[] a = new int[]{cursor.getColumnIndex(ID),
                cursor.getColumnIndex(S_Scheduled),
                cursor.getColumnIndex(Title),
                cursor.getColumnIndex(S_Time),
                cursor.getColumnIndex(Link)};
        List<ScheduleItem> items = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ScheduleItem item = new ScheduleItem();
            item.scheduled = cursor.getInt(a[1]) == 1;
            item.title = cursor.getString(a[2]);
            item.time = cursor.getString(a[3]);
            item.link = cursor.getString(a[4]);
            item.id = cursor.getString(a[0]);
            items.add(item);
        }
        cursor.close();
        database.close();
        return items;
    }

    public void removeFromFavourites(String sid) {
        SQLiteDatabase database = new Database(context).getWritableDatabase();
        database.delete(H_Favourites, ID + " = ?", new String[]{sid});
        database.close();
    }

    public List<ShowDetail> getAllFavourites() {
        SQLiteDatabase database = new Database(context).getReadableDatabase();
        String[] projection = {F_ShowID, Title, F_Image, Link, ID};
        Cursor cursor = database.query(H_Favourites, projection, null, null,
                null, null, null);
        int b = cursor.getColumnIndex(F_ShowID);
        int d = cursor.getColumnIndex(F_Image);
        int c = cursor.getColumnIndex(Title);
        int e = cursor.getColumnIndex(Link);
        int a = cursor.getColumnIndex(ID);
        List<ShowDetail> showDetails = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ShowDetail showDetail = new ShowDetail();
            showDetail.image = cursor.getString(d);
            showDetail.title = cursor.getString(c);
            showDetail.link = cursor.getString(e);
            showDetail.sid = cursor.getString(b);
            showDetail.id = cursor.getString(a);
            showDetails.add(showDetail);
        }
        cursor.close();
        database.close();
        return showDetails;
    }

    public void onCacheShows(ShowsItems items) {
        SQLiteDatabase database = new Database(context).getWritableDatabase();
        onCacheShows(database, items.current, H_Current);
        onCacheShows(database, items.all, H_All);
        ContentValues values = new ContentValues();
        values.put("Time", Calendar.getInstance().getTime().getTime());
        database.update(H_Updates, values, "Item = ?", new String[]{"Shows"});
        database.close();
    }

    public List<ListItem> getCachedReleases() {
        String[] projection = {ID, Link, Title, L_Release, L_SD, L_HD, L_FHD};
        SQLiteDatabase database = new Database(context).getReadableDatabase();
        Cursor cursor = database.query(H_Latest, projection, null,
                null, null, null, null);
        int[] a = new int[]{cursor.getColumnIndex(ID), cursor.getColumnIndex(L_Release),
                cursor.getColumnIndex(Title), cursor.getColumnIndex(Link),
                cursor.getColumnIndex(L_FHD), cursor.getColumnIndex(L_HD),
                cursor.getColumnIndex(L_SD)};
        List<ListItem> listItems = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ListItem listItem = new ListItem();
            List<Boolean> quality = new ArrayList<>();
            listItem.id = cursor.getString(a[0]);
            listItem.link = cursor.getString(a[3]);
            listItem.title = cursor.getString(a[2]);
            listItem.release = cursor.getString(a[1]);
            quality.add(0, cursor.getInt(a[6]) == 1);
            quality.add(1, cursor.getInt(a[5]) == 1);
            quality.add(2, cursor.getInt(a[4]) == 1);
            listItem.quality = quality;
            listItems.add(listItem);
        }
        cursor.close();
        database.close();
        return listItems;
    }

    public boolean isFavourite(String id) {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_Favourites, ID + " = ?", new String[]{id}) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNotified(String id) {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_Notify, ID + " = ?", new String[]{id}) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isScheduleExists() {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_Schedule) != 0;
        }
    }

    public Long getTime(String table) {
        try {
            SQLiteDatabase database = new Database(context).getReadableDatabase();
            Cursor cursor = database.query(H_Updates, new String[]{"Time"},
                    "Item = ?", new String[]{table}, null, null, null);
            cursor.moveToFirst();
            long l = Long.parseLong(cursor.getString(cursor.getColumnIndex("Time")));
            cursor.close();
            database.close();
            return (new Date()).getTime() - l;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public boolean isCurrentExists() {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_Current) != 0;
        }
    }

    public boolean isLatestExists() {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_Latest) != 0;
        }
    }

    public boolean isAllExists() {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_All) != 0;
        }
    }

    public boolean isFavExists() {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, H_Favourites) != 0;
        }
    }
}