package com.creativelair.handyphone.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import com.creativelair.handyphone.Adapters.MessageListAdapter;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Message;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;

import java.util.ArrayList;


public class MessageDialog extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private LayoutInflater inflater;
    private View view;
    private Contacts contacts;
    private ListView lv;
    private ArrayList<Message> messages;
    private Preference preference;
    private Context mContext;
    private Button Add;
    private ScrollView scrollLayout;

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
        Add = (Button) view.findViewById(R.id.add);
        scrollLayout = (ScrollView) view.findViewById(R.id.relativelayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Message msg1 = new Message("Meeting", "I'm in a meeting. I'll call you back.");
        Message msg2 = new Message("Driving", "Sorry, I'm driving.");
        Message msg3 = new Message("Urgent", "It's urgent, Call back as soon as possible.");
        Message msg4 = new Message("Busy", "Can't talk now. Call me later?");
        Message msg5 = new Message("Busy 2", "Can't talk now. Will call later.");
        Message msg6 = new Message("Class", "I'm in a class.");
        Message msg7 = new Message("Late", "I'm going to be late tonight.");

        messages = new ArrayList<>();
        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);
        messages.add(msg4);
        messages.add(msg5);
        messages.add(msg6);
        messages.add(msg7);

        MessageListAdapter adapter = new MessageListAdapter(getActivity(), R.layout.messagedialoglist, messages);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

//        Add.setOnClickListener(this);
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
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

    /*
        MessageListAdapter adapter = new MessageListAdapter(getActivity(), R.layout.messagedialoglist, messages);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        String head = preference.getHeading().toString();
        String messg = preference.getMsg().toString();
        Message msg7 = new Message(head, messg);
        messages.add(msg7);
        Add.setOnClickListener(this);
        preference.setHeading("");
        preference.setMsg("");
    */
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.add:

                /*Intent i = new Intent(getActivity(), AddNewMsg.class);
                startActivity(i);   */
                break;
        }
    }
}
