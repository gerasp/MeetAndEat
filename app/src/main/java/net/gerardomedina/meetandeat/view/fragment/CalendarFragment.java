package net.gerardomedina.meetandeat.view.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AlarmReceiver;
import net.gerardomedina.meetandeat.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends BaseFragment {

    final static int REQUEST_CODE = 3;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = (CalendarView)view.findViewById(R.id.calendar);
        calendarView.setFirstDayOfWeek(Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());

        final Meeting nextMeeting = appCommon.getNextMeeting();
        if (nextMeeting != null) {
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
        Intent intent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(),
                REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextMeeting.getDatetime().getTimeInMillis(), pendingIntent);

    }
}
