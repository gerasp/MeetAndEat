package net.gerardomedina.meetandeat.view.activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Meeting;

public class MeetingActivity extends BaseActivity {

    private Meeting meeting;
    private AlertDialog alertDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public Activity getActivity() {
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
//                LinearLayout layout = new LinearLayout(getActivity());
//                layout.setOrientation(LinearLayout.VERTICAL);
//
//                final EditText category = new EditText(getActivity());
//                category.setHint("Title");
//                layout.addView(category);
//
//                final EditText descriptionBox = new EditText(getActivity());
//                descriptionBox.setHint("Description");
//                layout.addView(descriptionBox);
//
//                dialog.setView(layout);

                showAddFoodDialog();
            }
        });
    }

    private void showAddFoodDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        LinearLayout linearLayout = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setPadding(10,10,10,10);
        scrollView.addView(generateIconsTable());
        alertDialog.setView(scrollView);
        alertDialog.show();
    }

    private TableLayout generateIconsTable() {
        TableLayout table = new TableLayout(this);
        try {
            int counter = 0;
            for (int i = 1; i <= 50; i++) {
                TableRow row = new TableRow(this);
                for (int j = 1; j < 5; j++) {
                    counter = counter+1;
                    int id = R.drawable.class.getField("ic_"+counter).getInt(0);
                    ImageView icon = new ImageView(this);
                    icon.setImageResource(id);
                    icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    row.addView(icon);
                }
                table.addView(row);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return table;
    }
}
