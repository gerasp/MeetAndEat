package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.model.Invitation;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetInvitationsTask extends BaseTask {

    public GetInvitationsTask(BaseActivity activity) {
        this.activity = activity;
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
                        ((MainActivity)activity).setBadgeCount(activity,
                                (response.getJSONArray("results").length()
                                        + response.getJSONArray("results2").length())+"");
                        List<Invitation> invitations = new ArrayList<>();
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            invitations.add(new Invitation(results.getJSONObject(i).getInt("id"),
                                    results.getJSONObject(i).getString("title"), 0));
                        }
                        JSONArray results2 = response.getJSONArray("results2");
                        for (int i = 0; i < results2.length(); i++) {
                            invitations.add(new Invitation(results2.getJSONObject(i).getInt("id"),
                                    results2.getJSONObject(i).getString("username"), 1));
                        }
                        appCommon.setInvitations(invitations);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}