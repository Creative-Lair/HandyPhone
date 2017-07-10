package com.creativelair.handyphone.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;

import java.util.ArrayList;

import static com.creativelair.handyphone.R.id.imageView;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    ArrayList<Contacts> contacts;
    String[] Mcolors = {"#11303f", "#d9dd5f", "#fd1254", "#88abb2"};
    int colorIndex = 0;
    String color;
    private Context mContext;
    private ArrayList<Contacts> display;
    private Preference pref;

    public ContactListAdapter(Context mContext, ArrayList<Contacts> contacts) {
        this.mContext = mContext;
        this.contacts = new ArrayList<>();
        this.display = new ArrayList<>();
        this.contacts.addAll(contacts);
        this.display.addAll(contacts);
        pref = new Preference(mContext);
    }

    public ArrayList<Contacts> getList() {
        return display;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return display.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Typeface RobotoBlack = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Black.ttf");
        Typeface Roboto = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        Contacts contact = display.get(position);
        holder.name.setText(contact.getName());
        holder.name.setTypeface(Roboto);
        if (contact.getPic() != null) {
                holder.img.setImageBitmap(contact.getPic());
                holder.initial.setText("");
        }
        else if(contact.getIcon()!=null){
            if(!contact.getIcon().equals("")) {
                Glide.with(mContext).load(contact.getIcon()).into(holder.img);
                //holder.img.setImageBitmap(BitmapFactory.decodeFile(contact.getIcon()));
                holder.initial.setText("");
            } else {
                pref.setColor(Mcolors[colorIndex % 4]);
                color = pref.getColor();
                contact.setColor(color);
                holder.img.setImageDrawable(new ColorDrawable(Color.parseColor(color)));
                colorIndex++;
                holder.initial.setText("" + holder.name.getText().toString().toUpperCase().charAt(0));
                holder.initial.setTypeface(RobotoBlack);
            }
        } else{
                pref.setColor(Mcolors[colorIndex % 4]);
                color = pref.getColor();
                contact.setColor(color);
                holder.img.setImageDrawable(new ColorDrawable(Color.parseColor(color)));
                colorIndex++;
                holder.initial.setText("" + holder.name.getText().toString().toUpperCase().charAt(0));
                holder.initial.setTypeface(RobotoBlack);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void filterData(String query) {
        query = query.toLowerCase();
        display.clear();

        if (query.isEmpty()) {
            display.addAll(contacts);
        } else {
            for (Contacts contact : contacts) {
                if (contact.getName().toLowerCase().contains(query)) {
                    display.add(contact);
                }
            }
        }
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, initial;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.icon);
            initial = (TextView) view.findViewById(R.id.initial);
        }
    }



}
