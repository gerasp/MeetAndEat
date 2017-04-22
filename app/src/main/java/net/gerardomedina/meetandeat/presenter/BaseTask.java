package net.gerardomedina.meetandeat.presenter;

import android.os.AsyncTask;

import net.gerardomedina.meetandeat.model.Requester;

import org.json.JSONObject;

public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {
    Requester requester = new Requester();
    JSONObject response;
}
