package com.creativelair.handyphone;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.creativelair.handyphone.Adapters.ContactListAdapter;
import com.creativelair.handyphone.Fragment.MyDialog;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.SQLiteHandler;

import java.util.ArrayList;

public class Search extends Activity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static LayoutInflater inflat = null;
    private SearchManager searchManager;
    private SearchView searchView;
    private SQLiteHandler db;
    private ContactListAdapter listAdapter;
    private RecyclerView mList;
    private ArrayList<Contacts> contacts = new ArrayList<>();
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.seacrhview);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.requestFocus();
        contacts = new ArrayList<>();
        mList = (RecyclerView) findViewById(R.id.searchlist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getApplicationContext(), 2);
        mList.setLayoutManager(mLayoutManager);
        mList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mList.setItemAnimator(new DefaultItemAnimator());

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view == null) {
                            view = inflat.inflate(R.layout.all_contacts, null);
                        }

                        Contacts contact = contacts.get(position);

                        MyDialog myDialog = new MyDialog(contact);
                        myDialog.show(getFragmentManager(), "my_dialog");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        db = new SQLiteHandler(this);


        displayList();

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void displayList() {
        loadData();
    }

    @Override
    public boolean onClose() {
        listAdapter.filterData("");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filterData(newText);
        return false;
    }

    private void loadData() {
        contacts = db.getContactDetails();
        listAdapter = new ContactListAdapter(this, contacts);
        mList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }


}