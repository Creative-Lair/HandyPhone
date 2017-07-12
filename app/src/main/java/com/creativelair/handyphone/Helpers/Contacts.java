package com.creativelair.handyphone.Helpers;

import android.graphics.Bitmap;

import java.sql.Blob;

public class Contacts {
    private String Name;
    private String Number;
    private String icon;
    private String Group;
    private String Color;
    private Bitmap pic;
    private int id;

    public Contacts() {
        id = -1;
        Name = null;
        Number = null;
        icon = null;
        Group = null;
        Color = null;
        pic = null;
    }

    public Contacts(String name, String number, String icon, int id) {
        Name = name;
        Number = number;
        this.icon = icon;
        this.id = id;
    }

    public Contacts(String name, String number, Bitmap icon, int id) {
        Name = name;
        Number = number;
        this.pic = icon;
        this.id = id;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() { return Number; }

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
}
