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
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.persistence.local.OldMeetingValues;
import net.gerardomedina.meetandeat.task.GetOldMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.adapter.MeetingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryFragment extends BaseFragment implements InitiableFragment {
    private View view;
    private ListView meetingListView;
    private DBHelper dbHelper;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        meetingListView = (ListView) view.findViewById(R.id.meetings);
        dbHelper = new DBHelper(getActivity());

        init();
        return view;
    }

    public void init() {
        if (appCommon.hasInternet(getActivity())) new GetOldMeetingsTask(this).execute();
        else loadMeetingListFromLocalDB();
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
            values.put(OldMeetingValues.COLUMN_NAME_DATETIME, results.getJSONObject(i).getString("datetime"));
            values.put(OldMeetingValues.COLUMN_NAME_COLOR, results.getJSONObject(i).getString("color"));
            db.insert(OldMeetingValues.TABLE_NAME, null, values);
        }
        loadMeetingListFromLocalDB();

    }

    public void loadMeetingListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ OldMeetingValues.TABLE_NAME+
                " order by " + OldMeetingValues.COLUMN_NAME_DATETIME+" ASC;",null);
        meetingListView.setAdapter(null);
        meetingListView.setAdapter(new MeetingAdapter(getActivity(), (BaseActivity) getActivity(),cursor,true,true));
    }

}
