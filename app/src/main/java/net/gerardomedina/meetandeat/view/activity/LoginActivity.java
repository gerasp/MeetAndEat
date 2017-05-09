package net.gerardomedina.meetandeat.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.LoginTask;
import net.gerardomedina.meetandeat.task.RememberPasswordTask;
import net.gerardomedina.meetandeat.task.SignupTask;


public class LoginActivity extends BaseActivity {

    private EditText usernameView;
    private EditText passwordView;
    private Handler handler;
    private Runnable runnable;

    private CarouselView carouselView;

    private int[] sampleImages = {R.drawable.carousel1,
            R.drawable.carousel2,
            R.drawable.carousel3,
            R.drawable.carousel4,
            R.drawable.carousel5};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout loginForm = (LinearLayout) findViewById(R.id.email_login_form);
        loginForm.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));

        setCarouselView();
        setInputs();

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void setInputs() {
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                attemptLogin();
                return true;
            }
        });
        TextView rememberPasswordButton = (TextView) findViewById(R.id.rememberPasswordButton);
        rememberPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRememberPasswordDialog();
            }
        });
    }

    private void setCarouselView() {
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });
    }

    private void attemptLogin() {

        usernameView.setError(null);
        passwordView.setError(null);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameView.setError(getString(R.string.error_invalid_username));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) focusView.requestFocus();
        else new LoginTask(this, username, password).execute();
    }

    private boolean isUsernameValid(String username) {
        return username.length() > 5 && !username.contains(" ");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5 && !password.contains(" ");
    }

    public void showEmailDialog(final String username, final String password) {
        final BaseActivity activity = this;
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setMessage(getString(R.string.request_email));
        alertDialog.setView(input);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (appCommon.isEmailValid(input.getText().toString())) {
                    dialog.dismiss();
                    new SignupTask(activity, username, password, input.getText().toString()).execute((Void) null);
                } else {
                    showEmailDialog(username, password);
                    showToast(R.string.error_invalid_email);
                }
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showRememberPasswordDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText input = new EditText(this);
        input.setText(usernameView.getText().toString());
        alertDialog.setMessage(getString(R.string.request_username));
        alertDialog.setView(input);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new RememberPasswordTask(getBaseActivity(),input.getText().toString()).execute();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
            handler = null;
        }
    }


}

