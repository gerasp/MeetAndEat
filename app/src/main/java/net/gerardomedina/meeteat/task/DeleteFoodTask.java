package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.fragment.BaseFragment;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class DeleteFoodTask extends BaseTask {

    private final int foodId;

    public DeleteFoodTask(BaseFragment fragment, int foodId) {
        this.fragment = fragment;
        this.activity = fragment.getBaseActivity();
        this.foodId = foodId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("food_id", foodId +"");

        response = requester.httpRequest("DeleteFood.php", "POST", parameters);
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
                    case 2: new GetFoodTask(fragment).execute();
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}