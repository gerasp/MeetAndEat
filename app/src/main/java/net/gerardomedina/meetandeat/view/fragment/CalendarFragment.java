package net.gerardomedina.meetandeat.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
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

import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends BaseFragment implements InitiableFragment {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Button setAlarmButton;
    private TextView calendarInfo;
    private CalendarView calendarView;
    private View view;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendar);
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
                " order by " + MeetingValues.COLUMN_NAME_DATETIME + " ASC;", null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            final Meeting nextMeeting = new Meeting(1,
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_DATETIME)), "");
            calendarView.setDate(nextMeeting.getDatetime().getTime().getTime());
            setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAlarm(nextMeeting);
                }
            });
        } else {
            calendarInfo.setVisibility(View.VISIBLE);
            setAlarmButton.setVisibility(View.GONE);
        }
    }

    private void setAlarm(Meeting nextMeeting) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, nextMeeting.getDatetime().getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, nextMeeting.getDatetime().getTimeInMillis()+60*60*1000);
        intent.putExtra(CalendarContract.Events.TITLE, nextMeeting.getTitle());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, nextMeeting.getLocation().toString());
        startActivity(intent);
    }
}
