package net.gerardomedina.meeteat.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.model.Meeting;
import net.gerardomedina.meeteat.task.GetParticipantsTask;
import net.gerardomedina.meeteat.task.MeetingOptionsTask;
import net.gerardomedina.meeteat.view.fragment.ChatFragment;
import net.gerardomedina.meeteat.view.fragment.FoodFragment;

public class MeetingActivity extends BaseActivity {
    private static final int PLACE_PICKER_REQUEST = 2;
    private Meeting meeting;
    private Menu menu;
    private Toolbar toolbar;

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
                changeToFoodFragment(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        meeting = appCommon.getSelectedMeeting();
        if (meeting == null) changeToActivityNoBackStack(MainActivity.class);
        super.onCreate(savedInstanceState);
        if (appCommon.isColorDark(meeting.getColor())) setContentView(R.layout.activity_meeting_white);
        else setContentView(R.layout.activity_meeting_black);
        init();
    }

    private void init() {
        changeToFoodFragment(false);
        new GetParticipantsTask(this).execute();
        setToolbar();
    }


    public void changeToFoodFragment(boolean animate) {
        if (menu != null) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) transaction.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom);
        transaction.replace(R.id.fragment_container, new FoodFragment());
        transaction.commit();
    }

    public void changeToChatFragment() {
        if (menu != null) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
        transaction.replace(R.id.fragment_container, new ChatFragment());
        transaction.commit();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(meeting.getTitle());
        toolbar.setBackgroundColor(Color.parseColor(meeting.getColor()));

        FrameLayout fragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);
        fragmentContainer.setBackgroundColor(Color.parseColor(meeting.getColor()));
    }

    public void changeLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.e("Google Play", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                new MeetingOptionsTask(this,1,place.getLatLng().latitude + "," + place.getLatLng().longitude).execute();
            }
        }
    }


}
