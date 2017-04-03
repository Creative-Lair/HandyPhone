package com.creativelair.handyphone.Helpers;

import android.graphics.Bitmap;

public class Contacts {
    private String Name;
    private String Number;
    private Bitmap icon;
    private String Group;

    public Contacts() {
        Name = null;
        Number = null;
        icon = null;
        Group = null;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public Contacts(String name, String number, Bitmap icon) {
        Name = name;
        Number = number;
        this.icon = icon;
    }
}




