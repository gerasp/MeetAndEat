package net.gerardomedina.meetandeat.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.GetMeetingsTask;
import net.gerardomedina.meetandeat.view.activity.BaseActivity;
import net.gerardomedina.meetandeat.view.activity.ScrollingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends BaseFragment {


    private int PLACE_PICKER_REQUEST = 1;
    private ListView meetings;
    private View view;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        FloatingActionButton newMeetingButton = (FloatingActionButton) view.findViewById(R.id.newMeetingButton);
        new GetMeetingsTask(this, 1).execute();
        return view;
    }

    public void populateDashboard(JSONObject response) throws JSONException {
        ListView resultsListView = (ListView) view.findViewById(R.id.meetings);

        HashMap<String, String> meetings = new HashMap<>();
        meetings.put(response.getJSONObject("0").getString("description"), response.getJSONObject("0").getString("datetime"));

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
                ((BaseActivity)getActivity()).changeToActivity(ScrollingActivity.class);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, getActivity());
                // Do something with the place
            }
        }
    }
}
