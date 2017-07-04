package com.creativelair.handyphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.Screens.AddContact;

import java.util.ArrayList;

/**
 * Created by HishamAhmed on 12-Jun-17.
 */

public class EmergencyContact extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private final int PICK_PHOTO = 1;
    ArrayList<Contacts> contacts;
    RecyclerView listView;
    SQLiteHandler db;
    EditText name;
    EditText number;
    Bitmap mBitmap;
    ImageView img;
    Contacts contact;
    AddContact add;
    private ActionBar actionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*
        try{
            contacts = db.getEmergency();
        }catch (Exception e) {
            e.printStackTrace();
        }
    */
        if (contacts == null) {
            loadEmergencyContactAddScreen();
        } else {
            loadEmergencyScreen();
        }
    }

    public void loadEmergencyContactAddScreen() {
        setContentView(R.layout.emergrncy_add_contact);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Emergency");
        prepare();
    }

    public void loadEmergencyScreen() {
        setContentView(R.layout.emergency);
        contacts = db.getEmergency();
        prepare();
        name.setText(contact.getName());
        number.setText(contact.getNumber());

        /*
        ContactListAdapter adapter = new ContactListAdapter(this, contacts);
        listView.setAdapter(adapter);
        */

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Emergency");
    }

    public void prepare() {
        name = (EditText) findViewById(R.id.Name);
        number = (EditText) findViewById(R.id.Number);
        img = (ImageView) findViewById(R.id.icon);
    }

    public void onClick(View v) {
        int id = v.getId();
        String sname = name.getText().toString().trim();
        String snum = number.getText().toString().trim();
        switch (id) {
            case R.id.sav_emg_btn:
                if (sname.equals("") || sname.equals(" ")) {
                    Toast.makeText(this, "A person without name is quite useless!!\n" +
                            "Please enter emergency contact name.", Toast.LENGTH_LONG).show();
                    break;
                } else if (snum.equals("") || snum.equals(" ")) {
                    Toast.makeText(this, "A number identifies a human!!\n" +
                            "Please enter emergency contact number.", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    contact = new Contacts();
                    contact.setName(sname);
                    contact.setNumber(snum);
                    contact.setGroup("Emergency");
                    if (mBitmap != null) {
                        contact.setIcon(mBitmap);
                    } else {
                        contact.setIcon(null);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                            Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
                                PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        String tag = " Added ";
                        Log.d(tag, " Name: " + contact.getName() + " Number: " + contact.getNumber()
                                + " Group: " + contact.getGroup() + " Pic: " + contact.getIcon() + " ID: " + contact.getId());
                        contact.setId(add.addContact(contact));
                        db.addEmergencyContact(contact);
                    }
                    loadEmergencyScreen();
                }

            case R.id.gallery:
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_PHOTO);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_PHOTO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.add_from_contacts:
                Toast.makeText(this, "Select emergency contact from already saved contacts.", Toast.LENGTH_LONG).show();

                break;

            case R.id.emg_btn:

                break;
        }
    }
}