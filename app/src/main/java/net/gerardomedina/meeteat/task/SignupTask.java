package net.gerardomedina.meeteat.task;

import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.view.activity.BaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SignupTask extends BaseTask {

    private final String username;
    private final String password;
    private final String email;

    public SignupTask(BaseActivity activity, String username, String password, String email) {
        this.activity = activity;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);
        parameters.put("email",email);

        response = requester.httpRequest("SignupUser.php", "POST", parameters);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: activity.showToast(R.string.error_taken_email);
                            break;
                    case 2: activity.login(response.getInt("id"),response.getString("username"));
                            break;
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}