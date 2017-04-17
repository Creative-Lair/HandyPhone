package com.creativelair.handyphone.Helpers;

import android.graphics.Bitmap;

public class Contacts {
    private String Name;
    private String Number;
    private Bitmap icon;
    private String Group;
    private String Color;
    private int id;

    public Contacts() {
        id = -1;
        Name = null;
        Number = null;
        icon = null;
        Group = null;
        Color = null;

    }

    public Contacts(String name, String number, Bitmap icon, int id) {
        Name = name;
        Number = number;
        this.icon = icon;
        this.id = id;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}




