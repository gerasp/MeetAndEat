package net.gerardomedina.meetandeat.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.task.AddParticipantsTask;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.activity.AdminActivity;
import net.gerardomedina.meetandeat.view.activity.LocationActivity;
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
    private BottomNavigationView bottomNavigationView;

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
        LayoutInflater inflater = (LayoutInflater)getBaseActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                createAddFoodDialog();
            }
        });
        (view.findViewById(R.id.item2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createParticipantsDialog();
            }
        });
        (view.findViewById(R.id.item3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().changeToActivity(LocationActivity.class);
                getBaseActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        (view.findViewById(R.id.item4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().changeToActivity(AdminActivity.class);
                getBaseActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void createParticipantsDialog() {
        new AlertDialog.Builder(getBaseActivity())
                .setTitle(getString(R.string.participants))
                .setItems(meeting.getParticipants().toArray(new String[0]), null)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton(R.string.add_participant_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createAddParticipantDialog();
                    }
                })
                .create().show();
    }

    private void createAddParticipantDialog() {
        SQLiteOpenHelper dbHelper = new DBHelper(getBaseActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ContactValues.COLUMN_NAME_USERNAME + " from " +
                ContactValues.TABLE_NAME + " order by "
                + ContactValues.COLUMN_NAME_USERNAME + " ASC;", null);
        List<String> contactsList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contactsList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME)));
        }
        cursor.close();
        contactsList.removeAll(meeting.getParticipants());
        if (contactsList.size() > 0) {
            final String[] contacts = contactsList.toArray(new String[0]);
            final boolean[] isChecked = new boolean[contacts.length];
            final List<String> selectedContacts = new ArrayList<>();
            new AlertDialog.Builder(getBaseActivity())
                    .setTitle(getString(R.string.select_from_contacts))
                    .setMultiChoiceItems(contacts, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) selectedContacts.add(contacts[which]);
                            else selectedContacts.remove(contacts[which]);
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String result = "";
                            for (String selectedContact : selectedContacts)
                                result = result + selectedContact + ",";
                            if (result.charAt(result.length() - 1) == ',')
                                result = result.substring(0, result.length() - 1);
                            new AddParticipantsTask(getBaseActivity(), result).execute();
                        }
                    })
                    .create().show();

        } else {
            getBaseActivity().showSimpleDialog(R.string.no_contact_to_invite);
        }
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
