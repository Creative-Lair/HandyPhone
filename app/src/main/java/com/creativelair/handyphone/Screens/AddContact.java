package com.creativelair.handyphone.Screens;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddContact extends AppCompatActivity
implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private final int PICK_PHOTO = 1;
    ActionBar actionBar;
    EditText name, phone;
    ImageView image;
    CheckBox work,friend,family;
    Button add, gallery;
    SQLiteHandler db;
    Contacts contact;
    String path;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Contact");
        db = new SQLiteHandler(this);
        preference = new Preference(getApplicationContext());

        prepare();
        setListener();
    }

    public void prepare(){
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        work = (CheckBox) findViewById(R.id.work);
        friend = (CheckBox) findViewById(R.id.friends);
        family = (CheckBox) findViewById(R.id.family);
        add = (Button) findViewById(R.id.add);
        gallery = (Button) findViewById(R.id.gallery);
        image = (ImageView) findViewById(R.id.icon);
    }

    public void setListener(){
        work.setOnCheckedChangeListener(this);
        family.setOnCheckedChangeListener(this);
        friend.setOnCheckedChangeListener(this);
        add.setOnClickListener(this);
        gallery.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add:
                String mName = name.getText().toString().trim();
                String mPhone = phone.getText().toString().trim();
                String category = check();

                if(mName.equals("")){
                    Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(mPhone.equals("")){
                    Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(category == null){
                    Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
                    break;
                }
                mName = mName.substring(0,1).toUpperCase() + mName.substring(1);

                contact = new Contacts();

                contact.setGroup(category);
                contact.setName(mName);
                contact.setNumber(mPhone);
                if (path != null) {
                    contact.setIcon(path);
                } else {
                    contact.setIcon("");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    contact.setId(addContact(contact));
                    db.addContact(contact);
                    Contacts c = new Contacts();
                    c.setGroup("All");
                    c.setNumber(mPhone);
                    c.setIcon(contact.getIcon());
                    c.setName(mName);
                    db.addContact(c);

                }

                preference.setName(contact.getName());
                preference.setPhone(contact.getNumber());
                preference.setGroup(contact.getGroup());
                preference.setPic(path);
                if(path != null){
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    preference.setBitmap(bitmap);
                    preference.setId(contact.getId());
                }

                Intent i = new Intent(this, DisplayContact.class);
                startActivity(i);
                finish();

                break;

            case R.id.gallery:
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_PHOTO);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_PHOTO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public int addContact(Contacts contact) {
        String DisplayName = contact.getName();
        String MobileNumber = contact.getNumber();

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<>();

        int rawContactID = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName)
                    .build());
        }
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Contact is successfully added!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawContactID;
    }

    public String check(){

        if(work.isChecked()){
            return "Work";
        } else if (friend.isChecked()){
            return "Friend";
        } else if (family.isChecked()){
            return "Family";
        } else {
            return null;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = buttonView.getId();

        switch (id){

            case R.id.work:
                if(isChecked){
                    family.setChecked(false);
                    friend.setChecked(false);
                    Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.family:
                if(isChecked){
                    work.setChecked(false);
                    friend.setChecked(false);

                    Toast.makeText(this, "Family", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.friends:
                if(isChecked){
                    family.setChecked(false);
                    work.setChecked(false);

                    Toast.makeText(this, "Friend", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case PICK_PHOTO:
                if(resultCode == RESULT_OK){
                    // Getting the uri of the picked photo

                    Uri SelectedImage = data.getData();
                    String[] filepathcolumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(SelectedImage, filepathcolumn, null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filepathcolumn[0]);
                    path = cursor.getString(columnIndex);
                    cursor.close();
                    Glide.with(image.getContext()).load(path).into(image);
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PICK_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO);
                }
                else {
                    Toast.makeText(this, "Until you grant the permission, we can't display the names.", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    db.addContact(contact);
                    addContact(contact);
                } else {
                    Toast.makeText(this, "Until you grant the permission, we can't display the names.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
