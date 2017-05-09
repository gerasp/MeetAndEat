package net.gerardomedina.meetandeat.task;

import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class AccountTask extends BaseTask{
    private final int type;
    private final String change;
    private final String password;


    public AccountTask(BaseActivity activity, int type, String change, String password) {
        this.activity = activity;
        this.type = type;
        this.change = change;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("user_id",appCommon.getUser().getId()+"");
        parameters.put("type", type+"");
        parameters.put("change", change);
        parameters.put("password", password);

        response = requester.httpRequest("Account.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showSimpleDialog(R.string.error_invalid_password);
                        break;
                    case 2:
                        activity.showToast(R.string.done);
                        if (type == 2) activity.logout();
                        break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}
