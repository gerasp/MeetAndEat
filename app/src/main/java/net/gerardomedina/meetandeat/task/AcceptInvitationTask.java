package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Invitation;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcceptInvitationTask extends BaseTask {
    private int meetingId;
    private int accept;

    public AcceptInvitationTask(BaseActivity activity, int accept, int meetingId) {
        this.activity = activity;
        this.meetingId = meetingId;
        this.accept = accept;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.accepting_invitation);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId() + "");
        parameters.put("meeting_id", meetingId +"");
        parameters.put("accept", accept +"");

        response = requester.httpRequest("AcceptInvitation.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 1:
                    case 2:
                        activity.changeToActivity(MainActivity.class);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}