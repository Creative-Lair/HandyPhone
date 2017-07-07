package com.creativelair.handyphone;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.Screens.AddContact;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HishamAhmed on 12-Jun-17.
 */

public class EmergencyContact extends AppCompatActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    Contacts contacts;
    SQLiteHandler db;
    EditText name;
    EditText number, msg_box;
    CheckBox call, msg, callMsg;
    Preference pref;
    private ActionBar actionBar;

    private Button btn;
    private Button imgBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteHandler(this);
        pref = new Preference(this);

        loadEmergencyScreen();
    }

    public void loadEmergencyScreen() {
        setContentView(R.layout.emergency);

        msg_box = (EditText)findViewById(R.id.msg_box);
        name = (EditText) findViewById(R.id.emg_name);
        number = (EditText) findViewById(R.id.emg_numbr);
        btn = (Button) findViewById(R.id.emg_btn);
        call = (CheckBox) findViewById(R.id.call);
        msg = (CheckBox) findViewById(R.id.msg);
        callMsg = (CheckBox) findViewById(R.id.callMsg);
        imgBtn = (Button) findViewById(R.id.imageButton);


        call.setOnCheckedChangeListener(this);
        msg.setOnCheckedChangeListener(this);
        callMsg.setOnCheckedChangeListener(this);

        call.setChecked(pref.getCALL());
        msg.setChecked(pref.getMSG());
        callMsg.setChecked(pref.getCALLMSG());

        if(call.isChecked()){
            msg_box.setEnabled(false);
        }

        msg_box.setText(pref.getMSGTEXT());
        imgBtn.setOnClickListener(this);
        btn.setOnClickListener(this);

        name.setText(pref.getEname());
        number.setText(pref.getEnumber());

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Emergency");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.emg_btn:
                String sname = name.getText().toString().trim();
                String snum = number.getText().toString().trim();
                String msgText = msg_box.getText().toString().trim();

                if(msg.isChecked()) {
                    pref.setMSG(true);
                    pref.setCALL(false);
                    pref.setCALLMSG(false);
                } else if (call.isChecked()){
                    pref.setMSG(false);
                    pref.setCALL(true);
                    pref.setCALLMSG(false);
                } else if(callMsg.isChecked()){
                    pref.setMSG(false);
                    pref.setCALL(false);
                    pref.setCALLMSG(true);
                } else {
                    pref.setMSG(true);
                    pref.setCALL(false);
                    pref.setCALLMSG(false);
                }

                if(msgText.equals("") && (msg.isChecked()||callMsg.isChecked())){
                    Toast.makeText(this, "Please Set Some Msg Text!!", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    pref.setMSGTEXT(msgText);
                }

                if (sname.equals("") || sname.equals(" ")) {
                    Toast.makeText(this, "A person without name is quite useless!!\n" +
                            "Please enter emergency contact name.", Toast.LENGTH_LONG).show();
                    break;
                } else if (snum.equals("") || snum.equals(" ")) {
                    Toast.makeText(this, "A number identifies a human!!\n" +
                            "Please enter emergency contact number.", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    pref.setEname(sname);
                    pref.setEnumber(snum);
                    Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
                    pref.setEmergency(true);
                    finish();

                }
                break;

            case R.id.add_from_contacts:
                Toast.makeText(this, "Select emergency contact from already saved contacts.", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id  = buttonView.getId();

        switch (id) {
            case R.id.msg:
                if(msg.isChecked()) {
                    callMsg.setChecked(false);
                    call.setChecked(false);
                    msg_box.setEnabled(true);
                }
                break;

            case R.id.call:

                if(call.isChecked()) {
                    callMsg.setChecked(false);
                    msg.setChecked(false);
                    msg_box.setEnabled(false);
                }

                break;

            case R.id.callMsg:
                if(callMsg.isChecked()) {
                    msg.setChecked(false);
                    call.setChecked(false);
                    msg_box.setEnabled(true);
                }
                break;
        }
    }
}