package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.ChatFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetMessagesTask extends BaseTask {

    private BaseFragment fragment;

    public GetMessagesTask(BaseFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (fragment == null) return true;
        Map<String,String> parameters = new HashMap<>();
        parameters.put("meeting_id",appCommon.getSelectedMeeting().getId()+"");

        response = requester.httpRequest("GetMessages.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (fragment == null) return;
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2: ((ChatFragment)fragment).populateMessageList(response);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}