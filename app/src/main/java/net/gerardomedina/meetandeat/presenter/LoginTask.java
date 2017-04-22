package net.gerardomedina.meetandeat.presenter;

import android.app.ProgressDialog;
import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.BaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class LoginTask extends BaseTask {

    private final String username;
    private final String password;
    private final BaseActivity activity;
    private ProgressDialog progressDialog;

    public LoginTask(BaseActivity activity, String username, String password) {
        this.activity = activity;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.login_dialog));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);

        response = requester.httpRequest("LoginUser.php", "POST", parameters);

        // TODO: register the new account here if the account does not exist.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        progressDialog.cancel();
        if (success) {
            try {
                switch (response.getInt("code")) {
                    case 0: break;
                    case 1: activity.showSimpleDialog(activity.getString(R.string.error_incorrect_password));break;
                    case 2: activity.showEditTextDialog("YOU ARE LOGGED IN");break;
                    default:
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data: " + e.toString());
            }
        }
    }
}