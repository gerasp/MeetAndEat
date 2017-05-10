package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.view.activity.BaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetParticipantsTask extends BaseTask {

    public GetParticipantsTask(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("meeting_id", appCommon.getSelectedMeeting().getId()+"");

        response = requester.httpRequest("GetParticipants.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        appCommon.getSelectedMeeting().setParticipants(response);
                        appCommon.getSelectedMeeting().setAdmin(response.getString("admin"));
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}