package com.creativelair.handyphone.Helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
       image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_CONTACTNUMBER, phone); // Email
        values.put(KEY_GROUP, group);

        if(image ==  null){
            values.put(KEY_CONTACTPIC, "");
        }else {
            values.put(KEY_CONTACTPIC, img);
        }

        long uid = db.insert(TABLE_CONTACT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + name);
    }

    public Contacts getContactDetails() {
        Contacts user = new Contacts();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();



        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
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
