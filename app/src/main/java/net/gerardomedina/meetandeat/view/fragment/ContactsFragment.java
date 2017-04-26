package net.gerardomedina.meetandeat.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.GetContactsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class ContactsFragment extends BaseFragment {
    private View view;
    private ListView contactList;
    private SearchView searchView;
    private ArrayList<String> contacts;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactList = (ListView)view.findViewById(R.id.contacts);
        contacts = new ArrayList<>();
        contactList.setAdapter(new ContactsAdapter(getActivity(),contacts));

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

        return view;
    }



    class ContactsAdapter extends ArrayAdapter<String> {
        ContactsAdapter(Context context, ArrayList<String> contacts) {
            super(context, 0, contacts);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final String string = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts_item, parent, false);
            }
            TextView username = (TextView) convertView.findViewById(R.id.contact_label);
            username.setText(string);
            return convertView;
        }
    }
}
