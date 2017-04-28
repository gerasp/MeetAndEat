package net.gerardomedina.meetandeat.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Meeting;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;

public class MeetingActivity extends BaseActivity {

    private Meeting meeting;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        meeting = appCommon.getMeeting();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(meeting.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setBackgroundColor(Color.parseColor(meeting.getColor()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.locationFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToActivity(LocationActivity.class);
            }
        });

        Button addFoodButton = (Button) findViewById(R.id.add_food_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFoodDialog addFoodDialog = new AddFoodDialog(getActivity());
                addFoodDialog.show();
            }
        });
    }
}
