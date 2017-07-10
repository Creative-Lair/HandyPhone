package com.creativelair.handyphone.Screens;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditContact extends AppCompatActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private final int PICK_PHOTO = 1;
    private final int CAMERA = 4;
    ActionBar actionBar;
    TextView name, phone;
    ImageView image;
    CheckBox work, friend, family;
    Button add, gallery;
    SQLiteHandler db;
    Bitmap mBitmap;
    Contacts contact;
    Contacts oldcontact;
    private String mCurrentPhotoPath;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Update Contact");
        db = new SQLiteHandler(this);
        preference = new Preference(getApplicationContext());
        oldcontact = new Contacts();

        oldcontact.setName(preference.getName());
        oldcontact.setNumber(preference.getPhone());
        oldcontact.setId(preference.getId());
        mBitmap = preference.getPic();

        prepare();
        intialize();
        setListener();
    }

    private void intialize() {
        name.setText(preference.getName());
        phone.setText(preference.getPhone());
        image.setImageBitmap(preference.getPic());
        //    Toast.makeText(this, preference.getGroup(), Toast.LENGTH_SHORT).show();
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
        add = (Button) findViewById(R.id.update);
        gallery = (Button) findViewById(R.id.gallery);
        image = (ImageView) findViewById(R.id.icon);
    }

    public void setListener() {
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
        switch (id) {
            case R.id.update:

                String mName = name.getText().toString().trim();
                String mPhone = phone.getText().toString().trim();
                String category = check();

                if (mName.equals("")) {
                    Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (mPhone.equals("")) {
                    Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (category == null) {
                    Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
                    break;
                }


                mName = mName.substring(0, 1).toUpperCase() + mName.substring(1);


                contact = new Contacts();

                contact.setGroup(category);
                contact.setName(mName);
                contact.setNumber(mPhone);
                contact.setId(preference.getId());

                if (mBitmap != null) {
                    contact.setIcon(mBitmap);
                } else {
                    contact.setIcon(null);
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    db.updateContact(oldcontact, contact);
                    updateContact(oldcontact, contact);

                    preference.setName(contact.getName());
                    preference.setPhone(contact.getNumber());
                    preference.setGroup(contact.getGroup());
                    preference.setPic(contact.getIcon());
                    preference.setId(contact.getId());

                    Intent i = new Intent(this, DisplayContact.class);
                    startActivity(i);
                    finish();

                }

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

    private boolean updateContact(Contacts oldcontact, Contacts contact) {
        boolean success = false;
        String name = contact.getName();
        String number = contact.getNumber();
        Bitmap pic = contact.getIcon();
        int ContactId = contact.getId();
        String oldname = oldcontact.getName();
        String oldnumber = oldcontact.getNumber();

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        String where = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + "=?";

    /*    if (name != null) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, new String[]{oldname})
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.Data.DISPLAY_NAME, name)
                        .build());
                Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            }
       /*     ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(where, new String[]{oldnumber})
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(where, new String[]{oldnumber})
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,stream.toByteArray())
                    .build());


            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data



            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
//
        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Contact is successfully Updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
        return success;
    }

    public String check() {

        if (work.isChecked()) {
            return "Work";
        } else if (friend.isChecked()) {
            return "Friend";
        } else if (family.isChecked()) {
            return "Family";
        } else {
            return null;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = buttonView.getId();

        switch (id) {

            case R.id.work:
                if (isChecked) {
                    family.setChecked(false);
                    friend.setChecked(false);
                    //    Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.family:
                if (isChecked) {
                    work.setChecked(false);
                    friend.setChecked(false);

                    //   Toast.makeText(this, "Family", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.friends:
                if (isChecked) {
                    family.setChecked(false);
                    work.setChecked(false);

                    //  Toast.makeText(this, "Friend", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    // Getting the uri of the picked photo
                    Uri selectedImage = data.getData();

                    InputStream imageStream = null;
                    try {
                        // Getting InputStream of the selected image
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Creating bitmap of the selected image from its inputstream
                    mBitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] imageInByte = stream.toByteArray();
                    long lengthbmp = imageInByte.length /1024;
                    if(lengthbmp < 1000) {
                        image.setImageBitmap(mBitmap);
                    } else {
                        Toast.makeText(this, "Image too large... Your Image should be less them 1MB", Toast.LENGTH_SHORT).show();
                    }

                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    db.updateContact(oldcontact, contact);
                    updateContact(oldcontact, contact);
                } else {
                    Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
