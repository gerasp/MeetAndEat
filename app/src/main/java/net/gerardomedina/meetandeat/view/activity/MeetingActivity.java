package net.gerardomedina.meetandeat.view.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.task.GetParticipantsTask;
import net.gerardomedina.meetandeat.view.fragment.ChatFragment;
import net.gerardomedina.meetandeat.view.fragment.FoodFragment;

public class MeetingActivity extends BaseActivity {

    private Meeting meeting;
    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        this.menu = menu;
        if (meeting != null && appCommon.isColorDark(meeting.getColor())) {
            menu.getItem(0).setIcon(R.drawable.ic_chat_white);
            menu.getItem(1).setIcon(R.drawable.ic_menu_restaurant_white);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat:
                changeToChatFragment();
                break;
            case R.id.menu_food:
                changeToFoodFragment();
                break;
            case R.id.menu_participants:
                new AlertDialog.Builder(getBaseActivity())
                        .setTitle(getString(R.string.select_from_contacts))
                        .setItems(meeting.getParticipants().toArray(new String[0]),null)
                        .create().show();
                break;
            case R.id.menu_location:
                changeToActivity(LocationActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        meeting = appCommon.getSelectedMeeting();
        if (meeting == null) changeToActivityNoBackStack(MainActivity.class);
        if (appCommon.isColorDark(meeting.getColor())) setTheme(R.style.AppTheme_AppBarOverlayDark);
        else setTheme(R.style.AppTheme_AppBarOverlay);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        init();
    }

    private void init() {
        new GetParticipantsTask(this).execute();
        setToolbar();
        changeToFoodFragment();
    }


    public void changeToFoodFragment() {
        if (menu != null) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
        transaction.replace(R.id.fragment_container, new FoodFragment());
        transaction.commit();
    }

    public void changeToChatFragment() {
        if (menu != null) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom);
        transaction.replace(R.id.fragment_container, new ChatFragment());
        transaction.commit();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(meeting.getTitle());
        toolbar.setBackgroundColor(Color.parseColor(meeting.getColor()));

        FrameLayout fragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);
        fragmentContainer.setBackgroundColor(Color.parseColor(meeting.getColor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(meeting.getColor()));
        }
    }

}
