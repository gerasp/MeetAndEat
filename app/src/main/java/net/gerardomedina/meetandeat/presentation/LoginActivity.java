package net.gerardomedina.meetandeat.presentation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gerardomedina.meetandeat.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask authTask = null;

    private ImageView logoView;
    private EditText usernameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        Button usernameSignInButton = (Button) findViewById(R.id.sign_in_button);
        usernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        logoView = (ImageView) findViewById(R.id.logo);
        logoView.requestFocus();
    }

    private void attemptLogin() {
        if (authTask != null) {
            return;
        }
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
            authTask = new UserLoginTask(username, password);
            authTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        // TODO CHANGE THIS
        return username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        // TODO CHANGE THIS
        return password.length() > 0;
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;

        UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            getUsers();
            // TODO: register the new account here if the account does not exist.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            if (success) {
                finish();
            } else {
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
        }


        private void getUsers() {
            final String url = "jdbc:mysql://192.168.1.51/";
            final String user = "root";
            final String password = "root";
            try {

                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement();
                final ResultSet rs = st.executeQuery("SELECT * FROM meetandeat.User;");
                while (rs.next()) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            try {
                                Toast.makeText(LoginActivity.this, rs.getString(0), Toast.LENGTH_SHORT).show();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (isCancelled()) break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

