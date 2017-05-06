package net.gerardomedina.meetandeat.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Invitation;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.User;

import java.util.List;

public class AppCommon {

    private static AppCommon singleton;

    private User user;
    private Meeting selectedMeeting;
    private List<Invitation> invitations;

    public static AppCommon getInstance() {
        if (singleton != null) return singleton;
        else {
            singleton = new AppCommon();
            return singleton;
        }
    }

    public String getKey() {
        return "Df5f5z7e6W5pR2D2yEMK7Vkb77cp23nP";
    }
    public String getBaseURL() {
        return "http://192.168.1.32/ws/";
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Meeting getSelectedMeeting() {
        return selectedMeeting;
    }
    public void setSelectedMeeting(Meeting selectedMeeting) {
        this.selectedMeeting = selectedMeeting;
    }


    public boolean hasInternet(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        return i != null && i.isConnected() && i.isAvailable();
    }

    public boolean isEmailValid(CharSequence target) {
        return target != null && !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isColorDark(String colorString){
        int color = Color.parseColor(colorString);
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        return darkness >= 0.5;
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null && imm.isActive()) imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public Object sharedGetValue(Context context, String valueName, int objectType) {
        Object object = null;
        SharedPreferences confShared = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        if (objectType == 0) object = confShared.getBoolean(valueName, false);
        if (objectType == 1) object = confShared.getString(valueName, "");
        if (objectType == 2) object = confShared.getInt(valueName, 0);
        return object;
    }

    public void sharedSetValue(Context context, String sharedName, Object value) {
        SharedPreferences confShared 	   	= context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editShared = confShared.edit();
        if (value instanceof Boolean) editShared.putBoolean(sharedName, (Boolean) value);
        if (value instanceof Integer) editShared.putInt(sharedName, (Integer) value);
        if (value instanceof String) editShared.putString(sharedName, (String) value);
        editShared.apply();
    }

    public void sharedRemoveValue(Context context, String sharedName) {
        SharedPreferences confShared 	   	= context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editShared = confShared.edit();
        editShared.remove(sharedName);
        editShared.apply();
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }
}
