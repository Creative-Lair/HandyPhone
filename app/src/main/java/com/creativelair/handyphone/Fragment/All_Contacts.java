package com.creativelair.handyphone.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creativelair.handyphone.Adapters.ContactListAdapter;
import com.creativelair.handyphone.GridSpacingItemDecoration;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;
import com.creativelair.handyphone.RecyclerItemClickListener;
import com.creativelair.handyphone.Screens.AddContact;

import java.util.ArrayList;

public class All_Contacts extends Fragment implements View.OnClickListener{

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static LayoutInflater inflat = null;
    RecyclerView listView;
    FloatingActionButton fb;
    ArrayList<Contacts> allcontacts;
    Preference preference;
    SQLiteHandler db;
    ProgressDialog pd;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_contacts, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        fb = (FloatingActionButton) view.findViewById(R.id.add);

        activity = getActivity();
        fb.setOnClickListener(this);

        preference = new Preference(activity);
        db = new SQLiteHandler(activity);

        allcontacts = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        listView.setItemAnimator(new DefaultItemAnimator());

        listView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), listView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(view==null) {
                            view = inflat.inflate(R.layout.all_contacts, null);
                        }

                        Contacts contacts = allcontacts.get(position);

                        MyDialog myDialog = new MyDialog(contacts);
                        myDialog.show(getActivity().getFragmentManager(), "my_dialog");
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        checkPermission();
        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            if (preference.getLoad()) {
                allcontacts = db.getContactDetails();
                ContactListAdapter adapter = new ContactListAdapter(activity, allcontacts);
                listView.setAdapter(adapter);
            } else {
                LoadContactsAyscn loadContactsAyscn = new LoadContactsAyscn();
                loadContactsAyscn.execute();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (preference.getLoad()) {
                    allcontacts = db.getContactDetails();
                    ContactListAdapter adapter = new ContactListAdapter(activity, allcontacts);
                    listView.setAdapter(adapter);
                } else {
                    LoadContactsAyscn loadContactsAyscn = new LoadContactsAyscn();
                    loadContactsAyscn.execute();
                }

            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add:
                Intent i = new Intent( getContext() , AddContact.class);
                startActivity(i);
                break;
        }
    }

    public class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<Contacts>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected ArrayList<Contacts> doInBackground(Void... params) {
            ArrayList<Contacts> contacts = new ArrayList<>();

            String[] columns = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_ID};


            Cursor c1 = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    columns,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            while (c1.moveToNext()) {

                String contactName = c1.getString(0);
                int hasPhone = c1.getInt(1);
                String c_id = c1.getString(2);

                Log.d("Names", contactName + " " + hasPhone + " " + c_id + " " + c1.getString(3));

                if (hasPhone > 0) {
                    String[] columns1 = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

                    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
                    String[] whereargs = {c_id};

                    Cursor c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
                            contacts1 = new Contacts(contactName, phNumber, null, id);
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
            Cursor c = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
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
            preference.setLoad(true);
            allcontacts = contacts;
            ContactListAdapter adapter = new ContactListAdapter(getContext(), allcontacts);
            listView.setAdapter(adapter);
        }

    }



}