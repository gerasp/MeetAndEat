package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AddContactTask extends BaseTask {

    private final String user2Username;

    public AddContactTask(BaseFragment fragment, String user2Username) {
        this.fragment = fragment;
        this.activity = (BaseActivity)fragment.getActivity();
        this.user2Username = user2Username;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.adding_contact);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id",appCommon.getUser().getId()+"");
        parameters.put("user2_username", user2Username +"");

        response = requester.httpRequest("AddContact.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(R.string.error_adding_contact);
                            break;
                    case 2: fragment.showToast(R.string.contact_added_successfully);
                            new GetContactsTask(fragment).execute();
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}