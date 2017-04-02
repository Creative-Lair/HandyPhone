package com.creativelair.handyphone;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.Toast;

public class All_Contacts extends Fragment implements
        LoaderCallbacks<Cursor>{

  //  private OnContactSelectedListener mContactsListener;
    private SimpleCursorAdapter mAdapter;
    private String mCurrentFilter = null;

    ListView listView;

    String TAG = getClass().getSimpleName();

    private static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            Contacts._ID,
            Contacts.DISPLAY_NAME,
            Contacts.HAS_PHONE_NUMBER,
            Contacts.LOOKUP_KEY
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_contacts, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        getLoaderManager().initLoader(0, null, this);

        mAdapter = new IndexedListAdapter(
                this.getActivity(),
                R.layout.fragment_frequent,
                null,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                new int[] {R.id.list});

        listView.setAdapter(mAdapter);
        listView.setFastScrollEnabled(true);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnContactSelectedListener");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri baseUri;

        if (mCurrentFilter != null) {
            baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                    Uri.encode(mCurrentFilter));
        } else {
            baseUri = Contacts.CONTENT_URI;
        }

        String selection = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Log.v(TAG, "Name = "+ContactsContract.Contacts.DISPLAY_NAME);

        return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECTION, selection, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    class IndexedListAdapter extends SimpleCursorAdapter implements SectionIndexer{

        AlphabetIndexer alphaIndexer;

        public IndexedListAdapter(Context context, int layout, Cursor c,
                                  String[] from, int[] to) {
            super(context, layout, c, from, to, 0);
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            if (c != null) {
                alphaIndexer = new AlphabetIndexer(c,
                        c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME),
                        " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                Log.v(TAG, "data = "+ c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            return super.swapCursor(c);
        }

        @Override
        public int getPositionForSection(int section) {
            return alphaIndexer.getPositionForSection(section);
        }

        @Override
        public int getSectionForPosition(int position) {
            return alphaIndexer.getSectionForPosition(position);
        }

        @Override
        public Object[] getSections() {
            return alphaIndexer == null ? null : alphaIndexer.getSections();
        }
    }
}