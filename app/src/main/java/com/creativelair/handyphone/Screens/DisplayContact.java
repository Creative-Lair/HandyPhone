package com.creativelair.handyphone.Screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativelair.handyphone.Fragment.MessageDialog;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;

public class DisplayContact extends AppCompatActivity {

    ActionBar actionBar;
    TextView name, phone;
    ImageView image;
    CheckBox work, friend, family;
    SQLiteHandler db;
    Contacts contacts;
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
        Bitmap bitmap = preference.getBitmap();
        image.setImageBitmap(bitmap);
        if (preference.getGroup().equals("Work")) {
            work.setChecked(true);
        } else if (preference.getGroup().equals("Friend")) {
            friend.setChecked(true);
        } else if (preference.getGroup().equals("Family")) {
            family.setChecked(true);
        }
        contacts = new Contacts();
        contacts.setName(preference.getName());
        contacts.setNumber(preference.getName());
        contacts.setGroup(preference.getGroup());
        contacts.setId(preference.getId());
        contacts.setIcon(preference.getPic());
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

            case R.id.Call:
                if (isPermissionGranted()) {
                    call_action();
                }
                break;

            case R.id.msg:
                MessageDialog myDialog = new MessageDialog(contacts);
                myDialog.show(getFragmentManager(), "my_dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void call_action() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacts.getNumber()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                     Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
