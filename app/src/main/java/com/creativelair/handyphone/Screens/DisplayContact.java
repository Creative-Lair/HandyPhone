package com.creativelair.handyphone.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;

public class DisplayContact extends AppCompatActivity {

    ActionBar actionBar;
    TextView name, phone;
    ImageView image;
    CheckBox work, friend, family;
    SQLiteHandler db;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        db = new SQLiteHandler(this);
        preference = new Preference(getApplicationContext());
        actionBar.setTitle(preference.getName());


        prepare();
        intialize();
        setListener();
    }

    private void intialize() {
        name.setText(preference.getName());
        phone.setText(preference.getPhone());
        image.setImageBitmap(preference.getPic());
        Toast.makeText(this, preference.getGroup(), Toast.LENGTH_SHORT).show();
        if (preference.getGroup().equals("Work")) {
            work.setChecked(true);
        } else if (preference.getGroup().equals("Friend")) {
            friend.setChecked(true);
        } else if (preference.getGroup().equals("Family")) {
            family.setChecked(true);
        }

    }

    public void prepare() {

        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        work = (CheckBox) findViewById(R.id.work);
        friend = (CheckBox) findViewById(R.id.friends);
        family = (CheckBox) findViewById(R.id.family);
        image = (ImageView) findViewById(R.id.icon);
    }

    public void setListener() {
        work.setEnabled(false);
        family.setEnabled(false);
        friend.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.edit:
                Intent i = new Intent(this, EditContact.class);
                startActivity(i);
                finish();
                break;

            case android.R.id.home:
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
