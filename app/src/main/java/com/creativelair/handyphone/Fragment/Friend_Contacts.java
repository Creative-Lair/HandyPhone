package com.creativelair.handyphone.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativelair.handyphone.Adapters.ContactListAdapter;
import com.creativelair.handyphone.GridSpacingItemDecoration;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.Helpers.SQLiteHandler;
import com.creativelair.handyphone.R;
import com.creativelair.handyphone.RecyclerItemClickListener;
import com.creativelair.handyphone.Screens.AddContact;

import java.util.ArrayList;

public class Friend_Contacts extends Fragment implements View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static LayoutInflater inflat = null;
    RecyclerView listView;
    FloatingActionButton fb;
    Preference preference;
    ArrayList<Contacts> allcontacts;
    SQLiteHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_contacts, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        fb = (FloatingActionButton) view.findViewById(R.id.add);
        db = new SQLiteHandler(getActivity());
        fb.setOnClickListener(this);
        allcontacts = new ArrayList<>();
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

                        Contacts contacts = allcontacts.get(position);

                        MyDialog myDialog = new MyDialog(contacts, preference.getColor());
                        myDialog.show(getActivity().getFragmentManager(), "my_dialog");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        loadContacts();
        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void loadContacts() {
        allcontacts = db.getFriend();
        ContactListAdapter adapter = new ContactListAdapter(getActivity(), allcontacts);
        listView.setAdapter(adapter);

    }



    @Override
    public void onResume() {
        if(preference.getLoad()) {
            allcontacts.clear();
            allcontacts = db.getFriend();
            ContactListAdapter adapter = new ContactListAdapter(getContext(), allcontacts);
            listView.setAdapter(adapter);
        }
        super.onResume();
    }

    @Override
    public void onPause(){
        allcontacts.clear();
        super.onPause();
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


}