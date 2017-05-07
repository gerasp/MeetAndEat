package net.gerardomedina.meetandeat.task;

import android.app.AlertDialog;
import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.dialog.SearchContactsDialog;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SearchTask extends BaseTask {

    private String username;
    private AlertDialog dialog;

    public SearchTask(AlertDialog dialog, BaseFragment fragment, String username) {
        this.username = username;
        this.fragment = fragment;
        this.dialog = dialog;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("username",username);
        parameters.put("user_id",appCommon.getUser().getId()+"");

        response = requester.httpRequest("Search.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: fragment.showToast(R.string.no_user_found);
                            break;
                    case 2: ((SearchContactsDialog)dialog).populateWithSearchResults(response);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}