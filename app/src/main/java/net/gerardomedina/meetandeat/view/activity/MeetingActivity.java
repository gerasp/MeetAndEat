package net.gerardomedina.meetandeat.view.activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Food;
import net.gerardomedina.meetandeat.common.Meeting;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;
import net.gerardomedina.meetandeat.view.table.FoodAdapter;
import net.gerardomedina.meetandeat.view.table.SortableFoodTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

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
        switch (item.getItemId()){
            case R.id.menu_location:
                changeToActivity(LocationActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        meeting = appCommon.getSelectedMeeting();
        if (meeting == null) changeToActivityNoBackStack(MainActivity.class);
        if (appCommon.isColorDark(meeting.getColor())) setTheme(R.style.AppTheme_AppBarOverlayDark);
        else setTheme(R.style.AppTheme_AppBarOverlay);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(meeting.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setBackgroundColor(Color.parseColor(meeting.getColor()));

        FloatingActionButton addFoodButton = (FloatingActionButton) findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToActivity(LocationActivity.class);
            }
        });
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddFoodDialog addFoodDialog = new AddFoodDialog(getActivity());
                addFoodDialog.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_addfood, null));
                addFoodDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                addFoodDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFoodDialog.dismiss();
                    }
                });
                addFoodDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = addFoodDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addFoodDialog.attemptAddFood();
                            }
                        });
                    }
                });

                addFoodDialog.show();
            }
        });

        if (appCommon.hasInternet(this)) new GetFoodTask(this).execute();
        else {
            showSimpleDialog(getString(R.string.no_internet_connection));
            changeToActivityNoBackStack(MainActivity.class);
        }

    }

    public void populateFoodTable (JSONObject response) throws JSONException {
        List<Food> foodList = new ArrayList<>();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonObject = results.getJSONObject(i);
            foodList.add(new Food(jsonObject.getString("icon"),
                    jsonObject.getString("description"),
                    jsonObject.getInt("amount"),
                    jsonObject.getString("username")
            ));
        }


        final SortableFoodTableView foodTableView = (SortableFoodTableView) findViewById(R.id.foodTable);
        final FoodAdapter foodAdapter = new FoodAdapter(this, foodList, foodTableView);
        foodTableView.setDataAdapter(foodAdapter);
        foodTableView.setSwipeToRefreshEnabled(true);
        final BaseActivity activity = this;
        foodTableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                foodTableView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetFoodTask(activity).execute();
                    }
                }, 3000);
            }
        });
    }
}
