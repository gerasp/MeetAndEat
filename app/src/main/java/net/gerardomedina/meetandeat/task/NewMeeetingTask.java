package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class NewMeeetingTask extends BaseTask {

    private String title;
    private String location;
    private String date;
    private String time;
    private String color;

    public NewMeeetingTask(BaseActivity activity, String title, String location, String date, String time, String color) {
        this.activity = activity;
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.color = color;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.creating_meeting_dialog);
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId()+"");
        parameters.put("title", title);
        parameters.put("location", location);
        parameters.put("date", date);
        parameters.put("time", time);
        parameters.put("color", color);

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
                        activity.showSimpleDialog(activity.getString(R.string.error_creating_meeting));
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