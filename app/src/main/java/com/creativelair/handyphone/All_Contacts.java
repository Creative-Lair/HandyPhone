package com.creativelair.handyphone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.creativelair.handyphone.Adapters.ContactListAdapter;

import java.util.ArrayList;

public class All_Contacts extends Fragment{

    GridView listView;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_contacts, container, false);
        listView = (GridView) view.findViewById(R.id.list);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
           requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
       } else {
           LoadContactsAyscn loadContactsAyscn = new LoadContactsAyscn();
           loadContactsAyscn.execute();
       }

        return view;
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

    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<com.creativelair.handyphone.Contacts>> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected ArrayList<com.creativelair.handyphone.Contacts> doInBackground(Void... params) {
            // TODO Auto-generated method stub
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

                com.creativelair.handyphone.Contacts contacts1 = new com.creativelair.handyphone.Contacts(contactName,phNumber,null);

                contacts.add(contacts1);

            }
            c.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<com.creativelair.handyphone.Contacts> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);

            ContactListAdapter adapter = new ContactListAdapter(getContext(),R.layout.contact_list_item,contacts);

            listView.setAdapter(adapter);
        }

    }


}