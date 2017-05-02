package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AddFoodTask extends BaseTask {

    private String icon;
    private String description;
    private String amount;

    public AddFoodTask(BaseFragment fragment, String icon, String description, String amount) {

        this.activity = fragment.getBaseActivity();
        this.icon = icon;
        this.description = description;
        this.amount = amount;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog(R.string.adding_food);
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", appCommon.getUser().getId()+"");
        parameters.put("meeting_id", appCommon.getSelectedMeeting().getId()+"");
        parameters.put("icon", icon);
        parameters.put("description", description);
        parameters.put("amount", amount);

        response = requester.httpRequest("AddFood.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0:
                        activity.showSimpleDialog(R.string.error_adding_food);
                        break;
                    case 2:
                        new GetFoodTask(fragment).execute();
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}