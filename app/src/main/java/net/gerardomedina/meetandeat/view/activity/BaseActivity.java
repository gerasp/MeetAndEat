package net.gerardomedina.meetandeat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.model.User;

public abstract class BaseActivity extends AppCompatActivity {

    AppCommon appCommon = AppCommon.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void login(int id, String username) {
        appCommon.setUser(new User(id, username));
        appCommon.sharedSetValue(this,"id",id);
        appCommon.sharedSetValue(this,"username",username);
        changeToActivity(MainActivity.class);
    }

    public void loginWithPrevValues(int id) {
        appCommon.setUser(new User(id, (String)appCommon.sharedGetValue(getApplicationContext(), "username", 1)));
        changeToActivity(MainActivity.class);
    }

    public void changeToActivity(Class activity) {
        this.startActivity(new Intent(this, activity));
        this.overridePendingTransition(0,0);

    }

    public void showSimpleDialog(String message) {
        new android.support.v7.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

