package net.gerardomedina.meetandeat.task;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.persistence.remote.Requester;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

import org.json.JSONObject;

public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {
    AppCommon appCommon = AppCommon.getInstance();
    Requester requester = new Requester();
    JSONObject response;
    ProgressDialog progressDialog;
    BaseActivity activity;
    BaseFragment fragment;

    void showProgressDialog(int messageId) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(messageId));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requester.cancel();
                dialog.dismiss();
            }
        });
        progressDialog.show();
    }
}
