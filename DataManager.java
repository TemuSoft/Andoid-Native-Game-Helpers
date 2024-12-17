package com.c0cktai1.dr55ms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataManager {

    private SQLiteDatabase db;

    public DataManager(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Create operation
    public long addItemArticle(String title, String dis, String image, String owner) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TITLE, title);
        values.put(DatabaseHelper.DIS, dis);
        values.put(DatabaseHelper.IMAGE, image);
        values.put(DatabaseHelper.OWNER, owner);

        return db.insert(DatabaseHelper.TABLE_ARTICLE, null, values);
    }

    // Create operation
    public long addItem(String title, String type, String dis, String status, String image, String owner, String[] ingredients) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TITLE, title);
        values.put(DatabaseHelper.TYPE, type);
        values.put(DatabaseHelper.DIS, dis);
        values.put(DatabaseHelper.STATUS, status);
        values.put(DatabaseHelper.IMAGE, image);
        values.put(DatabaseHelper.OWNER, owner);

        db.insert(DatabaseHelper.TABLE_MAIN, null, values);

        Cursor cursor = db.query(DatabaseHelper.TABLE_MAIN, null, null, null, null, null, DatabaseHelper.COLUMN_ID  + " DESC limit 1");
        cursor.moveToFirst();
        String id = String.valueOf(cursor.getInt(0)); // Index of _id column

        for (int i = 0; i < ingredients.length; i++) {
            values = new ContentValues();
            values.put(DatabaseHelper.TYPE_ID, id);
            values.put(DatabaseHelper.ING_DIS, ingredients[i]);

            db.insert(DatabaseHelper.TABLE_INGREDIENT, null, values);
        }

        return 0;
    }

    // Read operation
    public ArrayList<ArrayList<String>> getAllItems() {
        ArrayList<ArrayList<String>> item_list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MAIN, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getInt(0)); // Index of _id column
                String title = cursor.getString(1); // Index of title column
                String type = cursor.getString(2); // Index of type column
                String dis = cursor.getString(3); // Index of dis column
                String status = cursor.getString(4); // Index of status column
                String image = cursor.getString(5); // Index of image column
                String owner = cursor.getString(6); // Index of owner column
                ArrayList<String> one_row = new ArrayList<>();
                one_row.add(id);
                one_row.add(title);
                one_row.add(type);
                one_row.add(dis);
                one_row.add(status);
                one_row.add(image);
                one_row.add(owner);
                item_list.add(one_row);

                String selection = DatabaseHelper.TYPE_ID + "=?";
                String[] selectionArgs = {id};
                Cursor cu = db.query(DatabaseHelper.TABLE_INGREDIENT, null, selection, selectionArgs, null, null, null);
                ArrayList<String> ings = new ArrayList<>();
                if (cu != null && cu.moveToFirst()) {
                    do {
                        String ing_id = String.valueOf(cu.getInt(0)); // Index of _id column
                        String ing_type = cu.getString(1); // Index of type column
                        String ing_name = cu.getString(2); // Index of name column

                        ings.add(ing_name);
                    } while (cu.moveToNext());
                    cu.close();
                }
                item_list.add(ings);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return item_list;
    }

    // Read operation
    public ArrayList<ArrayList<String>> getAllItemsArticle() {
        ArrayList<ArrayList<String>> item_list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ARTICLE, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getInt(0)); // Index of _id column
                String title = cursor.getString(1); // Index of title column
                String dis = cursor.getString(2); // Index of dis column
                String image = cursor.getString(3); // Index of image column
                String owner = cursor.getString(4); // Index of owner column
                ArrayList<String> one_row = new ArrayList<>();
                one_row.add(id);
                one_row.add(title);
                one_row.add(dis);
                one_row.add(image);
                one_row.add(owner);
                item_list.add(one_row);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return item_list;
    }

    // Read operation
    public ArrayList<ArrayList<String>> getItemsByItemID(String itemId) {
        ArrayList<ArrayList<String>> item_list = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        Cursor cursor = db.query(DatabaseHelper.TABLE_MAIN, null, selection, selectionArgs, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getInt(0)); // Index of _id column
                String title = cursor.getString(1); // Index of title column
                String type = cursor.getString(2); // Index of type column
                String dis = cursor.getString(3); // Index of dis column
                String status = cursor.getString(4); // Index of status column
                String image = cursor.getString(5); // Index of image column
                String owner = cursor.getString(6); // Index of owner column
                ArrayList<String> one_row = new ArrayList<>();
                one_row.add(id);
                one_row.add(title);
                one_row.add(type);
                one_row.add(dis);
                one_row.add(status);
                one_row.add(image);
                one_row.add(owner);
                item_list.add(one_row);

                selection = DatabaseHelper.TYPE_ID + "=?";
                selectionArgs = new String[]{id};
                Cursor cu = db.query(DatabaseHelper.TABLE_INGREDIENT, null, selection, selectionArgs, null, null, null);
                ArrayList<String> ings = new ArrayList<>();
                if (cu != null && cu.moveToFirst()) {
                    do {
                        String ing_id = String.valueOf(cu.getInt(0)); // Index of _id column
                        String ing_type = cu.getString(1); // Index of type column
                        String ing_name = cu.getString(2); // Index of name column

                        ings.add(ing_name);
                    } while (cu.moveToNext());
                    cu.close();
                }
                item_list.add(ings);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return item_list;
    }


    // Read operation
    public ArrayList<ArrayList<String>> getItemsByItemIDArticle(String itemId) {
        ArrayList<ArrayList<String>> item_list = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        Cursor cursor = db.query(DatabaseHelper.TABLE_ARTICLE, null, selection, selectionArgs, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getInt(0)); // Index of _id column
                String title = cursor.getString(1); // Index of title column
                String dis = cursor.getString(2); // Index of dis column
                String image = cursor.getString(3); // Index of image column
                String owner = cursor.getString(4); // Index of owner column
                ArrayList<String> one_row = new ArrayList<>();
                one_row.add(id);
                one_row.add(title);
                one_row.add(dis);
                one_row.add(image);
                one_row.add(owner);
                item_list.add(one_row);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return item_list;
    }

    // Update operation
    public int updateItem(int id, String status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.STATUS, status);
        return db.update(DatabaseHelper.TABLE_MAIN, values, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Delete operation
    public int deleteItem(long id) {
        return db.delete(DatabaseHelper.TABLE_MAIN, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete operation
    public int deleteAllItem() {
        return db.delete(DatabaseHelper.TABLE_MAIN, null, null);
    }


    public static String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return sdf.format(date);
    }
}
