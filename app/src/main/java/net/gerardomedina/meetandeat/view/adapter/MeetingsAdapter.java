package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.common.Meeting;
import net.gerardomedina.meetandeat.persistence.local.MeetingValues;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MeetingActivity;

public class MeetingsAdapter extends CursorAdapter {
    AppCommon appCommon = AppCommon.getInstance();
    private BaseActivity activity;
    public MeetingsAdapter(Context context, BaseActivity activity, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_dashboard_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        RelativeLayout meeting = (RelativeLayout) view.findViewById(R.id.meeting);
        TextView meetingTitle = (TextView) view.findViewById(R.id.meeting_title);
        TextView meetingDateTime = (TextView) view.findViewById(R.id.meeting_datetime);
        View meetingColor = view.findViewById(R.id.meeting_color);

        final String title = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TITLE));
        final String date = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_DATE));
        final String time = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TIME));
        final String color = (cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_COLOR)));
        meetingTitle.setText(title);
        meetingDateTime.setText(date + " | " + time);
        meetingColor.setBackgroundColor(Color.parseColor(color));

        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCommon.setSelectedMeeting(new Meeting(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MeetingValues._ID)),
                        title,
                        cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_LOCATION)),
                        date,
                        time,
                        color));
                activity.changeToActivity(MeetingActivity.class);
                activity.overridePendingTransition(R.anim.overshoot,R.anim.fade_out);
            }
        });
    }
}
