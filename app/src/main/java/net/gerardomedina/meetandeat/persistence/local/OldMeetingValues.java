package net.gerardomedina.meetandeat.persistence.local;

import android.provider.BaseColumns;

public class OldMeetingValues implements BaseColumns {
    public static final String TABLE_NAME = "oldMeeting";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_LOCATION = "location";
    public static final String COLUMN_NAME_DATETIME = "datetime";
    public static final String COLUMN_NAME_COLOR = "color";
    public static final String COLUMN_NAME_ISADMIN = "isAdmin";


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MeetingValues.TABLE_NAME + " (" +
                    MeetingValues._ID + " INTEGER PRIMARY KEY," +
                    MeetingValues.COLUMN_NAME_TITLE + " TEXT," +
                    MeetingValues.COLUMN_NAME_LOCATION + " TEXT," +
                    MeetingValues.COLUMN_NAME_DATETIME + " DATETIME," +
                    MeetingValues.COLUMN_NAME_COLOR + " TEXT" +
                    MeetingValues.COLUMN_NAME_ISADMIN + " BOOLEAN" + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MeetingValues.TABLE_NAME;
}