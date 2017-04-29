package net.gerardomedina.meetandeat.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Meeting;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends BaseFragment {

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = (CalendarView)view.findViewById(R.id.calendar);
        calendarView.setFirstDayOfWeek(Calendar.getInstance(Locale.getDefault()).getFirstDayOfWeek());

        Meeting nextMeeting = appCommon.getNextMeeting();
        if (nextMeeting != null) {
            TextView calendarInfo = (TextView) view.findViewById(R.id.calendarInfo);
            calendarInfo.setText(getResources().getString(R.string.set_alarm_text, nextMeeting.getDate(), nextMeeting.getTime() ));
            Button setAlarmButton = (Button) view.findViewById(R.id.setAlarmButton);
            setAlarmButton.setVisibility(View.VISIBLE);
        }
        return view;
    }


}