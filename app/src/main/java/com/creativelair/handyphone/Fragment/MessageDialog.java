package com.creativelair.handyphone.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.creativelair.handyphone.Adapters.MessageListAdapter;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Message;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;

import java.util.ArrayList;


public class MessageDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private LayoutInflater inflater;
    private View view;
    private Contacts contacts;
    private ListView lv;
    private ArrayList<Message> messages;
    private Preference preference;
    private Context mContext;

    public MessageDialog(Contacts contacts) {
        super();
        this.contacts = contacts;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.messagedialog, null);
        preference = new Preference(getActivity());

        lv = (ListView) view.findViewById(R.id.lv);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Message msg1 = new Message("Meeting", "temp1");
        Message msg2 = new Message("heading2", "temo2");
        Message msg3 = new Message("heading3", "temp3");
        Message msg4 = new Message("heading4", "temp4");
        Message msg5 = new Message("heading5", "temp5");

        messages = new ArrayList<>();

        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);
        messages.add(msg4);
        messages.add(msg5);

        MessageListAdapter adapter = new MessageListAdapter(getActivity(), R.layout.messagedialoglist, messages);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        return builder.create();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Message msg = messages.get(position);

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", contacts.getNumber());
        smsIntent.putExtra("sms_body", msg.getText());
        startActivity(smsIntent);


    }

    @Override
    public void onResume() {

        getDialog().setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
