package net.gerardomedina.meetandeat.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.LoginTask;
import net.gerardomedina.meetandeat.task.SignupTask;


public class LoginActivity extends BaseActivity {

    private ImageView logoView;
    private EditText usernameView;
    private EditText passwordView;
    private Button signInButton;
    private Handler handler;
    private Runnable runnable;

    private ProgressBar progressBar;
    private ScrollView loginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showProgress();

        loginForm = (ScrollView)findViewById(R.id.login_form);

        usernameView = (EditText) findViewById(R.id.username);
        usernameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) logoView.setVisibility(View.GONE);
                else logoView.setVisibility(View.VISIBLE);
            }
        });
        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        passwordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) logoView.setVisibility(View.GONE);
                else logoView.setVisibility(View.VISIBLE);
            }
        });

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                logoView.setVisibility(View.VISIBLE);
                attemptLogin();
            }
        });

        logoView = (ImageView) findViewById(R.id.logo);
        logoView.requestFocus();
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

        if (cancel) {
            focusView.requestFocus();
        } else {
            logoView.setVisibility(View.VISIBLE);
            new LoginTask(this, username, password).execute((Void) null);
        }

    }

    private boolean isEmailValid(String email) {
        // TODO CHANGE THIS
        return email.contains("@");
    }

    private boolean isUsernameValid(String username) {
        // TODO CHANGE THIS
        return username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        // TODO CHANGE THIS
        return password.length() > 0;
    }

    public EditText showEmailDialog(final String username, final String password) {
        final BaseActivity activity = this;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setMessage(getString(R.string.request_email));
        alertDialog.setView(input);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (isEmailValid(input.getText().toString())) {
                            dialog.cancel();
                            new SignupTask(activity, username, password, input.getText().toString()).execute((Void) null);
                        } else {
                            showEmailDialog(username, password);
                            showToast(getString(R.string.error_invalid_email));
                        }
                }
            }
        });
        alertDialog.show();
        return input;
    }

    public void showProgress() {
        progressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        animation.setDuration(1500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
                logoView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_top));
                logoView.setTranslationY((float) 50.0);

                loginForm.setVisibility(View.VISIBLE);
                loginForm.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_bottom));

            }
        };
        handler.postDelayed(runnable, 1500);
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

