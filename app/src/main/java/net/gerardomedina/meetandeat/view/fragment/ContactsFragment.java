package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.persistence.local.MeetingValues;
import net.gerardomedina.meetandeat.task.GetContactsTask;
import net.gerardomedina.meetandeat.view.adapter.ContactsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactsFragment extends BaseFragment {
    private ListView contactListView;
    private SearchView searchView;
    private ArrayList<String> contacts;
    private SQLiteOpenHelper dbHelper;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactListView = (ListView) view.findViewById(R.id.contacts);

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

        dbHelper = new DBHelper(getActivity());
        return view;
    }

    public void init() {
        if (appCommon.hasInternet(getActivity())) new GetContactsTask(this).execute();
        else loadContactListFromLocalDB();
    }

    public void saveContactListToLocalDB(JSONObject response) throws JSONException {
        contacts = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ContactValues.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < results.length(); i++) {
            values.put(ContactValues.COLUMN_NAME_USERNAME, results.getJSONObject(i).getString("username"));
            db.insert(ContactValues.TABLE_NAME, null, values);
        }
        loadContactListFromLocalDB();
    }

    private void loadContactListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select "+ContactValues.COLUMN_NAME_USERNAME+" from "+
                ContactValues.TABLE_NAME+" order by "
                +ContactValues.COLUMN_NAME_USERNAME+" ASC;",null);
        contacts = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME)));
        }
        cursor.close();
        contactListView.setAdapter(new ContactsAdapter(this, getActivity(),contacts));
    }


}
