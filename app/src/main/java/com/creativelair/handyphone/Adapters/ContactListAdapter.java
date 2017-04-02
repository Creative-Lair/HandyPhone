package com.creativelair.handyphone.Adapters;

import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativelair.handyphone.Contacts;
import com.creativelair.handyphone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AHSAN on 4/2/2017.
 */

public class ContactListAdapter extends ArrayAdapter {

    private Context mContext;
    ArrayList<Contacts> contacts;


    public ContactListAdapter(Context context,int resource, ArrayList<Contacts> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.contacts = objects;

    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        convertView = inflater.inflate(R.layout.contact_list_item, parent, false);

        ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        TextView tv = (TextView) convertView.findViewById(R.id.name);

        //iv.setImageDrawable();
        tv.setText(contacts.get(position).getName());

        return convertView;
    }

}
