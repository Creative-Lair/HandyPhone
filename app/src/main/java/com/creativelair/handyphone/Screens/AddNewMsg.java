package com.creativelair.handyphone.Screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Message;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;

import java.util.ArrayList;

/**
 * Created by HishamAhmed on 08-Jun-17.
 */

public class AddNewMsg extends AppCompatActivity
        implements View.OnClickListener {

    private ActionBar actionBar;
    private EditText head, msg;
    private Button btnAdd;
    private ListView lv;
    private android.support.v7.app.AlertDialog.Builder builder;
    private ArrayList<Message> messages;
    private Preference pref;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_msg);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Add Template");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        set();
    }

    public void set(){
        head = (EditText) findViewById(R.id.heading);
        msg = (EditText) findViewById(R.id.new_msg);
        btnAdd = (Button) findViewById(R.id.btnAddTemp);
        lv = (ListView) findViewById(R.id.lv);
        db = new SQLiteHandler(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        messages = new ArrayList<>();
        switch (id) {
            case R.id.btnAddTemp:
                String heading = head.getText().toString().trim();
                String mesg = msg.getText().toString().trim();

                if (heading.equals("")) {
                    Toast.makeText(this, "Please Enter Heading!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (mesg.equals("")) {
                    Toast.makeText(this, "Please Enter Message!", Toast.LENGTH_SHORT).show();
                    break;
                }
                pref = new Preference(this);
                pref.setHeading(heading);
                pref.setMsg(mesg);
                //Message msg = new Message(heading,mesg);
                //db.addMessage(msg, );

                finish();
        }
    }
}