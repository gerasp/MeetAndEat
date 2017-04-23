package net.gerardomedina.meetandeat.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import net.gerardomedina.meetandeat.persistence.Requester;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

import org.json.JSONObject;

public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {
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
        progressDialog.show();
    }
}
