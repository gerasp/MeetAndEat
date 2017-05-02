package net.gerardomedina.meetandeat.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Meeting;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Meeting meeting = appCommon.getSelectedMeeting();

        map.addMarker(new MarkerOptions().position(meeting.getLocation()).title(meeting.getTitle()));
        map.moveCamera(CameraUpdateFactory.newLatLng(meeting.getLocation()));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
