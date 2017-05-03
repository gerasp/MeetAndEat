package net.gerardomedina.meetandeat.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.persistence.local.ContactValues;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.task.NewMeeetingTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewMeetingActivity extends BaseActivity {

    private TextView titleInput;
    private TextView locationInput;
    private TextView dateInput;
    private TextView timeInput;
    private TextView colorInput;
    private TextView participantsInput;
    private String selectedDate;
    private String selectedTime;
    public static final int PLACE_PICKER_REQUEST = 1;
    private DBHelper dbHelper;

    Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newmeeting, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_new_meeting) attemptNewMeeting();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmeeting);
        setToolbar();


        titleInput = (TextView) findViewById(R.id.newMeetingTitleInput);
        dbHelper = new DBHelper(getActivity());

        setLocationPicker();
        setDateAndTimePicker();
        setColorPicker();
        setContactsPicker();
    }

    private void setContactsPicker() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ContactValues.COLUMN_NAME_USERNAME + " from " +
                ContactValues.TABLE_NAME + " order by "
                + ContactValues.COLUMN_NAME_USERNAME + " ASC;", null);

        if (cursor.getCount() > 0) {
            final String[] contacts = new String[cursor.getCount()];
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                contacts[i] = cursor.getString(cursor.getColumnIndexOrThrow(ContactValues.COLUMN_NAME_USERNAME));
                i++;
            }
            final boolean[] isChecked = new boolean[contacts.length];
            final List<String> selectedContacts = new ArrayList<>();
            participantsInput = (TextView) findViewById(R.id.newMeetingContactsInput);
            participantsInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                                    for (String selectedContact : selectedContacts) {
                                        result = result + selectedContact;
                                    }
                                    participantsInput.setText(result);
                                }
                            })
                            .create().show();
                }
            });
        } else {
            getBaseActivity().showSimpleDialog(R.string.new_meeting_no_contact);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setLocationPicker() {
        locationInput = (TextView) findViewById(R.id.newMeetingLocationInput);
        locationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("Google Play", e.getMessage());
                }
            }
        });
    }

    private void setColorPicker() {
        colorInput = (TextView) findViewById(R.id.newMeetingColorInput);
        colorInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(getActivity()).setTitle(getString(R.string.choose_color))
                        .noSliders().wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(7)
                        .setPositiveButton(getString(android.R.string.ok), new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                colorInput.setText("#" + Integer.toHexString(selectedColor));
                                colorInput.setTextColor(selectedColor);
                            }
                        }).build().show();
            }
        });
    }

    private void setDateAndTimePicker() {
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                selectedDate = dateFormatter1.format(newDate.getTime());
                dateInput.setText(dateFormatter2.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(newCalendar.getTime().getTime());
        dateInput = (TextView) findViewById(R.id.newMeetingDateInput);
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(0, 0, 0, hourOfDay, minute, 0);
                SimpleDateFormat dateFormatter1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat dateFormatter2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
                selectedTime = dateFormatter1.format(newDate.getTime());
                timeInput.setText(dateFormatter2.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
        timeInput = (TextView) findViewById(R.id.newMeetingTimeInput);
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewMeetingActivity.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                locationInput.setText(place.getLatLng().latitude + "," + place.getLatLng().longitude);
            }
        }
    }


    private void attemptNewMeeting() {
        List<TextView> inputs = new ArrayList<>();
        inputs.add(titleInput);
        inputs.add(locationInput);
        inputs.add(dateInput);
        inputs.add(timeInput);
        inputs.add(colorInput);
        inputs.add(participantsInput);

        for (TextView input : inputs) input.setError(null);

        boolean cancel = false;
        View focusView = null;
        for (TextView input : inputs) {
            if (TextUtils.isEmpty(input.getText())) {
                int ecolor = getResources().getColor(R.color.white);
                String estring = getString(R.string.error_field_required);
                ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                input.setError(ssbuilder);
                focusView = input;
                cancel = true;
            }
        }
        if (titleInput.getText().length() > 20) {
            int ecolor = getResources().getColor(R.color.white);
            String estring = getString(R.string.error_field_too_long);
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
            ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
            titleInput.setError(ssbuilder);
        }

        if (cancel) focusView.requestFocus();
        else new NewMeeetingTask(this,
                titleInput.getText().toString(),
                locationInput.getText().toString(),
                selectedDate,
                selectedTime,
                colorInput.getText().toString()).execute();

    }
}
