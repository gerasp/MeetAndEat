package net.gerardomedina.meeteat.persistence.local;

import android.provider.BaseColumns;

public class ContactValues implements BaseColumns {
    public static final String TABLE_NAME = "contact";
    public static final String COLUMN_NAME_USERNAME = "username";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactValues.TABLE_NAME + " (" +
                    ContactValues._ID + " INTEGER PRIMARY KEY," +
                    ContactValues.COLUMN_NAME_USERNAME + " TEXT" + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactValues.TABLE_NAME;
}
