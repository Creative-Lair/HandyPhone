package com.creativelair.handyphone;

/**
 * Created by HishamAhmed on 19-Mar-17.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frequent_Contacts extends Fragment {

    ListView outputText;
    List<String> userName;
    Map<String, String> userNamePhno;
    StringBuffer output;
    Dialog dialogDetails;
    String data = "";
    TextView tvname;
    CustomAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_items, container, false);
        return rootView;
    }
//        ListView result = (ListView) findViewById(R.id.ListView_Contacts)
/*
        Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(contacts.moveToNext()) {
            int index = contacts.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = contacts.getString(index);
            String num = contacts.getString(index);
            data = data + "\n" + name + "\n" + num;
        }
        tvname.setText(data);
*/

/*
        userNamePhno = new HashMap<String, String>();
        userName = new ArrayList<String>();
        new getContacts().execute();

        outputText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String[] details = new String[] { userName.get(arg2), userNamePhno.get(userName.get(arg2))};
                dialogDetails = new Dialog(Frequent_Contacts.this);
                dialogDetails.setContentView(R.layout.contact_details);
                dialogDetails.setTitle("Details");
                dialogDetails.show();

                TextView tvName = (TextView) dialogDetails.findViewById(R.id.name);
                TextView tvNumber = (TextView) dialogDetails.findViewById(R.id.phno);
                if (details[1]==null)
                {
                    tvName.setText(details[0]);
                    tvNumber.setText("-");
                }
                else if (details[2]==null)
                {
                    tvName.setText(details[0]);
                    tvNumber.setText(details[1]);
                }
                else if (details[2]==null && details[1]==null)
                {
                    tvName.setText(details[0]);
                    tvNumber.setText("-");
                }
                else
                {
                    tvName.setText(details[0]);
                    tvNumber.setText(details[1]);
                }
            }
        });
    }
*/

        class getContacts extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
//            dialog = new ProgressDialog(MainActivity.PlaceholderFragment.this, userName);
            dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setMessage("Wait please");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fetchContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            dialog.setCancelable(true);
//            adapter = new CustomAdapter(MainActivity.PlaceholderFragment.this, userName, userNamePhno);
            adapter = new CustomAdapter((MainActivity) getActivity(), userName, userNamePhno);
            outputText.setAdapter(adapter);
        }
    }
    private void fetchContacts() {
        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        output = new StringBuffer();

        ContentResolver contentResolver = getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor
                        .getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
                        .getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    output.append("\n First Name:" + name);
                    userName.add(name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(
                            PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
                            new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor
                                .getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                        // userPhoneNumber.add(phoneNumber);
                        userNamePhno.put(name, phoneNumber);

                    }
                    phoneCursor.close();
                }
                output.append("\n");
            }
        }
    }
}

class CustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private List<String> userName;
    private Map<String, String> userNamePhno;

    public CustomAdapter(MainActivity mainActivity, List<String> userName, Map<String, String> userNamePhno) {
        super(mainActivity, R.layout.contacts_items, userName);
        this.context=mainActivity;
        this.userName = userName;
        this.userNamePhno = userNamePhno;

    }
    static class ViewHolder {
        public TextView userName;
        public TextView userNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null){
            LayoutInflater inflater = context.getLayoutInflater();
            vi = inflater.inflate(R.layout.fragment_frequent, null);
            ViewHolder holder = new ViewHolder();
            holder.userName=(TextView) vi.findViewById(R.id.ListView_Contacts_Name);
            holder.userNumber=(TextView) vi.findViewById(R.id.ListView_Contacts_Phnum);
            vi.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) vi.getTag();
        holder.userName.setText("Name: "+userName.get(position).toUpperCase());
        holder.userNumber.setText("Ph No: "+userNamePhno.get(userName.get(position)));
        return vi;
    }
}