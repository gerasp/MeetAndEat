package net.gerardomedina.meetandeat.view.activity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Meeting;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Meeting meeting = appCommon.getSelectedMeeting();
        String [] latlng = meeting.getLocation().split(",");
        LatLng location = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
        map.addMarker(new MarkerOptions().position(location).title(meeting.getTitle()));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
