package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MeetingActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.FoodFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetFoodTask extends BaseTask {

    public GetFoodTask(BaseFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getBaseActivity();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        showProgressDialog(R.string.getting_food);
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("meeting_id", appCommon.getSelectedMeeting().getId()+"");

        response = requester.httpRequest("GetFood.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 2:
                        ((FoodFragment)fragment).hideRefreshIndicator();
                        ((FoodFragment)fragment).populateFoodTable(response);
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}