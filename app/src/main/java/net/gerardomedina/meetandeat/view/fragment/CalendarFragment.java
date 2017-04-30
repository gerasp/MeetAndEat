package net.gerardomedina.meetandeat.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class CalendarFragment extends BaseFragment implements InitiableFragment {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Button setAlarmButton;
    private TextView calendarInfo;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar);
        calendarView.setFirstDayOfWeek(Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());

        setAlarmButton = (Button) view.findViewById(R.id.setAlarmButton);
        calendarInfo = (TextView) view.findViewById(R.id.calendarInfo);

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getReadableDatabase();

        init();
        return view;
    }

    public void init() {
        Cursor cursor = db.rawQuery("select * from " + MeetingValues.TABLE_NAME +
                " order by " + MeetingValues.COLUMN_NAME_DATE + "," +
                MeetingValues.COLUMN_NAME_TIME + " ASC;", null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            final Meeting nextMeeting = new Meeting(1,
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TIME)), "");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm", Locale.getDefault());
            calendarInfo.setText(getResources().getString(R.string.set_alarm_text,
                    simpleDateFormat1.format(nextMeeting.getDatetime().getTime()),
                    simpleDateFormat2.format(nextMeeting.getDatetime().getTime())));
            setAlarmButton.setVisibility(View.VISIBLE);
            setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAlarm(nextMeeting);
                }
            });
        }
    }

    private void setAlarm(Meeting nextMeeting) {

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", nextMeeting.getDatetime().getTimeInMillis());
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", nextMeeting.getTitle());
        startActivity(intent);

    }
}
