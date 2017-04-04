package com.creativelair.handyphone.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orcl";
    private static final String TABLE_CONTACT = "contacts";

    private static final String KEY_ID = "conactid";
    private static final String KEY_NAME = "contactname";
    private static final String KEY_CONTACTPIC = "contactpic";
    private static final String KEY_CONTACTNUMBER = "contactnumber";
    private static final String KEY_GROUP = "contactgroup";
    private static final String WORK = "Work";
    private static final String FAMILY = "Family";
    private static final String FRIEND = "Friend";

    String[] Column = {
            KEY_ID,
            KEY_NAME,
            KEY_CONTACTNUMBER,
            KEY_GROUP,
            KEY_CONTACTPIC
    };

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_CONTACT + "(" +
               KEY_ID + " INTEGER NOT NULL , " +
               KEY_NAME + " TEXT NOT NULL, " +
                KEY_CONTACTNUMBER + " TEXT NOT NULL, " +
                KEY_GROUP + " TEXT NOT NULL, " +
                KEY_CONTACTPIC + " BLOB, PRIMARY KEY(" + KEY_ID + "))";

        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addContact(Contacts contacts) {
        SQLiteDatabase db = this.getWritableDatabase();

        String name = contacts.getName();
        String phone = contacts.getNumber();
        Bitmap image = contacts.getIcon();
        String group = contacts.getGroup();

        ContentValues values = new ContentValues();
        if (image != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] img = bos.toByteArray();
            values.put(KEY_CONTACTPIC, img);
        } else {
            values.put(KEY_CONTACTPIC, "");
        }
        values.put(KEY_NAME, name); // Name
        values.put(KEY_CONTACTNUMBER, phone); // Email
        values.put(KEY_GROUP, group);

        long uid = db.insert(TABLE_CONTACT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + name + group);
    }

    public void updateContact(Contacts oldcontacts, Contacts contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap image = contacts.getIcon();
        String where = KEY_NAME + "= ? AND " + KEY_CONTACTNUMBER + "=?";
        String[] whereargs = {oldcontacts.getName(), oldcontacts.getNumber()};
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, contacts.getName());
        cv.put(KEY_CONTACTNUMBER, contacts.getNumber());
        cv.put(KEY_GROUP, contacts.getGroup());

        if (image != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] img = bos.toByteArray();
            cv.put(KEY_CONTACTPIC, img);
        } else {
            cv.put(KEY_CONTACTPIC, "");
        }

        db.update(TABLE_CONTACT, cv, where, whereargs);

        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + contacts.getName() + " " + contacts.getNumber());


    }

    public ArrayList<Contacts> getContactDetails() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;
        ArrayList<Contacts> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                Contacts user = new Contacts();
                user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
                user.setGroup(cursor.getString(cursor.getColumnIndex(KEY_GROUP)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                Log.d(TAG, "Fetching user from Sqlite: " + user.getName() + user.getGroup());
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user
        // Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return contacts;
    }

    public ArrayList<Contacts> getWork() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;
        String whereClause = KEY_GROUP + "=?";
        String[] whereArgs = {WORK};
        ArrayList<Contacts> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACT, Column, whereClause, whereArgs, null, null, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                Contacts user = new Contacts();
                user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
                user.setGroup(cursor.getString(cursor.getColumnIndex(KEY_GROUP)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                Log.d(TAG, "Fetching user from Sqlite: " + user.getName() + user.getGroup());
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user

        return contacts;
    }

    public ArrayList<Contacts> getFamily() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;
        String whereClause = KEY_GROUP + "=?";
        String[] whereArgs = {FAMILY};
        ArrayList<Contacts> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACT, Column, whereClause, whereArgs, null, null, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                Contacts user = new Contacts();
                user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
                user.setGroup(cursor.getString(cursor.getColumnIndex(KEY_GROUP)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                Log.d(TAG, "Fetching user from Sqlite: " + user.getName() + user.getGroup());
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user
        // Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return contacts;
    }

    public ArrayList<Contacts> getFriend() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;
        String whereClause = KEY_GROUP + "=?";
        String[] whereArgs = {FRIEND};
        ArrayList<Contacts> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACT, Column, whereClause, whereArgs, null, null, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                Contacts user = new Contacts();
                user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setNumber(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
                user.setGroup(cursor.getString(cursor.getColumnIndex(KEY_GROUP)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                Log.d(TAG, "Fetching user from Sqlite: " + user.getName() + user.getGroup());
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user
        // Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return contacts;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CONTACT, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
