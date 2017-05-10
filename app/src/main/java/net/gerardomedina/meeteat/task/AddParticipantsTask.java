package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.activity.BaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AddParticipantsTask extends BaseTask {

    private final String participants;

    public AddParticipantsTask(BaseActivity activity, String participants) {
        this.activity = activity;
        this.participants = participants;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("participants", participants);
        parameters.put("meeting_id", appCommon.getSelectedMeeting().getId()+"");

        response = requester.httpRequest("AddParticipants.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0:
                        activity.showSimpleDialog(R.string.error_retrieving_data);
                        break;
                    case 2:
                        activity.showToast(R.string.participants_invited_successfully);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}