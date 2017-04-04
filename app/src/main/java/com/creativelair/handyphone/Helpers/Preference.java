package com.creativelair.handyphone.Helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

public class Preference {
    // Shared preferences file name
    private static final String PREF_NAME = "Handy Phone";
    private static final String KEY_NAME = "Contact Name";
    private static final String KEY_PHONE = "Contact Phone";
    private static final String KEY_GROUP = "Contact Group";
    private static final String KEY_PIC = "Contact Pic";
    private static final String KEY_ID = "Contact Id";
    private static final String KEY_LOAD = "Load Database";
    public static Bitmap bitmap;
    private static String TAG = Preference.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    public Preference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, "Enter Name");
    }

    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();

        Log.d(TAG, "Name added");
    }

    public String getPhone() {
        return pref.getString(KEY_PHONE, "Enter Number");
    }

    public void setPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.commit();

        Log.d(TAG, "Phone added");
    }

    public String getGroup() {
        return pref.getString(KEY_GROUP, "Work");
    }

    public void setGroup(String group) {
        editor.putString(KEY_GROUP, group);
        editor.commit();

        Log.d(TAG, "Group added");
    }

    public Bitmap getPic() {
        return bitmap;
    }

    public void setPic(Bitmap pic) {
        bitmap = pic;
    }

    public boolean getLoad() {
        return pref.getBoolean(KEY_LOAD, false);
    }

    public void setLoad(boolean load) {
        editor.putBoolean(KEY_LOAD, load);
        editor.commit();
    }

    public int getId() {
        return pref.getInt(KEY_ID, -1);
    }

    public void setId(int id) {
        editor.putInt(KEY_ID, id);
        editor.commit();

        Log.d(TAG, "Id added");

    }

}