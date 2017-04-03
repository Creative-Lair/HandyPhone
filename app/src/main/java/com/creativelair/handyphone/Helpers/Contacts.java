package com.creativelair.handyphone.Helpers;

import android.graphics.Bitmap;

public class Contacts {
    private String Name;
    private String Number;
    private String icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public Contacts(String name, String number, String icon) {
        Name = name;
        Number = number;
        this.icon = icon;
    }
}
