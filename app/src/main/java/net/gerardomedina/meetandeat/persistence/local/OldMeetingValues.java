package net.gerardomedina.meetandeat.persistence.local;

import android.provider.BaseColumns;

public class OldMeetingValues implements BaseColumns {
    public static final String TABLE_NAME = "oldMeeting";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_LOCATION = "location";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_COLOR = "color";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + OldMeetingValues.TABLE_NAME + " (" +
                    OldMeetingValues._ID + " INTEGER PRIMARY KEY," +
                    OldMeetingValues.COLUMN_NAME_TITLE + " TEXT," +
                    OldMeetingValues.COLUMN_NAME_LOCATION + " TEXT," +
                    OldMeetingValues.COLUMN_NAME_DATE + " DATE," +
                    OldMeetingValues.COLUMN_NAME_TIME + " TIME," +
                    OldMeetingValues.COLUMN_NAME_COLOR + " TEXT" + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OldMeetingValues.TABLE_NAME;
}
