package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.ChatFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetMessagesTask extends BaseTask {

    private BaseFragment fragment;

    public GetMessagesTask(BaseFragment fragment) {
        this.activity = fragment.getBaseActivity();
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.getting_messages);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("meeting_id",appCommon.getSelectedMeeting().getId()+"");

        response = requester.httpRequest("GetMessages.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(R.string.error_retrieving_data);
                            break;
                    case 2: ((ChatFragment)fragment).populateMessageList(response);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}