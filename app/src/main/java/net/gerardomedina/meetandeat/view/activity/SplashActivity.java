package net.gerardomedina.meetandeat.view.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import net.gerardomedina.meetandeat.R;

public class SplashActivity extends BaseActivity {

    private Handler handler;
    private Runnable runnable;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        animation.setDuration(1500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                int id = (Integer) appCommon.sharedGetValue(getApplicationContext(), "id", 2);
                if (id != 0) {
                    loginWithPrevValues(id);
                } else {
                    changeToActivity(LoginActivity.class);
                }
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