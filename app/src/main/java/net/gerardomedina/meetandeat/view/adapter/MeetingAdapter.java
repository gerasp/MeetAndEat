package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.persistence.local.MeetingValues;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MeetingActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MeetingAdapter extends CursorAdapter {
    private final boolean isOld;
    AppCommon appCommon = AppCommon.getInstance();
    private BaseActivity activity;

    public MeetingAdapter(Context context, BaseActivity activity, Cursor c, boolean autoRequery, boolean isOld) {
        super(context, c, autoRequery);
        this.activity = activity;
        this.isOld = isOld;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_dashboard_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        CardView meetingLayout = (CardView) view.findViewById(R.id.meeting);
        TextView meetingTitle = (TextView) view.findViewById(R.id.meetingTitle);
        TextView meetingDateTime = (TextView) view.findViewById(R.id.meetingDatetime);
        View meetingColor = view.findViewById(R.id.meetingColor);

        final String title = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TITLE));
        final String datetime = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_DATETIME));
        final String color = (cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_COLOR)));

        final Meeting meeting = new Meeting(
                cursor.getInt(cursor.getColumnIndexOrThrow(MeetingValues._ID)),
                title,
                cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_LOCATION)),
                datetime,
                color,
                cursor.getInt(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_ISADMIN))>0);

        if (isOld) meeting.setOld(true);

        meetingTitle.setText(meeting.getTitle());
        Calendar datetimeCalendar = meeting.getDatetime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        meetingDateTime.setText(simpleDateFormat.format(datetimeCalendar.getTime()));
        meetingColor.setBackgroundColor(Color.parseColor(color));

        meetingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appCommon.hasInternet(activity)) {
                    appCommon.setSelectedMeeting(meeting);
                    activity.changeToActivity(MeetingActivity.class);
                    activity.overridePendingTransition(R.anim.overshoot, R.anim.fade_out);
                } else {
                    activity.showSimpleDialog(R.string.no_internet_connection);
                }
            }
        });
    }
}
