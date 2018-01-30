package krifal.myfleet.and_mfs.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import krifal.myfleet.and_mfs.Map.GPSTracker;
import krifal.myfleet.and_mfs.R;


public class BottomNavigationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    ActionBar actionBar;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    String ignitionstatusdata;
    int acstatusdata=0;
    float speeddata=0,temperatue1;
    String datetimedata="",vehiclenumber1;
    String vehiclecodedata="";
    String addressdata="";
    TextView address,vehiclecode,igstatus,acstatus,speed,datetime,temperature,vehiclenumber;
    Button backbtn;
    String ignition="";
    String ac="";
    double lat,lang;
    String txtUserId,txtPwd;
    String vehiclelist[]=new String[0];
    Button map_satellite,map_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vehicle_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        vehiclenumber = (TextView) findViewById(R.id.TextView01);
        address=(TextView)findViewById(R.id.txtAddress);
        igstatus=(TextView)findViewById(R.id.txtignitionstatus1);
        acstatus=(TextView)findViewById(R.id.txtacstatus2);
        speed=(TextView)findViewById(R.id.txtspeed1);
        datetime=(TextView)findViewById(R.id.txtdatetime2);
        temperature=(TextView)findViewById(R.id.txttemperature);
        map_satellite = (Button)findViewById(R.id.map_satellite);
        map_normal = (Button)findViewById(R.id.map_normal);


        Bundle b = getIntent().getExtras();
        vehiclenumber1 = b.getString("vehiclenumber");
        speeddata=b.getFloat("speed");
//        vehiclelist=b.getStringArray("vehiclelist");
        addressdata=b.getString("address");
        ignitionstatusdata = b.getString("ignitionstatus");
        datetimedata=b.getString("datetime");
//        vehiclecodedata=b.getString("vehiclecode");
//        txtUserId=b.getString("txtUserId");
//        txtPwd=b.getString("txtPwd");
        lat = b.getDouble("lat");
        lang = b.getDouble("lng");
        temperatue1 = b.getFloat("temprature");
//        if(ignitionstatusdata==1){
//            ignition="On";
//        }
//        else
//            ignition="Off";
        acstatusdata = b.getInt("acstatus");
        if(acstatusdata==1){
            ac="On";
        }
        else
            ac="Off";
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        vehiclecode.setText(vehiclecodedata);
        vehiclenumber.setText(vehiclenumber1);
        igstatus.setText(ignitionstatusdata);
        acstatus.setText(ac);
        speed.setText(Float.toString(speeddata));
        datetime.setText(datetimedata);
        address.setText(addressdata);
        address.setSingleLine(false);
        temperature.setText(Float.toString(temperatue1));

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        gps = new GPSTracker(BottomNavigationActivity.this);
        LatLng mylocaiton = new LatLng(lat,lang);
//

        map_satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        map_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lang)).title("Vehicle Number").snippet(""+vehiclenumber1));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mylocaiton).zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocaiton,15));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
            mMap.setMyLocationEnabled(true);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]  {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION );
            }


        }
    }
}