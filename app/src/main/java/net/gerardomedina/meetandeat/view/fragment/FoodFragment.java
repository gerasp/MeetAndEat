package net.gerardomedina.meetandeat.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;
import net.gerardomedina.meetandeat.view.table.FoodAdapter;
import net.gerardomedina.meetandeat.view.table.SortableFoodTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

public class FoodFragment extends BaseFragment implements InitiableFragment {


    private View view;
    private SwipeToRefreshListener.RefreshIndicator refreshIndicator;
    private Meeting meeting;
    private List<Food> foodList;
    private FoodAdapter foodAdapter;

    public FoodFragment() {
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
        setAddButton();
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

    private void setAddButton() {
        FloatingActionButton addFoodButton = (FloatingActionButton) view.findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddFoodDialog();
            }
        });
        if (meeting.isOld()) addFoodButton.setVisibility(View.GONE);
    }


    private void createAddFoodDialog() {
        final AddFoodDialog addFoodDialog = new AddFoodDialog(this);
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
