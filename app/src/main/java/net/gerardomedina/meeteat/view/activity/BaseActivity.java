package net.gerardomedina.meeteat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.model.User;
import net.gerardomedina.meeteat.persistence.local.DBHelper;

public abstract class BaseActivity extends AppCompatActivity {

    public AppCommon appCommon = AppCommon.getInstance();

    public BaseActivity getBaseActivity() {return this;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCommon = AppCommon.getInstance();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void login(int id, String username) {
        appCommon.setUser(new User(id, username));
        appCommon.sharedSetValue(this,"id",id);
        appCommon.sharedSetValue(this,"username",username);
        changeToActivity(MainActivity.class);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    public void loginWithPrevValues(int id) {
        appCommon.setUser(new User(id, (String)appCommon.sharedGetValue(getApplicationContext(), "username", 1)));
        changeToActivity(MainActivity.class);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    public void changeToActivity(Class activity) {
        this.startActivity(new Intent(this, activity));
        this.overridePendingTransition(0,0);
    }

    public void changeToActivityNoBackStack(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void showSimpleDialog(int stringId) {
        new android.support.v7.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setMessage(getString(stringId))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void showToast(int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show();
    }

    public void logout() {
        appCommon.sharedRemoveValue(this,"id");
        appCommon.setUser(null);
        appCommon.setInvitations(null);
        appCommon.setSelectedMeeting(null);
        this.deleteDatabase(DBHelper.DATABASE_NAME);
        getBaseActivity().changeToActivityNoBackStack(LoginActivity.class);
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        this.finish();
    }
}

