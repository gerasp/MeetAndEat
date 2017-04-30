package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.persistence.local.OldMeetingValues;
import net.gerardomedina.meetandeat.task.GetOldMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.adapter.MeetingsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryFragment extends BaseFragment {
    private View view;
    private ListView meetingListView;
    private DBHelper dbHelper;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        FloatingActionButton newMeetingButton = (FloatingActionButton) view.findViewById(R.id.deleteOldMeetingsButton);
        newMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        meetingListView = (ListView) view.findViewById(R.id.meetings);
        dbHelper = new DBHelper(getActivity());
        if (appCommon.hasInternet(getActivity())) new GetOldMeetingsTask(this).execute();
        else loadMeetingListFromLocalDB();

        return view;
    }

    public void saveMeetingListToLocalDB(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(OldMeetingValues.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < results.length(); i++) {
            values.put(OldMeetingValues._ID, results.getJSONObject(i).getInt("id"));
            values.put(OldMeetingValues.COLUMN_NAME_TITLE, results.getJSONObject(i).getString("title"));
            values.put(OldMeetingValues.COLUMN_NAME_LOCATION, results.getJSONObject(i).getString("location"));
            values.put(OldMeetingValues.COLUMN_NAME_DATE, results.getJSONObject(i).getString("date"));
            values.put(OldMeetingValues.COLUMN_NAME_TIME, results.getJSONObject(i).getString("time"));
            values.put(OldMeetingValues.COLUMN_NAME_COLOR, results.getJSONObject(i).getString("color"));
            db.insert(OldMeetingValues.TABLE_NAME, null, values);
        }
        loadMeetingListFromLocalDB();

    }

    private void loadMeetingListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ OldMeetingValues.TABLE_NAME+
                " order by " + OldMeetingValues.COLUMN_NAME_DATE+","+
                OldMeetingValues.COLUMN_NAME_TIME+ " ASC;",null);
        meetingListView.setAdapter(new MeetingsAdapter(getActivity(), (BaseActivity) getActivity(),cursor,true));
    }

}
