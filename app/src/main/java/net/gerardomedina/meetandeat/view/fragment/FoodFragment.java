package net.gerardomedina.meetandeat.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.activity.LocationActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;
import net.gerardomedina.meetandeat.view.dialog.OptionsDialog;
import net.gerardomedina.meetandeat.view.dialog.ParticipantsDialog;
import net.gerardomedina.meetandeat.view.table.FoodAdapter;
import net.gerardomedina.meetandeat.view.table.SortableFoodTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

public class FoodFragment extends BaseFragment implements InitiableFragment {


    private final ParticipantsDialog participantsDialog = new ParticipantsDialog(this);
    private View view;
    private SwipeToRefreshListener.RefreshIndicator refreshIndicator;
    private Meeting meeting;
    private List<Food> foodList;
    private FoodAdapter foodAdapter;

    public FoodFragment() {
    }

    public Meeting getMeeting() {
        return meeting;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);
        meeting = appCommon.getSelectedMeeting();
        init();

        return view;
    }

    public void init() {
        setTable();
        if (appCommon.hasInternet(getActivity())) new GetFoodTask(this).execute();
        else {
            getBaseActivity().showSimpleDialog(R.string.no_internet_connection);
            getBaseActivity().changeToActivityNoBackStack(MainActivity.class);
        }
        setBottomNavigation();
    }

    public void setTable() {
        foodList = new ArrayList<>();
        SortableFoodTableView foodTableView = (SortableFoodTableView) view.findViewById(R.id.foodTable);
        foodAdapter = new FoodAdapter(getBaseActivity(), foodList, foodTableView);
        foodTableView.setDataAdapter(foodAdapter);
        foodTableView.setSwipeToRefreshEnabled(true);
        foodTableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(final RefreshIndicator refreshIndicator) {
                new GetFoodTask(getBaseFragment()).execute();
                setRefreshIndicator(refreshIndicator);
            }
        });
    }

    private void setBottomNavigation() {
        FrameLayout stub = (FrameLayout) view.findViewById(R.id.navigation_stub);
        LayoutInflater inflater = (LayoutInflater) getBaseActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout;
        if (appCommon.isColorDark(meeting.getColor())) {
            layout = inflater.inflate(R.layout.fragment_food_navigation_white, null);
        } else {
            layout = inflater.inflate(R.layout.fragment_food_navigation_black, null);
        }
        layout.setBackgroundColor(Color.parseColor(meeting.getColor()));
        stub.addView(layout);
        (view.findViewById(R.id.item1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddFoodDialog addFoodDialog = new AddFoodDialog(getBaseFragment());
                addFoodDialog.setView(getBaseActivity().getLayoutInflater().inflate(R.layout.dialog_addfood,null));
                addFoodDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFoodDialog.attemptAddFood();
                    }
                });
                addFoodDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFoodDialog.dismiss();
                    }
                });
                addFoodDialog.show();
            }
        });
        (view.findViewById(R.id.item2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantsDialog.createParticipantsDialog();
            }
        });
        (view.findViewById(R.id.item3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().changeToActivity(LocationActivity.class);
            }
        });
        (view.findViewById(R.id.item4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OptionsDialog(getBaseFragment()).show();
            }
        });
    }


    public void populateFoodTable(JSONObject response) throws JSONException {
        foodList.clear();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonObject = results.getJSONObject(i);
            foodList.add(new Food(jsonObject.getString("icon"),
                    jsonObject.getString("description"),
                    jsonObject.getInt("amount"),
                    jsonObject.getString("username")
            ));
        }
        foodAdapter.notifyDataSetChanged();
    }

    public void setRefreshIndicator(SwipeToRefreshListener.RefreshIndicator refreshIndicator) {
        this.refreshIndicator = refreshIndicator;
    }

    public void hideRefreshIndicator() {
        if (this.refreshIndicator != null) refreshIndicator.hide();
    }

}
