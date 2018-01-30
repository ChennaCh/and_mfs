package krifal.myfleet.and_mfs.Parent.Activity;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import krifal.myfleet.and_mfs.R;


public class ChildrenMapFragment extends Fragment {

    MapView mapView;
    GoogleMap map;
    Marker marker;
    Location location;
    Double latitude,langitude;
    Double lat1,lang1;
    int i = 1;
    private Handler handler = new Handler();
    Button map_satellite1,map_normal1;
    ChildrenMapFragment mapFragment;


    protected void displayReceivedData(String lat, String lang)
    {
//        map.setTrafficEnabled(true);
        latitude = Double.parseDouble(lat);
        langitude = Double.parseDouble(lang);
        if (i<=1){
            lat1 = latitude;
            lang1 = langitude;
            i++;
        }
        if (lat1 != Double.parseDouble(lat)) {
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(lat1, lang1), new LatLng(latitude, langitude))
                    .width(5)
                    .color(Color.GREEN));
            line.setWidth(10);
            lat1 = latitude;
            lang1 = langitude;

        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, langitude)).zoom(17).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
//        map.clear();
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title("Vehicle Number")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow)));
//        map.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title("Vehicle Number"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        map.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title("Vehicle Number"));
                        map.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title("Vehicle Number")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow)));
                        try {
                            Thread.sleep(100);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public ChildrenMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_children_map, container, false);

        map_satellite1 = (Button) view.findViewById(R.id.map_satellite1);
        map_normal1 = (Button) view.findViewById(R.id.map_normal1);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
//        map = mapView.getMap();
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.setMyLocationEnabled(true);

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map_satellite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        map_normal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });


        map.getUiSettings().setZoomControlsEnabled(true);




        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}