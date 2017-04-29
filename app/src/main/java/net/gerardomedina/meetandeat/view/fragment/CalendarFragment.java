package net.gerardomedina.meetandeat.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;

import org.w3c.dom.Text;

public class CalendarFragment extends BaseFragment {

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        if (appCommon.getNextMeeting() != null) {
            TextView calendarInfo = (TextView) view.findViewById(R.id.calendarInfo);
            calendarInfo.setText(getString(R.string.));
        }
        return view;
    }


}
