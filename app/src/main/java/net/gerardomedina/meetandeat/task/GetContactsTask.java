package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.ContactsFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetContactsTask extends BaseTask {

    public GetContactsTask(BaseFragment fragment) {
        this.fragment = fragment;
        this.activity = (BaseActivity)fragment.getActivity();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.get_contacts_dialog);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id",appCommon.getUser().getId()+"");

        response = requester.httpRequest("GetContacts.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case -1: activity.showSimpleDialog(activity.getString(R.string.error_getting_contacts));
                            break;
                    case 2: ((ContactsFragment)fragment).saveContactListToLocalDB(response);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}