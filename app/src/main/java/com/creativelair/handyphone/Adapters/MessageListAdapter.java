package com.creativelair.handyphone.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Message;
import com.creativelair.handyphone.R;

import java.util.ArrayList;

/**
 * Created by AHSAN on 4/9/2017.
 */

public class MessageListAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<Message> messages;
    private LayoutInflater inflater;

    public MessageListAdapter(Context context, int resource, ArrayList<Message> objects) {
        super(context, resource, objects);
        mContext = context;
        messages = objects;
        Toast.makeText(context, messages.size() + " " + objects.size(), Toast.LENGTH_SHORT).show();

        log(messages, objects);

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private void log(ArrayList<Message> messages, ArrayList<Message> objects) {
        for (Message msg : messages) {
            Log.d("Ye dekh", msg.getHeader());
        }

        for (Message msg : objects) {
            Log.d("Ye bhi dekh", msg.getHeader());
        }

    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Message msg = messages.get(position);
        if (convertView == null)
            vi = inflater.inflate(R.layout.messagedialoglist, null);
        Typeface RobotoBlack = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Black.ttf");
        Typeface Roboto = Typeface.createFromAsset(mContext.getAssets(), "fonts/Raleway-Regular.ttf");
        TextView header = (TextView) vi.findViewById(R.id.header);
        TextView text = (TextView) vi.findViewById(R.id.text);

        header.setText(msg.getHeader());
        text.setText(msg.getText());
        header.setTypeface(RobotoBlack);
        text.setTypeface(Roboto);


        return vi;
    }
}
