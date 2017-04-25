package net.gerardomedina.meetandeat.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.GetMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.MeetingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends BaseFragment {


    private View view;
    private CalendarView calendarView;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        
//        FloatingActionButton newMeetingButton = (FloatingActionButton) view.findViewById(R.id.newMeetingButton);
//        new GetMeetingsTask(this, 1).execute();
        return view;
    }
//
//    public void populateDashboard(JSONObject response) throws JSONException {
//        ListView resultsListView = (ListView) view.findViewById(R.id.meetings);
//        HashMap<String, String> meetings = new HashMap<>();
//        meetings.put(response.getJSONObject("0").getString("description"), response.getJSONObject("0").getString("datetime"));
//        List<HashMap<String, String>> listItems = new ArrayList<>();
//        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.fragment_dashboard_item,
//                new String[]{"First Line", "Second Line"},
//                new int[]{R.id.meeting_label, R.id.meeting_date});
//        for (Object o : meetings.entrySet()) {
//            HashMap<String, String> resultsMap = new HashMap<>();
//            Map.Entry pair = (Map.Entry) o;
//            resultsMap.put("First Line", pair.getKey().toString());
//            resultsMap.put("Second Line", pair.getValue().toString());
//            listItems.add(resultsMap);
//        }
//        resultsListView.setAdapter(adapter);
//        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((BaseActivity) getActivity()).changeToActivity(MeetingActivity.class);
//            }
//        });
//
//    }

}
