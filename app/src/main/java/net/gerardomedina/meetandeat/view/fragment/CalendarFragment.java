package net.gerardomedina.meetandeat.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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
        List<Meeting> meetingList = new ArrayList<>();
        meetingList.add(new Meeting("hola", new Date(117, 3, 2)));
        calendarView.set(generateVisibleMonths(), new StyledCalendarBuilder(meetingList));

        return view;
    }

    private VisibleMonths generateVisibleMonths() {
        Date date = new Date();
        date.setMonth(3);
        date.setYear(117);
        return CalendarDataFactory.getInstance(Locale.getDefault()).create(date, 7);

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

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
            super.onBindViewHolder(holder, position, payloads);
            Day day = data.getAt(position);
            for (Meeting meeting : meetings) {
                ((StyledViewHolder) holder).bindMeeting(meeting);

//                if (isSameDay(meeting.date, day.getDate())) {
//                    ((StyledViewHolder)holder).bindMeeting(meeting);
//                    break;
//                }
            }
        }

    }

    private static class StyledViewHolder extends CalendarDayViewHolder {
        private final TextView detailView;
        StyledViewHolder(View itemView) {
            super(itemView);
            detailView = (TextView) itemView.findViewById(R.id.date_details);
        }
        @Override
        public void bind(Day day, DayState state) {
            super.bind(day, state);
            detailView.setText("d");
        }
        public void bindMeeting(Meeting meeting) {
            detailView.setText(meeting.name);
        }
    }

    public static class VerticalViewPager extends ViewPager {

        public VerticalViewPager(Context context) {
            super(context);
            init();
        }

        public VerticalViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            // The majority of the magic happens here
            setPageTransformer(true, new VerticalPageTransformer());
            // The easiest way to get rid of the overscroll drawing that happens on the left and right
            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        private class VerticalPageTransformer implements PageTransformer {

            @Override
            public void transformPage(View view, float position) {

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    view.setAlpha(1);

                    // Counteract the default slide transition
                    view.setTranslationX(view.getWidth() * -position);

                    //set Y position to swipe in from top
                    float yPosition = position * view.getHeight();
                    view.setTranslationY(yPosition);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        }

        /**
         * Swaps the X and Y coordinates of your touch event.
         */
        private MotionEvent swapXY(MotionEvent ev) {
            float width = getWidth();
            float height = getHeight();

            float newX = (ev.getY() / height) * width;
            float newY = (ev.getX() / width) * height;

            ev.setLocation(newX, newY);

            return ev;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev){
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev); // return touch coordinates to original reference frame for any child views
            return intercepted;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return super.onTouchEvent(swapXY(ev));
        }

    }
}
