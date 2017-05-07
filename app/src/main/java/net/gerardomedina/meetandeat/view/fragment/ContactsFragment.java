package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.task.GetContactsTask;
import net.gerardomedina.meetandeat.task.SearchTask;
import net.gerardomedina.meetandeat.view.adapter.ContactAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends BaseFragment implements InitiableFragment {
    private ListView contactListView;
    private List<String> contacts;
    private SQLiteOpenHelper dbHelper;
    private View view;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        init();
        return view;
    }

    public void init() {
        dbHelper = new DBHelper(getActivity());
        setContactList();
        setSearchView();
        loadContactListFromLocalDB();
        if (appCommon.hasInternet(getActivity())) new GetContactsTask(this).execute();
    }

    public void setContactList() {
        contactListView = (ListView) view.findViewById(R.id.contacts);
        contacts = new ArrayList<>();
        ContactAdapter contactsAdapter = new ContactAdapter(this, getBaseActivity(), contacts, false);
        contactListView.setAdapter(contactsAdapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetContactsTask(getBaseFragment()).execute();
            }
        });

    }

    public void setSearchView() {
        SearchView searchView = (SearchView) view.findViewById(R.id.contactsSearchBox);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new SearchTask(getBaseFragment(), s).execute();
                appCommon.hideKeyboard(getActivity());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) loadContactListFromLocalDB();
                if (s.contains(" ")) showToast(R.string.no_spaces_allowed);
                return false;
            }
        });
    }

    public void saveContactListToLocalDB(JSONObject response) throws JSONException {
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

    public void loadContactListFromLocalDB() {
        contacts.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ContactValues.COLUMN_NAME_USERNAME + " from " +
                ContactValues.TABLE_NAME + " order by "
                + ContactValues.COLUMN_NAME_USERNAME + " ASC;", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME)));
        }
        if (cursor.getCount() > 0) {
            contactListView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.noContent).setVisibility(View.GONE);
            contactListView.setAdapter(new ContactAdapter(this, getActivity(), contacts, false));
        } else {
            contactListView.setVisibility(View.GONE);
            view.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
        }
    }


    public void populateWithSearchResults(JSONObject response) throws JSONException {
        contacts.clear();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            contacts.add(results.getJSONObject(i).getString("username"));
        }
        contactListView.setAdapter(new ContactAdapter(this, getActivity(), contacts, true));
        contactListView.setVisibility(View.VISIBLE);
        view.findViewById(R.id.noContent).setVisibility(View.GONE);
    }

}
