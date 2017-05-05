package net.gerardomedina.meetandeat.view.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.GetInvitationsTask;
import net.gerardomedina.meetandeat.view.adapter.SectionAdapter;
import net.gerardomedina.meetandeat.view.drawable.BadgeDrawable;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    private final int DEFAULT_SECTION = 2;
    private final int NUMBER_OF_SECTIONS = 5;
    private TextView counter;
    private LayerDrawable icon;
    public boolean realBadge = false;
    public String count;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setEnabled(true);
        MenuItem itemInvitations = menu.findItem(R.id.menu_invitations);
        icon = (LayerDrawable) itemInvitations.getIcon();
        setBadgeCount();
        return true;
    }

    public void setBadgeCount() {
        if (!realBadge) count = "0";
        BadgeDrawable badge;
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) badge = (BadgeDrawable) reuse;
        else badge = new BadgeDrawable(this);
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    public void setBadgeCountNoUI(String s) {
        realBadge = true;
        count = s;
        if (icon != null) setBadgeCount();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        changeToActivity(InvitationsActivity.class);
        overridePendingTransition(R.anim.slide_down,R.anim.fade_out);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        setViewPager();

        setBottomNavigationView();

        if (appCommon.hasInternet(this)) new GetInvitationsTask(this).execute();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setViewPager() {
        SectionAdapter sectionAdapter = new SectionAdapter(this, getSupportFragmentManager(), NUMBER_OF_SECTIONS);
        viewPager = (ViewPager) findViewById(R.id.fragment_container);
        viewPager.setAdapter(sectionAdapter);
        viewPager.setCurrentItem(DEFAULT_SECTION, true);
        viewPager.setOffscreenPageLimit(NUMBER_OF_SECTIONS - 1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setBottomNavigationView() {
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

