package net.gerardomedina.meetandeat.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.fragment.DashboardFragment;
import net.gerardomedina.meetandeat.view.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends BaseActivity {

    private Toolbar toolbar;
    final List<MenuItem> items=new ArrayList<>();
    private int lastPosition = 1;
    private int position = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            position = items.indexOf(item);
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    changeToFragment(new DashboardFragment());
                    return true;
                case R.id.navigation_notifications:
                    return true;
                case R.id.navigation_contacts:
                    return true;
                case R.id.navigation_preferences:
                    changeToFragment(new SettingsFragment());
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeToFragment(new DashboardFragment());
        setContentView(R.layout.activity_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu=navigation.getMenu();
        for(int i=0; i<menu.size(); i++){ items.add(menu.getItem(i)); }
    }

    void changeToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lastPosition > position) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        lastPosition = position;
    }
}

