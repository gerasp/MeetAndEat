package net.gerardomedina.meetandeat.view.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.Option;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.task.AddParticipantsTask;
import net.gerardomedina.meetandeat.task.AdminTask;
import net.gerardomedina.meetandeat.task.GetFoodTask;
import net.gerardomedina.meetandeat.view.activity.LocationActivity;
import net.gerardomedina.meetandeat.view.activity.MainActivity;
import net.gerardomedina.meetandeat.view.adapter.OptionAdapter;
import net.gerardomedina.meetandeat.view.dialog.AddFoodDialog;
import net.gerardomedina.meetandeat.view.table.FoodAdapter;
import net.gerardomedina.meetandeat.view.table.SortableFoodTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;

public class FoodFragment extends BaseFragment implements InitiableFragment {


    private View view;
    private SwipeToRefreshListener.RefreshIndicator refreshIndicator;
    private Meeting meeting;
    private List<Food> foodList;
    private FoodAdapter foodAdapter;
    private static final int PLACE_PICKER_REQUEST = 2;

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
            }
        });
        (view.findViewById(R.id.item4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsList();
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
    private void showOptionsList() {
        ListView optionsListView = new ListView(getBaseActivity());
        List<Option> options = new ArrayList<>();
        options.add(new Option(getString(R.string.change_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getBaseActivity());
                input.setText(meeting.getTitle());
                final AlertDialog alertDialog = new AlertDialog.Builder(getBaseActivity()).create();
                alertDialog.setView(input);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().length() > 20) showToast(R.string.error_field_too_long);
                        else new AdminTask(getBaseActivity(), 0, input.getText().toString()).execute();
                    }
                });
                alertDialog.show();
            }
        }));
        options.add(new Option(getString(R.string.change_location), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(getBaseActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("Google Play", e.getMessage());
                }
            }
        }));
        options.add(new Option(getString(R.string.change_date_and_time), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar newCalendar = Calendar.getInstance();
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getBaseActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                        final SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        new TimePickerDialog(getBaseActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                new AdminTask(getBaseActivity(),2,dateFormatter1.format(newDate.getTime())).execute();
                            }
                        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true).show();
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(newCalendar.getTime().getTime());
                datePickerDialog.show();
            }
        }));
        options.add(new Option(getString(R.string.change_color), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(getBaseActivity()).setTitle(getString(R.string.choose_color))
                        .noSliders().wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(7)
                        .setPositiveButton(android.R.string.ok, new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                new AdminTask(getBaseActivity(), 3, "#" + Integer.toHexString(selectedColor)).execute();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build().show();
            }
        }));
        options.add(new Option(getString(R.string.delete_participant), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> participants = meeting.getParticipants();
                participants.remove(appCommon.getUser().getUsername());
                final String [] selected = {""};
                new AlertDialog.Builder(getBaseActivity())
                        .setTitle(getString(R.string.participants))
                        .setSingleChoiceItems(participants.toArray(new String[0]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected[0]= participants.get(which);
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AdminTask(getBaseActivity(),4,selected[0]).execute();
                            }
                        })
                        .create().show();
            }
        }));
        options.add(new Option(getString(R.string.delete_meeting), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(getBaseActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(R.string.are_you_sure)
                        .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AdminTask(getBaseActivity(), 5, "").execute();
                            }
                        })
                        .create()
                        .show();
            }
        }));
        optionsListView.setAdapter(new OptionAdapter(getBaseActivity(), options));
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getBaseActivity());
        builderSingle.setView(optionsListView);
        builderSingle.show();

    }

    public void setRefreshIndicator(SwipeToRefreshListener.RefreshIndicator refreshIndicator) {
        this.refreshIndicator = refreshIndicator;
    }

    public void hideRefreshIndicator() {
        if (this.refreshIndicator != null) refreshIndicator.hide();
    }

}
