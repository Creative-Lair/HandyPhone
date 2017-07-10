package com.creativelair.handyphone.Screens;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativelair.handyphone.Adapters.SettingsAdapter;
import com.creativelair.handyphone.EmergencyContact;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> items;
    private SQLiteHandler db;
    private Preference preference;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        listView = (ListView) findViewById(R.id.list);
        db = new SQLiteHandler(this);
        items = new ArrayList<>();
        preference = new Preference(this);
        actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Setting");


        adapter = new SettingsAdapter(this, R.layout.settinglistitems, items);

        items.add("Clear Data:Wipe away all the contacts in categories, emergency contact and custom message templates");
        items.add("Update Contacts:Resync all contacts");
        items.add("Emergency Contact:Add or Update Emergency Contact");
        items.add("About:About us");

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.textView);
        String string = textView.getText().toString();

        switch (string){
            case "About":
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;

            case "Update Contacts":
                db.deleteAllContacts();
                LoadContactsAyscn loadContactsAyscn = new LoadContactsAyscn();
                loadContactsAyscn.execute();
                break;

            case "Emergency Contact":
                Intent emergency = new Intent(this, EmergencyContact.class);
                startActivity(emergency);


                break;

            case "Clear Data":
                new AlertDialog.Builder(this)
                        .setTitle("Clear Data")
                        .setMessage("Are you sure you want to clear data?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.deleteContact();
                                db.deleteMSG();
                                preference.setMSGLOADED(false);
                                preference.setLoad(false);
                                preference.setMSG(true);
                                preference.setEnumber("");
                                preference.setPic(null);
                                preference.setEname("");
                                preference.setEmergency(false);
                                Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                break;
        }
    }

    public class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<Contacts>> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Settings.this);
            pd.setTitle("Updating Contacts...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected ArrayList<Contacts> doInBackground(Void... params) {
            ArrayList<Contacts> contacts = new ArrayList<>();

            String[] columns = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_ID};

            Cursor c1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    columns,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            while (c1.moveToNext()) {

                String contactName = c1.getString(0);
                int hasPhone = c1.getInt(1);
                String c_id = c1.getString(2);

                if (hasPhone > 0) {
                    String[] columns1 = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

                    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
                    String[] whereargs = {c_id};

                    Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            columns1,
                            where,
                            whereargs,
                            null);
                    if (c.moveToNext()) {

                        String phNumber = c.getString(0);
                        Bitmap photo;
                        int id = c.getInt(2);
                        int imgId = c.getInt(1);

                        Contacts contacts1;
                        photo = queryContactImage(imgId);
                        if (photo != null)
                            contacts1 = new Contacts(contactName, phNumber, photo, id);
                        else
                            contacts1 = new Contacts(contactName, phNumber, "", id);
                        contacts1.setGroup(" ");
                        db.addContact(contacts1);
                        contacts.add(contacts1);
                        c.close();
                    }
                }
            }
            c1.close();
            return contacts;
        }

        public Bitmap queryContactImage(int imageDataRow) {
            Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                    ContactsContract.CommonDataKinds.Photo.PHOTO
            }, ContactsContract.Data._ID + "=?", new String[] {
                    Integer.toString(imageDataRow)
            }, null);
            byte[] imageBytes = null;
            if (c != null) {
                if (c.moveToFirst()) {
                    imageBytes = c.getBlob(0);
                }
                c.close();
            }

            if (imageBytes != null) {
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            } else {
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<Contacts> contacts) {
            super.onPostExecute(contacts);
            if (pd.isShowing()) {
                pd.hide();
            }
            preference.setLoad(true);
            Toast.makeText(Settings.this, "Resync Complete", Toast.LENGTH_SHORT).show();
        }
    }
}
