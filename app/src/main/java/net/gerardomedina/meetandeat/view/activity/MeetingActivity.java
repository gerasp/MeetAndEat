package net.gerardomedina.meetandeat.view.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.model.Meeting;
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
    private Menu menu;
    private SwipeToRefreshListener.RefreshIndicator refreshIndicator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        if (meeting != null && appCommon.isColorDark(meeting.getColor())) {
            menu.getItem(0).setIcon(R.drawable.ic_chat_white);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

        if (appCommon.hasInternet(this)) new GetFoodTask(this).execute();
        else {
            showSimpleDialog(R.string.no_internet_connection);
            changeToActivityNoBackStack(MainActivity.class);
        }

        setToolbar();
        setAddFoodButton();
    }

    private void setAddFoodButton() {
        FloatingActionButton addFoodButton = (FloatingActionButton) findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddFoodDialog();
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(meeting.getTitle());
        toolbar.setBackgroundColor(Color.parseColor(meeting.getColor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(meeting.getColor()));
        }
    }

    private void createAddFoodDialog() {
        final AddFoodDialog addFoodDialog = new AddFoodDialog(getBaseActivity());
        addFoodDialog.setView(getBaseActivity().getLayoutInflater().inflate(R.layout.dialog_addfood, null));
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

    public void populateFoodTable(JSONObject response) throws JSONException {
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
        foodTableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                new GetFoodTask(getBaseActivity()).execute();
                setRefreshIndicator(refreshIndicator);
            }
        });
    }

    public void setRefreshIndicator(SwipeToRefreshListener.RefreshIndicator refreshIndicator) {
        this.refreshIndicator = refreshIndicator;
    }

    public void hideRefreshIndicator() {
        if (this.refreshIndicator != null) refreshIndicator.hide();
    }
}
