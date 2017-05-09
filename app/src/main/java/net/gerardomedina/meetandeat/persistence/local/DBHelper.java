package net.gerardomedina.meetandeat.persistence.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "MeetAndEat.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactValues.SQL_CREATE_ENTRIES);
        db.execSQL(MeetingValues.SQL_CREATE_ENTRIES);
        db.execSQL(OldMeetingValues.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactValues.SQL_DELETE_ENTRIES);
        db.execSQL(MeetingValues.SQL_DELETE_ENTRIES);
        db.execSQL(OldMeetingValues.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
