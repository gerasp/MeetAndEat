package net.gerardomedina.meetandeat.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class DashboardFragment extends BaseFragment {
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
                ((BaseActivity) getActivity()).changeToActivity(MeetingActivity.class);
            }
        });

    }

    class NewMeetingDialog extends Dialog implements
            android.view.View.OnClickListener {
        private Button okButton;
        private Button cancelButton;

        NewMeetingDialog(Activity a) {super(a);}
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_dashboard_new);
            TextView country = (TextView)findViewById(R.id.newMeetingTitle);
            TextView level = (TextView)findViewById(R.id.friend_level_label);
            TextView totalScore = (TextView)findViewById(R.id.friend_total_score_label);
            ImageView flag = (ImageView)findViewById(R.id.friend_flag);

            setTitle(selectedFriend.getUsername());
            country.setText(new Locale("",selectedFriend.getCountry()).getDisplayCountry());
            level.setText(String.valueOf(selectedFriend.getLevel()));
            totalScore.setText(String.valueOf(selectedFriend.getTotalScore()));
            int drawableId = getResources()
                    .getIdentifier("flag_"+selectedFriend.getCountry().toLowerCase(), "drawable", getActivity().getPackageName());


            okButton = (Button) findViewById(R.id.newMeetingOkButton);
            okButton.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {dismiss();}
    }
}
