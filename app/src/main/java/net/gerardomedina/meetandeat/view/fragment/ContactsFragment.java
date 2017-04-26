package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.persistence.local.ContactHelper;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.task.DeleteContactsTask;
import net.gerardomedina.meetandeat.task.GetContactsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends BaseFragment {
    private ListView contactList;
    private SearchView searchView;
    private ArrayList<String> contacts;
    private SQLiteOpenHelper dbHelper;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactList = (ListView) view.findViewById(R.id.contacts);

        searchView = (SearchView) view.findViewById(R.id.contacts_searchbox);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        dbHelper = new ContactHelper(getActivity());
        if (appCommon.hasInternet(getActivity())) {
            new GetContactsTask(this,appCommon.getUser().getId()).execute();
        } else {
            populateContactListFromLocalDB();
        }
        return view;
    }

    private void populateContactListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ContactValues._ID, ContactValues.COLUMN_NAME_USERNAME};
        String sortOrder = ContactValues.COLUMN_NAME_USERNAME + " ASC";
        Cursor cursor = db.query(
                ContactValues.TABLE_NAME, projection, null, null, null, null,
                sortOrder);
        contacts = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME)));
        }
        cursor.close();
        contactList.setAdapter(new ContactsAdapter(getActivity(),contacts));
    }

    public void populateContactListFromRemoteWS(JSONObject response) throws JSONException {
        contacts = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            contacts.add(results.getJSONObject(i).getString("username"));
        }
        contactList.setAdapter(new ContactsAdapter(getActivity(),contacts));
        saveContactListToLocalDB(contacts);
    }

    private void saveContactListToLocalDB(List<String> contacts) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ContactValues.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (String contact: contacts) {
            values.put(ContactValues.COLUMN_NAME_USERNAME, contact);
            db.insert(ContactValues.TABLE_NAME, null, values);
        }
    }


    private class ContactsAdapter extends ArrayAdapter<String> {
        ContactsAdapter(Context context, ArrayList<String> contacts) {
            super(context, 0, contacts);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final String contactUsername = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts_item, parent, false);
            }

            TextView username = (TextView) convertView.findViewById(R.id.contactLabel);
            username.setText(contactUsername);

            ImageView delete = (ImageView) convertView.findViewById(R.id.deleteContactButton);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appCommon.hasInternet(getActivity())) {
                        new DeleteContactsTask(getBaseFragment(),appCommon.getUser().getId(),contactUsername).execute();
                    } else ((BaseActivity)getActivity()).showToast(getString(R.string.no_internet_connection));
                }
            });

            return convertView;
        }
    }
}
