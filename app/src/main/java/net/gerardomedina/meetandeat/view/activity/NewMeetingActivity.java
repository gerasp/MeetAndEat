package net.gerardomedina.meetandeat.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import net.gerardomedina.meetandeat.R;
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
    private TextView contactsInput;
    public static final int PLACE_PICKER_REQUEST = 1;

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

    private void attemptNewMeeting() {

        List<TextView> inputs = new ArrayList<>();
        inputs.add(titleInput);
        inputs.add(locationInput);
        inputs.add(dateInput);
        inputs.add(timeInput);
        inputs.add(colorInput);
// TODO       inputs.add(contactsInput);

        boolean cancel = false;
        View focusView = null;
        for (TextView input : inputs) {
            input.setError(null);
            if (TextUtils.isEmpty(input.getText())){
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
        if (titleInput.getText().length() > 20){
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
                dateInput.getText().toString(),
                timeInput.getText().toString(),
                colorInput.getText().toString()).execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmeeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity activity = this;

        titleInput = (TextView) findViewById(R.id.newMeetingTitleInput);

        locationInput = (TextView) findViewById(R.id.newMeetingLocationInput);
        locationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(activity);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("Google Play", e.getMessage());
                }
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog  = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                newDate.set(year, monthOfYear, dayOfMonth);
                dateInput.setText(dateFormatter.format(newDate.getTime()));
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
                timeInput.setText((hourOfDay > 9 ? "" + hourOfDay : "0" + hourOfDay)
                        + ":"
                        + (minute > 9 ? "" + minute : "0" + minute)
                        + ":00");
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
        timeInput = (TextView) findViewById(R.id.newMeetingTimeInput);
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        colorInput = (TextView) findViewById(R.id.newMeetingColorInput);
        colorInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(getActivity()).setTitle(getString(R.string.choose_color))
                        .noSliders().wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).density(6)
                        .setPositiveButton(getString(android.R.string.ok), new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                colorInput.setText("#"+Integer.toHexString(selectedColor));
                            }
                        }).build().show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewMeetingActivity.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                LatLng latLng = PlacePicker.getPlace(data, this).getLatLng();
                locationInput.setText(latLng.latitude+","+latLng.longitude);
            }
        }
    }
}
