package info.horriblesubs.sher.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.common.Strings;
import info.horriblesubs.sher.model.base.PageItem;

public class FavDBFunctions implements Strings {

    public static void addToFavourites(@NotNull Context context, @NotNull PageItem pageItem) {
        SQLiteDatabase database = new FavDB(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavID, pageItem.id);
        values.put(FavLink, pageItem.link);
        values.put(FavShowID, pageItem.sid);
        values.put(FavImage, pageItem.image);
        values.put(FavTitle, pageItem.title);
        database.insert(FavTable, null, values);
    }

    public static void removeFromFavourites(@NotNull Context context, @NotNull String s) {
        SQLiteDatabase database = new FavDB(context).getWritableDatabase();
        database.delete(FavTable, FavID + " = ?", new String[]{s});
    }

    @NotNull
    public static List<PageItem> getAllFavourites(@NotNull Context context) {
        SQLiteDatabase database = new FavDB(context).getReadableDatabase();
        String[] projection = {
                FavShowID,
                FavTitle,
                FavImage,
                FavLink,
                FavID
        };
        Cursor cursor = database.query(FavTable, projection, null, null,
                null, null, null);
        int a = cursor.getColumnIndex(FavID);
        int b = cursor.getColumnIndex(FavShowID);
        int c = cursor.getColumnIndex(FavTitle);
        int d = cursor.getColumnIndex(FavImage);
        int e = cursor.getColumnIndex(FavLink);
        List<PageItem> pageItems = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            PageItem pageItem = new PageItem();
            pageItem.id = cursor.getString(a);
            pageItem.sid = cursor.getString(b);
            pageItem.title = cursor.getString(c);
            pageItem.image = cursor.getString(d);
            pageItem.link = cursor.getString(e);
            pageItems.add(pageItem);
        }
        cursor.close();
        return pageItems;
    }

    public static boolean checkFavourite(@NotNull Context context, @NotNull String s) {
        SQLiteDatabase database = new FavDB(context).getReadableDatabase();
        String[] projection = {FavShowID, FavTitle, FavImage, FavLink, FavID};
        Cursor cursor = database.query(FavTable, projection, FavID + " = ?",
                new String[]{s}, null, null, null);
        int a = cursor.getColumnIndex(FavID);
        int b = cursor.getColumnIndex(FavShowID);
        int c = cursor.getColumnIndex(FavTitle);
        int d = cursor.getColumnIndex(FavImage);
        int e = cursor.getColumnIndex(FavLink);
        List<PageItem> pageItems = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            PageItem pageItem = new PageItem();
            pageItem.id = cursor.getString(a);
            pageItem.sid = cursor.getString(b);
            pageItem.title = cursor.getString(c);
            pageItem.image = cursor.getString(d);
            pageItem.link = cursor.getString(e);
            pageItems.add(pageItem);
        }
        cursor.close();
        return pageItems.size() == 1;
    }
}