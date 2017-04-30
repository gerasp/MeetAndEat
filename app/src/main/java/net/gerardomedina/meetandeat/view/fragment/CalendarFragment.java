package net.gerardomedina.meetandeat.view.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.persistence.local.MeetingValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarFragment extends BaseFragment {

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar);
        calendarView.setFirstDayOfWeek(Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());

        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MeetingValues.TABLE_NAME +
                " order by " + MeetingValues.COLUMN_NAME_DATE + "," +
                MeetingValues.COLUMN_NAME_TIME + " ASC;", null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            final Meeting nextMeeting = new Meeting(1,
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TIME)), "");
            TextView calendarInfo = (TextView) view.findViewById(R.id.calendarInfo);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm", Locale.getDefault());
            calendarInfo.setText(getResources().getString(R.string.set_alarm_text,
                    simpleDateFormat1.format(nextMeeting.getDatetime().getTime()),
                    simpleDateFormat2.format(nextMeeting.getDatetime().getTime())));
            Button setAlarmButton = (Button) view.findViewById(R.id.setAlarmButton);
            setAlarmButton.setVisibility(View.VISIBLE);
            setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAlarm(nextMeeting);
                }
            });
        }
        return view;
    }

    private void setAlarm(Meeting nextMeeting) {
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, nextMeeting.getDatetime().getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, nextMeeting.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, "");
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY");
        values.put(CalendarContract.Events.DURATION, "+P1H");
        values.put(CalendarContract.Events.HAS_ALARM, 1);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_CALENDAR}, 4);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)
            cr.insert(CalendarContract.Events.CONTENT_URI, values);

    }
}
