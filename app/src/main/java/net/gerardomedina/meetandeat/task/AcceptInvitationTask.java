package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AcceptInvitationTask extends BaseTask {
    private int meetingId;
    private int accept;
    private int type;

    public AcceptInvitationTask(BaseActivity activity, int accept, int meetingId, int type) {
        this.activity = activity;
        this.meetingId = meetingId;
        this.accept = accept;
        this.type = type;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId() + "");
        parameters.put("meeting_id", meetingId + "");
        parameters.put("accept", accept + "");
        parameters.put("type", type + "");

        response = requester.httpRequest("AcceptInvitation.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        activity.changeToActivityNoBackStack(MainActivity.class);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}