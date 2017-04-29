package net.gerardomedina.meetandeat.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.view.adapter.SectionsAdapter;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    private final int DEFAULT_SECTION = 2;
    private final int NUMBER_OF_SECTIONS = 5;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_notifications) ;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsAdapter sectionsAdapter = new SectionsAdapter(this, getSupportFragmentManager(), NUMBER_OF_SECTIONS);
        viewPager = (ViewPager) findViewById(R.id.fragment_container);
        viewPager.setAdapter(sectionsAdapter);
        viewPager.setCurrentItem(DEFAULT_SECTION, true);
        viewPager.setOffscreenPageLimit(NUMBER_OF_SECTIONS-1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(DEFAULT_SECTION).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_contacts:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_calendar:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_preferences:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
    }
}

