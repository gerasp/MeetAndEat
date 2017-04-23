package net.gerardomedina.meetandeat.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewStub;

import net.gerardomedina.meetandeat.R;

public abstract class NavigationActivity extends BaseActivity {

    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    changeToActivity(DashboardActivity.class);
                    return true;
                case R.id.navigation_notifications:
                    return true;
                case R.id.navigation_contacts:
                    return true;
                case R.id.navigation_preferences:
                    changeToActivity(SettingsActivity.class);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    void inflateBase(int layout) {
        setContentView(R.layout.navigation);
        ViewStub stub = (ViewStub) findViewById(R.id.content_container);
        stub.setLayoutResource(layout);
        stub.inflate();
    }
}

