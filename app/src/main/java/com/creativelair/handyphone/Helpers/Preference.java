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
    private static final String KEY_COLOR = "Color";
    private static final String HEADING = "Head";
    private static final String MESSAGE = "Message Body";
    private static final String SEND_MESSAGE = "msg";
    private static final String CALL = "call";
    private static final String CALLANDMESSAGE = "callMsg";
    private static final String MESSAGE_TEXT = "msg_text";
    private static final String EMERGENCY = "emergency";
    private static final String ENAME = "ename";
    private static final String ENUMBER = "enumber";
    private static final String MESSAGELOADED = "msgloaded";





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
    }

    public String getColor() {
        return pref.getString(KEY_COLOR, "#000000");
    }

    public void setColor(String color) {
        editor.putString(KEY_COLOR, color);
        editor.commit();
    }

    public void setHeading(String head){
        editor.putString(HEADING, head);
        editor.commit();
    }

    public String getHeading(){
        return pref.getString(HEADING, "Head");
    }

    public void setMsg(String msgg){
        editor.putString(MESSAGE, msgg);
        editor.commit();
    }

    public String getMsg(){
        return pref.getString(MESSAGE, "Message Body");
    }

    public String getPhone() {
        return pref.getString(KEY_PHONE, "Enter Number");
    }

    public void setPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }

    public String getGroup() {
        return pref.getString(KEY_GROUP, "Work");
    }

    public void setGroup(String group) {
        editor.putString(KEY_GROUP, group);
        editor.commit();
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
    }

    public void setMSG(boolean v){
        editor.putBoolean(SEND_MESSAGE, v);
        editor.commit();
    }

    public void setCALL(boolean v){
        editor.putBoolean(CALL, v);
        editor.commit();
    }

    public void setCALLMSG(boolean v){
        editor.putBoolean(CALLANDMESSAGE, v);
        editor.commit();
    }

    public boolean getMSG(){
        return pref.getBoolean(SEND_MESSAGE, true);
    }

    public boolean getCALL(){
        return pref.getBoolean(CALL, false);
    }

    public boolean getCALLMSG(){
        return pref.getBoolean(CALLANDMESSAGE, false);
    }

    public void setMSGTEXT(String v){
        editor.putString(MESSAGE_TEXT, v);
        editor.commit();
    }

    public String getMSGTEXT(){
        return pref.getString(MESSAGE_TEXT, "I am in trouble!!");
    }

    public void setEmergency(Boolean v){
        editor.putBoolean(EMERGENCY, v);
        editor.commit();
    }

    public boolean getEmergency(){
        return pref.getBoolean(EMERGENCY, false);
    }

    public void setEname(String ename) {
        editor.putString(ENAME, ename);
        editor.commit();
    }

    public void setEnumber(String enumber) {
        editor.putString(ENUMBER, enumber);
        editor.commit();
    }

    public String getEname(){
        return pref.getString(ENAME, "");
    }

    public String getEnumber(){
        return pref.getString(ENUMBER, "");
    }

    public Boolean getMSGLOADED(){
        return pref.getBoolean(MESSAGELOADED, false);
    }

    public void setMSGLOADED(boolean v){
        editor.putBoolean(MESSAGELOADED, v);
        editor.commit();
    }

}