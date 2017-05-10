package net.gerardomedina.meeteat.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.task.SearchTask;
import net.gerardomedina.meeteat.view.adapter.ContactAdapter;
import net.gerardomedina.meeteat.view.fragment.BaseFragment;
import net.gerardomedina.meeteat.view.fragment.ContactsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchContactsDialog extends AlertDialog {
    private final BaseFragment fragment;
    private final Activity activity;
    AppCommon appCommon = AppCommon.getInstance();
    private ListView resultList;


    public SearchContactsDialog(BaseFragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public AlertDialog getDialog() {
        return this;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultList = (ListView) findViewById(R.id.resultList);
        SearchView searchView = (SearchView) findViewById(R.id.contactsSearchBox);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new SearchTask(getDialog(),fragment, s).execute();
                appCommon.hideKeyboard(activity);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.contains(" ")) fragment.showToast(R.string.no_spaces_allowed);
                return false;
            }
        });
    }


    public void populateWithSearchResults(JSONObject response) throws JSONException {

        List<String> contacts = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            contacts.add(results.getJSONObject(i).getString("username"));
        }
        resultList.setAdapter(new ContactAdapter((ContactsFragment) fragment, activity, contacts, true));
    }

}
