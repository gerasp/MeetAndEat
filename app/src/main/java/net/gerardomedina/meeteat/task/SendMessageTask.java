package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.fragment.BaseFragment;

import org.json.JSONException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendMessageTask extends BaseTask {

    private String content;

    public SendMessageTask(BaseFragment fragment, String content) {

        this.activity = fragment.getBaseActivity();
        this.content = content;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", appCommon.getUser().getUsername());
        parameters.put("timestamp", (int) (new Date().getTime()/1000) + "");
        parameters.put("meeting_id", appCommon.getSelectedMeeting().getId() + "");
        parameters.put("content", content);

        response = requester.httpRequest("SendMessage.php", "POST", parameters);
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
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}