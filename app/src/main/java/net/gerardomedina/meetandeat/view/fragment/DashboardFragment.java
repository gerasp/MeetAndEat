package net.gerardomedina.meetandeat.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

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
    private NewMeetingDialog newMeetingDialog;

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
                newMeetingDialog = new NewMeetingDialog(getActivity());
                newMeetingDialog.show();

            }
        });
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

    private class NewMeetingDialog extends Dialog {
        private Button okButton;
        private Button cancelButton;
        private int PLACE_PICKER_REQUEST = 1;

        NewMeetingDialog(Activity a) {super(a);}
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_dashboard_new);

            TextView title = (TextView)findViewById(R.id.newMeetingTitleInput);

            final TextView location = (TextView)findViewById(R.id.newMeetingLocationInput);
            location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent intent;
                    try {
                        intent = builder.build(getActivity());
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        Log.e("Google Play",e.getMessage());
                    }
                }
            });

            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    location.setText(place.getAddress());
                }

                @Override
                public void onError(Status status) {
                }
            });


            okButton = (Button) findViewById(R.id.newMeetingOkButton);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            cancelButton = (Button) findViewById(R.id.newMeetingCancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }


    }
}
