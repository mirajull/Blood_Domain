package com.example.user.blood_domain1.Activities;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.blood_domain1.Database.LoginDataBaseAdapter;
import com.example.user.blood_domain1.Models.User;
import com.example.user.blood_domain1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.example.user.blood_domain1.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String address;
    private String bloodgroup;

    //
    private static final double DEFAULT_RADIUS_METERS = 2800;
    private static final double RADIUS_OF_EARTH_METERS = 6371009;

    private static final int MAX_WIDTH_PX = 50;
    private static final int MAX_HUE_DEGREES = 360;
    private static final int MAX_ALPHA = 255;

    private static final int PATTERN_DASH_LENGTH_PX = 100;
    private static final int PATTERN_GAP_LENGTH_PX = 200;
    private static final Dot DOT = new Dot();
    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);
    private static final List<PatternItem> PATTERN_DASHED = Arrays.asList(DASH, GAP);
    private static final List<PatternItem> PATTERN_MIXED = Arrays.asList(DOT, GAP, DOT, DASH, GAP);


    private List<DraggableCircle> mCircles = new ArrayList<>(1);


    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            address = extras.getString("address");
            bloodgroup = extras.getString("bloodgroup");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0)
            {

                Double lat = (double) (addresses.get(0).getLatitude());
                Double lang = (double) (addresses.get(0).getLongitude());
                Log.d("lat-long of user ", "" + lat + "......." + lang);
                LatLng search=new LatLng(lat,lang);
                ArrayList<User> latLngs= LoginDataBaseAdapter.getUsersLocation(lat,lang,bloodgroup);
                if(latLngs==null)
                {
                    Toast.makeText(getApplicationContext(), "No Donor Found! ", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Total "+latLngs.size()+" donor(s) found", Toast.LENGTH_LONG).show();
                for( User user : latLngs ) {
                    LatLng latLng=new LatLng(user.getLat(),user.getLang());
                    Marker donor = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(user.getUsername()));
                    donor.showInfoWindow();
                }
                DraggableCircle circle = new DraggableCircle(search, DEFAULT_RADIUS_METERS);
                mCircles.add(circle);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(search, 14.05f));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14.05f), 2000, null);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private class DraggableCircle {
        //private final Marker mCenterMarker;
        private final Circle mCircle;
        private double mRadiusMeters;

        public DraggableCircle(LatLng center, double radiusMeters) {
            mRadiusMeters = radiusMeters;
            /*mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));*/
            mCircle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radiusMeters)
                    .strokeColor(Color.RED));
        }
    }
    private static LatLng toRadiusLatLng(LatLng center, double radiusMeters) {
        double radiusAngle = Math.toDegrees(radiusMeters / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }


}
