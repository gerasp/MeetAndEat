package net.gerardomedina.meetandeat.presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Requester;
import net.gerardomedina.meetandeat.view.BaseActivity;

import org.json.JSONObject;

public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {
    Requester requester = new Requester();
    JSONObject response;
    ProgressDialog progressDialog;
    BaseActivity activity;

    void showProgressDialog(int messageId) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(messageId));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
