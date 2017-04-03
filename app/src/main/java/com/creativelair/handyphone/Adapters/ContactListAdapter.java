package com.creativelair.handyphone.Adapters;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.R;

import java.util.ArrayList;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<Contacts> contacts;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.icon);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_list_item, parent, false);



        return new MyViewHolder(itemView);
    }


    public ContactListAdapter(Context mContext, ArrayList<Contacts> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Contacts contact = contacts.get(position);
        holder.name.setText(contact.getName());
        if(contact.getIcon()!=null) {
            holder.img.setImageBitmap(contact.getIcon());

        }

    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

}
