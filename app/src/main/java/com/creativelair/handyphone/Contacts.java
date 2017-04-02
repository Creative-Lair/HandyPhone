package com.creativelair.handyphone;

import android.graphics.Bitmap;

/**
 * Created by AHSAN on 4/2/2017.
 */

public class Contacts {
    private String Name;
    private String Number;
    private Bitmap icon;

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

    public Contacts(String name, String number, Bitmap icon) {
        Name = name;
        Number = number;
        this.icon = icon;
    }
}
