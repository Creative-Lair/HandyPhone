package com.creativelair.handyphone.Fragment;

import android.Manifest;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creativelair.handyphone.Adapters.ContactListAdapter;
import com.creativelair.handyphone.GridSpacingItemDecoration;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;
import com.creativelair.handyphone.RecyclerItemClickListener;
import com.creativelair.handyphone.Screens.AddContact;

import java.util.ArrayList;


/**
 * Created by HishamAhmed on 04-Apr-17.
 */

public class Frequent extends Fragment implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static LayoutInflater inflat = null;
    RecyclerView listView;
    Preference preference;
    FloatingActionButton fb;
    ArrayList<Contacts> frequent;
    boolean perms = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_contacts, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        fb = (FloatingActionButton) view.findViewById(R.id.add);

        fb.setOnClickListener(this);
        frequent = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        listView.setItemAnimator(new DefaultItemAnimator());
        preference = new Preference(getActivity());
        listView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), listView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view == null) {
                            view = inflat.inflate(R.layout.all_contacts, null);
                        }
                        Contacts contacts = frequent.get(position);
                        MyDialog myDialog = new MyDialog(contacts, preference.getColor());
                        myDialog.show(getActivity().getFragmentManager(), "my_dialog");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        checkPermission();
        return view;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            perms = false;
            Frequent.LoadContactsAyscn loadContactsAyscn = new Frequent.LoadContactsAyscn();
            loadContactsAyscn.execute();
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add:
                Intent i = new Intent(getContext(), AddContact.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                perms = true;
                Frequent.LoadContactsAyscn loadContactsAyscn = new Frequent.LoadContactsAyscn();
                loadContactsAyscn.execute();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we can't display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause(){
        frequent.clear();
        super.onPause();
    }

    public class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<Contacts>> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (perms) {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Loading Contacts");
                pd.show();
            }
        }

        @Override
        protected ArrayList<Contacts> doInBackground(Void... params) {
            ArrayList<Contacts> contacts = new ArrayList<>();

            String[] columns = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_ID};


            Cursor c1 = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_STREQUENT_URI,
                    columns,
                    null,
                    null,
                    null);


            while (c1.moveToNext()) {

                String contactName = c1.getString(0);
                int hasPhone = c1.getInt(1);
                String c_id = c1.getString(2);

                if (hasPhone > 0) {
                    String[] columns1 = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                            ContactsContract.CommonDataKinds.Phone._ID,
                            ContactsContract.CommonDataKinds.Photo.PHOTO};

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
                        if (photo != null) {
                            contacts1 = new Contacts(contactName, phNumber, photo, id);
                        } else
                            contacts1 = new Contacts(contactName, phNumber, "", id);
                        contacts1.setGroup("Frequent");
                        contacts.add(contacts1);
                        c.close();
                    }
                }
            }
            c1.close();
            return contacts;
        }


        public Bitmap queryContactImage(int imageDataRow) {
            Cursor c = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                    ContactsContract.CommonDataKinds.Photo.PHOTO
            }, ContactsContract.Data._ID + "=?", new String[]{
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
            frequent = contacts;
            ContactListAdapter adapter = new ContactListAdapter(getContext(), frequent);
            listView.setAdapter(adapter);
            if (perms) {
                if (pd.isShowing()) {
                    pd.hide();
                }
            }
        }
    }
}