package net.gerardomedina.meetandeat.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.Option;
import net.gerardomedina.meetandeat.task.AdminTask;
import net.gerardomedina.meetandeat.view.adapter.OptionAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdminActivity extends BaseActivity {

    private Meeting meeting;
    private static final int PLACE_PICKER_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        meeting = appCommon.getSelectedMeeting();
        setupActionBar();
        setupAdminOptionsList();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupAdminOptionsList() {
        ListView optionsListView = (ListView) findViewById(R.id.adminOptions);
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
        optionsListView.setAdapter(new OptionAdapter(this, options));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                new AdminTask(this,1,place.getLatLng().latitude + "," + place.getLatLng().longitude).execute();
            }
        }
    }

}
