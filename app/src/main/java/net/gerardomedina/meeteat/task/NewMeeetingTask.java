package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.activity.BaseActivity;
import net.gerardomedina.meeteat.view.activity.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class NewMeeetingTask extends BaseTask {

    private String title;
    private String location;
    private String datetime;
    private String color;
    private String participants;

    public NewMeeetingTask(BaseActivity activity, String title, String location,
                           String datetime, String color, String participants) {
        this.activity = activity;
        this.title = title;
        this.location = location;
        this.datetime = datetime;
        this.color = color;
        this.participants = participants;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId()+"");
        parameters.put("title", title);
        parameters.put("location", location);
        parameters.put("datetime", datetime);
        parameters.put("color", color);
        parameters.put("participants", participants);

        response = requester.httpRequest("NewMeeting.php", "POST", parameters);
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
                        activity.changeToActivityNoBackStack(MainActivity.class);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}