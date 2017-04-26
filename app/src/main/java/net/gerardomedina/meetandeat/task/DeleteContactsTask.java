package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class DeleteContactsTask extends BaseTask {

    private final int userId;
    private final String user2Username;

    public DeleteContactsTask(BaseFragment fragment, int userId, String user2Username) {
        this.fragment = fragment;
        this.activity = (BaseActivity)fragment.getActivity();
        this.userId = userId;
        this.user2Username = user2Username;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.delete_contacts_dialog);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id",userId+"");
        parameters.put("user2_username", user2Username +"");

        response = requester.httpRequest("DeleteContact.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        progressDialog.cancel();
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(activity.getString(R.string.error_deleting_contacts));
                            break;
                    case 2: new GetContactsTask(fragment,appCommon.getUser().getId()).execute();
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}