package info.horriblesubs.sher.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import retrofit2.Retrofit;

public class DataMethods implements Database.Horrible {

    private final Context context;

    public DataMethods(Context context) {
        this.context = context;
    }

    public void onResetNotifications(List<ListItem> items) {
        SQLiteDatabase database = new Database(context).getWritableDatabase();
        database.delete(Notify, null, null);
        for (ListItem item : items) {
            ContentValues values = new ContentValues();
            values.put(Release, item.release);
            values.put(Title, item.title);
            values.put(Link, item.link);
            values.put(ID, item.id);
            database.insert(Notify, null, values);
        }
        database.close();
    }

    public void onAddToFavourite(ShowDetail detail) {
        try (SQLiteDatabase db = new Database(context).getWritableDatabase()) {
            Log.d(Favourites, "Add To" + Favourites + " + " + detail.sid);
            ContentValues values = new ContentValues();
            values.put(Title, detail.title);
            values.put(Image, detail.image);
            values.put(ShowID, detail.sid);
            values.put(Link, detail.link);
            values.put(ID, detail.id);
            db.insert(Favourites, null, values);
            new AddFav().execute(detail.sid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long onNotify(Map<String, String> map) {
        try (SQLiteDatabase db = new Database(context).getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(Release, map.get("release"));
            values.put(Title, map.get("title"));
            values.put(Link, map.get("link"));
            values.put(ID, map.get("id"));
            return db.insert(Notify, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public void onDeleteFavourite(String sid) {
        SQLiteDatabase database = new Database(context).getWritableDatabase();
        database.delete(Favourites, ShowID + " = ?", new String[]{sid});
        database.close();
    }

    public List<ShowDetail> onGetFavourites() {
        SQLiteDatabase database = new Database(context).getReadableDatabase();
        String[] projection = {ShowID, Title, Image, Link, ID};
        Cursor cursor = database.query(Favourites, projection, null, null,
                null, null, null);
        int b = cursor.getColumnIndex(ShowID);
        int d = cursor.getColumnIndex(Image);
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

    public boolean isFavourite(String id) {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, Favourites, ShowID + " = ?", new String[]{id}) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNotified(String id) {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, Notify, ID + " = ?", new String[]{id}) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFavExists() {
        try (SQLiteDatabase database = new Database(context).getReadableDatabase()) {
            return DatabaseUtils.queryNumEntries(database, Favourites) != 0;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddFav extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (strings != null && strings.length == 1) {
                    Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
                    Hpi api = retrofit.create(Hpi.class);
                    return api.onFavouriteShow(strings[0]).execute().body();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}