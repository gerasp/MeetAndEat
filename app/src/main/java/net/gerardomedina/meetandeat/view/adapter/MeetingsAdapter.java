package net.gerardomedina.meetandeat.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.persistence.local.MeetingValues;

public class MeetingsAdapter extends CursorAdapter {


    public MeetingsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_dashboard_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView meetingTitle = (TextView) view.findViewById(R.id.meeting_title);
        TextView meetingDateTime = (TextView) view.findViewById(R.id.meeting_datetime);
        View meetingColor = view.findViewById(R.id.meeting_color);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TITLE));
        String datetime = cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_DATE))
                +" "+cursor.getString(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_TIME));
        int color = (cursor.getInt(cursor.getColumnIndexOrThrow(MeetingValues.COLUMN_NAME_COLOR)));
        meetingTitle.setText(title);
        meetingDateTime.setText(datetime);
        meetingColor.setBackgroundColor(color);
    }
}
