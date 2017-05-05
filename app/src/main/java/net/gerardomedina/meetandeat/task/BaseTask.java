package net.gerardomedina.meetandeat.task;

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
