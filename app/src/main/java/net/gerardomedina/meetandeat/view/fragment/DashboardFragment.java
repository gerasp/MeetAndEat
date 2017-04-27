package net.gerardomedina.meetandeat.view.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.GetMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MeetingActivity;
import net.gerardomedina.meetandeat.view.activity.NewMeetingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends BaseFragment {
    private View view;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        FloatingActionButton newMeetingButton = (FloatingActionButton) view.findViewById(R.id.newMeetingButton);
        newMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).changeToActivity(NewMeetingActivity.class);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top);
            }
        });
        new GetMeetingsTask(this).execute();
        return view;
    }

    public void populateDashboard(JSONObject response) throws JSONException {
        ListView resultsListView = (ListView) view.findViewById(R.id.meetings);
        HashMap<String, String> meetings = new HashMap<>();
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            meetings.put(results.getJSONObject(i).getString("title"),
                    results.getJSONObject(i).getString("date")+
                    results.getJSONObject(i).getString("time")
            );
        }
        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.fragment_dashboard_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.meeting_label, R.id.meeting_date});
        for (Object o : meetings.entrySet()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) o;
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }
        resultsListView.setAdapter(adapter);
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BaseActivity) getActivity()).changeToActivity(MeetingActivity.class);
            }
        });

    }

}
