package net.gerardomedina.meeteat.task;

import android.os.AsyncTask;
import android.util.Log;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.persistence.remote.Requester;
import net.gerardomedina.meeteat.view.activity.BaseActivity;
import net.gerardomedina.meeteat.view.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {
    AppCommon appCommon = AppCommon.getInstance();
    Requester requester = new Requester();
    JSONObject response;
    BaseActivity activity;
    BaseFragment fragment;

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if(fragment != null) fragment.stopRefreshing();
        try {
            if (!success || response.getInt("code") < 0 || response.getInt("code") > 2) activity.showSimpleDialog(R.string.error_retrieving_data);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data: " + e.toString());
        }
    }
}
