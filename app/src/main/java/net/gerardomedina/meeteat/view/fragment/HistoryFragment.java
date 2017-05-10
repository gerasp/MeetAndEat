package net.gerardomedina.meeteat.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.persistence.local.DBHelper;
import net.gerardomedina.meeteat.persistence.local.OldMeetingValues;
import net.gerardomedina.meeteat.task.GetOldMeetingsTask;
import net.gerardomedina.meeteat.view.activity.BaseActivity;
import net.gerardomedina.meeteat.view.adapter.MeetingAdapter;

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
        init();
        return view;
    }

    public void init() {
        meetingListView = (ListView) view.findViewById(R.id.meetings);
        dbHelper = new DBHelper(getActivity());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetOldMeetingsTask(getBaseFragment()).execute();
            }
        });
        loadMeetingListFromLocalDB();
        if (appCommon.hasInternet(getActivity())) new GetOldMeetingsTask(this).execute();
    }

    public void saveMeetingListToLocalDB(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(OldMeetingValues.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonObject = results.getJSONObject(i);
            values.put(OldMeetingValues._ID, jsonObject.getInt("id"));
            values.put(OldMeetingValues.COLUMN_NAME_TITLE, jsonObject.getString("title"));
            values.put(OldMeetingValues.COLUMN_NAME_LOCATION, jsonObject.getString("location"));
            values.put(OldMeetingValues.COLUMN_NAME_DATETIME, jsonObject.getString("datetime"));
            values.put(OldMeetingValues.COLUMN_NAME_COLOR, jsonObject.getString("color"));
            db.insert(OldMeetingValues.TABLE_NAME, null, values);
        }
        loadMeetingListFromLocalDB();

    }

    public void loadMeetingListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + OldMeetingValues.TABLE_NAME +
                " order by " + OldMeetingValues.COLUMN_NAME_DATETIME + " ASC;", null);
        if (cursor.getCount() > 0) {
            meetingListView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.noContent).setVisibility(View.GONE);
            meetingListView.setAdapter(new MeetingAdapter(getActivity(), (BaseActivity) getActivity(), cursor, true, true));
        } else {
            meetingListView.setVisibility(View.GONE);
            view.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
        }
    }

}
