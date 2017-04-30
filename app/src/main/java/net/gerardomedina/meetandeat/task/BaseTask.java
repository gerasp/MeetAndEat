package net.gerardomedina.meetandeat.task;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.persistence.remote.Requester;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {
    AppCommon appCommon = AppCommon.getInstance();
    Requester requester = new Requester();
    JSONObject response;
    ProgressDialog progressDialog;
    BaseActivity activity;
    BaseFragment fragment;
    private int messageId;

    void showProgressDialog(int messageId) {
        this.messageId = messageId;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(this.messageId));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCancelled();
                dialog.dismiss();
            }
        });
        progressDialog.show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        activity.showSimpleDialog(R.string.error_retrieving_data);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (progressDialog!= null)progressDialog.cancel();
        try {
            if (!success || response.getInt("code") < 0 || response.getInt("code") > 2) activity.showSimpleDialog(R.string.error_retrieving_data);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data: " + e.toString());
        }
    }
}
