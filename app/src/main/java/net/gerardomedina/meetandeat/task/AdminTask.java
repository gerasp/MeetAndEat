package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AdminTask extends BaseTask {

    private final String parameter;
    private final int type;

    public AdminTask(BaseActivity activity, int type, String parameter) {
        this.activity = activity;
        this.type = type;
        this.parameter = parameter;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id",appCommon.getUser().getId()+"");
        parameters.put("meeting_id",appCommon.getSelectedMeeting().getId()+"");
        parameters.put("type", type+"");
        parameters.put("parameter", parameter);

        response = requester.httpRequest("Admin.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(R.string.error);
                            break;
                    case 2: activity.showSimpleDialog(R.string.done);
                            activity.changeToActivityNoBackStack(MainActivity.class);
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}