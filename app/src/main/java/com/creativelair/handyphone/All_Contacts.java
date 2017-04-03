package com.creativelair.handyphone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.creativelair.handyphone.Adapters.ContactListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;

public class All_Contacts extends Fragment implements View.OnClickListener{

    RecyclerView listView;
    FloatingActionButton fb;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_contacts, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        fb = (FloatingActionButton) view.findViewById(R.id.add);

        fb.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        listView.setItemAnimator(new DefaultItemAnimator());

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
            LoadContactsAyscn loadContactsAyscn = new LoadContactsAyscn();
            loadContactsAyscn.execute();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadContactsAyscn loadContactsAyscn = new LoadContactsAyscn();
                loadContactsAyscn.execute();

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

    public class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<com.creativelair.handyphone.Contacts>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected ArrayList<com.creativelair.handyphone.Contacts> doInBackground(Void... params) {
            ArrayList<com.creativelair.handyphone.Contacts> contacts = new ArrayList<>();
            Cursor c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);

            while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Bitmap photo = null;
                //Blo = c.getBlob(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                long contactID=c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
               int img= c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));



               // photo = BitmapFactory.decodeStream(img);
               System.out.println(img);
                System.out.println(contactName );
                Bitmap bitmap = queryContactImage(img);
                img=0;
/*

                try {

                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

                  if (inputStream != null) {
                      System.out.println("Decoding");
                      System.out.println(inputStream.toString());
                      photo = BitmapFactory.decodeStream(inputStream);

                   }

                 //   assert null != inputStream;
                    if (inputStream != null){

                    inputStream.close();}

                } catch (IOException e) {
                    e.printStackTrace();
                }

 */
                com.creativelair.handyphone.Contacts contacts1=null;
                if(bitmap !=null )
                {
                    contacts1 = new com.creativelair.handyphone.Contacts(contactName,phNumber,bitmap);
                }
                else
                {
                    contacts1 = new com.creativelair.handyphone.Contacts(contactName,phNumber,null);

                }

                contacts.add(contacts1);

            }
            c.close();

            return contacts;
        }
        private Bitmap queryContactImage(int imageDataRow) {
            Cursor c = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
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
        protected void onPostExecute(ArrayList<com.creativelair.handyphone.Contacts> contacts) {
            super.onPostExecute(contacts);
            ContactListAdapter adapter = new ContactListAdapter(getContext(), contacts);
            listView.setAdapter(adapter);
        }

    }


}