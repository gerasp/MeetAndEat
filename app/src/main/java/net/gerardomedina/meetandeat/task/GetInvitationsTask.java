package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetInvitationsTask extends BaseTask {

    public GetInvitationsTask(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.getting_invitations);
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId()+"");

        response = requester.httpRequest("GetInvitations.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        ((MainActivity)activity).setBadgeCount(activity,response.getJSONArray("results").length()+"");
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}