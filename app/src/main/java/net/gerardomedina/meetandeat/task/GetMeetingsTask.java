package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.DashboardFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetMeetingsTask extends BaseTask {


    public GetMeetingsTask(BaseFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getBaseActivity();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.get_meetings_dialog);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId() + "");

        response = requester.httpRequest("GetMeetings.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        ((DashboardFragment) fragment).saveMeetingListToLocalDB(response);
                        fragment.stopRefreshing();
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}