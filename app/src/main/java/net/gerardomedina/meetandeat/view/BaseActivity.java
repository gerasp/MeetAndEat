package net.gerardomedina.meetandeat.view;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import net.gerardomedina.meetandeat.R;

public abstract class BaseActivity extends AppCompatActivity {

    public void showSimpleDialog(String message) {
        new android.support.v7.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }


    public void showEditTextDialog(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText input = new EditText(this);
        alertDialog.setMessage(message);
        alertDialog.setView(input);
        alertDialog.show();
    }



}

