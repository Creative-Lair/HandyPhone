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
    private static final String KEY_CONTACT_ID = "id_Contact";
    private static final String WORK = "Work";
    private static final String FAMILY = "Family";
    private static final String FRIEND = "Friend";

    String[] Column = {
            KEY_ID,
            KEY_NAME,
            KEY_CONTACTNUMBER,
            KEY_GROUP,
            KEY_CONTACTPIC,
            KEY_CONTACT_ID
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
                KEY_CONTACTPIC + " BLOB," +
                KEY_CONTACT_ID + " INTEGER, PRIMARY KEY(" + KEY_ID + "))";

        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

    public void addContact(Contacts contacts) {
        SQLiteDatabase db = this.getWritableDatabase();

        String name = contacts.getName();
        String phone = contacts.getNumber();
        Bitmap image = contacts.getIcon();
        String group = contacts.getGroup();
        int id = contacts.getId();

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
        values.put(KEY_CONTACT_ID, id);

        long uid = db.insert(TABLE_CONTACT, null, values);
        db.close(); // Closing database connection

    }

    public void updateContact(Contacts oldcontacts, Contacts contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap image = contacts.getIcon();
        String where = KEY_CONTACTNUMBER + "= ? AND " + KEY_NAME + "=?";
        String[] whereargs = {oldcontacts.getNumber(), oldcontacts.getName()};
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, contacts.getName());
        cv.put(KEY_CONTACTNUMBER, contacts.getNumber());
        cv.put(KEY_GROUP, contacts.getGroup());
        cv.put(KEY_CONTACT_ID, contacts.getId());


        if (image != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 75, bos);
            byte[] img = bos.toByteArray();
            cv.put(KEY_CONTACTPIC, img);
            Log.d(TAG, "Image Added");
        } else {
            cv.put(KEY_CONTACTPIC, "");
            Log.d(TAG, "Image Not Added");
        }

        db.update(TABLE_CONTACT, cv, where, whereargs);

        db.close(); // Closing database connection



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
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_CONTACT_ID)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

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
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_CONTACT_ID)));
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
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_CONTACT_ID)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        
        return contacts;
    }

    public ArrayList<Contacts> getFriend() {
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
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_CONTACT_ID)));
                byte[] imageBytes = null;
                imageBytes = cursor.getBlob(cursor.getColumnIndex(KEY_CONTACTPIC));
                if (imageBytes != null) {
                    user.setIcon(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                } else {
                    user.setIcon(null);
                }
                contacts.add(user);
                System.out.print(cursor.getString(cursor.getColumnIndex(KEY_CONTACTNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return contacts;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACT, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
