package net.gerardomedina.meetandeat.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.persistence.local.DBHelper;
import net.gerardomedina.meetandeat.persistence.local.MeetingValues;
import net.gerardomedina.meetandeat.task.GetMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.NewMeetingActivity;
import net.gerardomedina.meetandeat.view.adapter.MeetingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends BaseFragment implements InitiableFragment {


    private View view;
    private ListView meetingListView;
    private DBHelper dbHelper;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init();
        return view;
    }

    public void init() {
        FloatingActionButton newMeetingButton = (FloatingActionButton) view.findViewById(R.id.newMeetingButton);
        meetingListView = (ListView) view.findViewById(R.id.meetings);
        dbHelper = new DBHelper(getActivity());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetMeetingsTask(getBaseFragment());
            }
        });
        newMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().changeToActivity(NewMeetingActivity.class);
                getActivity().overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_bottom);
            }
        });
        if (appCommon.hasInternet(getActivity())) new GetMeetingsTask(this).execute();
        else loadMeetingListFromLocalDB();
    }

    public void saveMeetingListToLocalDB(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MeetingValues.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonObject = results.getJSONObject(i);
            values.put(MeetingValues._ID, jsonObject.getInt("id"));
            values.put(MeetingValues.COLUMN_NAME_TITLE, jsonObject.getString("title"));
            values.put(MeetingValues.COLUMN_NAME_LOCATION, jsonObject.getString("location"));
            values.put(MeetingValues.COLUMN_NAME_DATETIME, jsonObject.getString("datetime"));
            values.put(MeetingValues.COLUMN_NAME_COLOR, jsonObject.getString("color"));
            db.insert(MeetingValues.TABLE_NAME, null, values);
        }
        loadMeetingListFromLocalDB();

    }

    public void loadMeetingListFromLocalDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+MeetingValues.TABLE_NAME+
                " order by " +MeetingValues.COLUMN_NAME_DATETIME+" ASC;",null);
        meetingListView.setAdapter(new MeetingAdapter(getActivity(), (BaseActivity) getActivity(),cursor,true,false));
    }
}
