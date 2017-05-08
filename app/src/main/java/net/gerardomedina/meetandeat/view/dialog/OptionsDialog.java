package net.gerardomedina.meetandeat.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TimePicker;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.model.Option;
import net.gerardomedina.meetandeat.task.MeetingOptionsTask;
import net.gerardomedina.meetandeat.view.adapter.OptionAdapter;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;
import net.gerardomedina.meetandeat.view.fragment.FoodFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OptionsDialog extends Dialog {
    private static final int PLACE_PICKER_REQUEST = 2;
    private AppCommon appCommon = AppCommon.getInstance();
    private final BaseFragment foodFragment;
    private Meeting meeting;

    public OptionsDialog(BaseFragment foodFragment) {
        super(foodFragment.getBaseActivity());
        this.foodFragment = foodFragment;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meeting = appCommon.getSelectedMeeting();
        showOptionsList();
    }

    public void showOptionsList() {
        ListView optionsListView = findViewById(R.id.optionList);
        List<Option> options = new ArrayList<Option>();
        options.add(new Option(foodFragment.getString(R.string.change_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(foodFragment.getBaseActivity());
                input.setText(meeting.getTitle());
                final AlertDialog alertDialog = new AlertDialog.Builder(foodFragment.getBaseActivity()).create();
                alertDialog.setView(input);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, foodFragment.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().length() > 20)
                            foodFragment.showToast(R.string.error_field_too_long);
                        else
                            new MeetingOptionsTask(foodFragment.getBaseActivity(), 0, input.getText().toString()).execute();
                    }
                });
                alertDialog.show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.change_location), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(foodFragment.getBaseActivity());
                    foodFragment.startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("Google Play", e.getMessage());
                }
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.change_date_and_time), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar newCalendar = Calendar.getInstance();
                final DatePickerDialog datePickerDialog = new DatePickerDialog(foodFragment.getBaseActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                        final SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        new TimePickerDialog(foodFragment.getBaseActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                new MeetingOptionsTask(foodFragment.getBaseActivity(), 2, dateFormatter1.format(newDate.getTime())).execute();
                            }
                        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true).show();
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(newCalendar.getTime().getTime());
                datePickerDialog.show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.change_color), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(foodFragment.getBaseActivity()).setTitle(foodFragment.getString(R.string.choose_color))
                        .noSliders().wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(7)
                        .setPositiveButton(android.R.string.ok, new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                new MeetingOptionsTask(foodFragment.getBaseActivity(), 3, "#" + Integer.toHexString(selectedColor)).execute();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build().show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.delete_participant), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> participants = meeting.getParticipants();
                participants.remove(appCommon.getUser().getUsername());
                final String[] selected = {""};
                new AlertDialog.Builder(foodFragment.getBaseActivity())
                        .setTitle(foodFragment.getString(R.string.participants))
                        .setSingleChoiceItems(participants.toArray(new String[0]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected[0] = participants.get(which);
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MeetingOptionsTask(foodFragment.getBaseActivity(), 4, selected[0]).execute();
                            }
                        })
                        .create().show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.delete_meeting), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(foodFragment.getBaseActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(R.string.are_you_sure)
                        .setNegativeButton(foodFragment.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(foodFragment.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MeetingOptionsTask(foodFragment.getBaseActivity(), 5, "").execute();
                            }
                        })
                        .create()
                        .show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.change_admin), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> participants = meeting.getParticipants();
                participants.remove(appCommon.getUser().getUsername());
                final String[] selected = {""};
                new AlertDialog.Builder(foodFragment.getBaseActivity())
                        .setTitle(foodFragment.getString(R.string.participants))
                        .setSingleChoiceItems(participants.toArray(new String[0]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected[0] = participants.get(which);
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MeetingOptionsTask(foodFragment.getBaseActivity(), 6, selected[0]).execute();
                            }
                        })
                        .create().show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.leave_meeting), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(foodFragment.getBaseActivity(), R.style.MyAlertDialogStyle)
                        .setMessage(R.string.are_you_sure)
                        .setNegativeButton(foodFragment.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(foodFragment.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MeetingOptionsTask(foodFragment.getBaseActivity(), 7, "").execute();
                            }
                        })
                        .create()
                        .show();
            }
        }));
        options.add(new Option(foodFragment.getString(R.string.set_alarm_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, meeting.getDatetime().getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, meeting.getDatetime().getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra(CalendarContract.Events.TITLE, meeting.getTitle());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, meeting.getLocation().toString());
                foodFragment.startActivity(intent);
            }
        }));
        optionsListView.setAdapter(new OptionAdapter(foodFragment.getBaseActivity(), options));
    }
}