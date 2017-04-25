package net.gerardomedina.meetandeat.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.jodamob.android.calendar.CalendarAdapter;
import de.jodamob.android.calendar.CalendarBuilder;
import de.jodamob.android.calendar.CalendarDataFactory;
import de.jodamob.android.calendar.CalendarDayViewHolder;
import de.jodamob.android.calendar.CalenderWidget;
import de.jodamob.android.calendar.Day;
import de.jodamob.android.calendar.DayState;
import de.jodamob.android.calendar.VisibleMonths;

import static de.jodamob.android.calendar.CalendarUtil.isSameDay;

public class CalendarFragment extends BaseFragment {


    private View view;
    private CalenderWidget calendarView;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (CalenderWidget) view.findViewById(R.id.calendar);
        calendarView.set(new CalendarBuilder(R.layout.fragment_calendar_cell, R.layout.fragment_calendar_header));
        final List<Meeting> meetingList = new ArrayList<>();
        meetingList.add(new Meeting("hola", new Date(117, 3, 2)));
        calendarView.set(generateVisibleMonths(), new StyledCalendarBuilder(meetingList));
        return view;
    }

    private VisibleMonths generateVisibleMonths() {
        Date date = new Date();
        return CalendarDataFactory.getInstance(Locale.getDefault()).create(date, 4);

    }

    private static class StyledCalendarBuilder extends CalendarBuilder {
        private final List<Meeting> meetings;

        StyledCalendarBuilder(List<Meeting> meetings) {
            super(R.layout.fragment_calendar_cell, R.layout.fragment_calendar_header);
            this.meetings = meetings;
        }

        @Override
        public CalendarAdapter createAdapterFor(LayoutInflater inflater, VisibleMonths months) {
            return new StyledAdapter(R.layout.fragment_calendar_cell, inflater, months, meetings);
        }
    }

    private static class StyledAdapter extends CalendarAdapter {
        private final List<Meeting> meetings;

        StyledAdapter(@LayoutRes int layout, LayoutInflater inflater, VisibleMonths data, List<Meeting> meetings) {
            super(layout, inflater, data);
            this.meetings = meetings;
        }

        @NonNull
        @Override
        protected RecyclerView.ViewHolder createViewHolder(View view) {
            return new StyledViewHolder(view);
        }

//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
//            super.onBindViewHolder(holder, position, payloads);
////            Day day = data.getAt(position);
////            for (Meeting meeting : meetings) {
////                if (isSameDay(meeting.date, day.getDate())) {
////                    ((StyledViewHolder)holder).bindMeeting();
////                    break;
////                }
////            }
//        }

    }

    private static class StyledViewHolder extends CalendarDayViewHolder {
        private final TextView detailView;
        StyledViewHolder(View itemView) {
            super(itemView);
            detailView = (TextView) itemView.findViewById(R.id.date_details);
            detailView.setVisibility(View.VISIBLE);

        }
        public void bindMeeting() {
            detailView.setVisibility(View.VISIBLE);
        }
    }
}
