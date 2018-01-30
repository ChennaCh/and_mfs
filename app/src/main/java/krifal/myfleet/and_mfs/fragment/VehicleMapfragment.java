package krifal.myfleet.and_mfs.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import krifal.myfleet.and_mfs.Map.GPSTracker;
import krifal.myfleet.and_mfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleMapfragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    String number,vehiclespeed;
    Button map_satellite,map_normal;
    private TextView speed1,distance1;
    private Handler handler = new Handler();
    Double currene_lat;
    Double current_lang;
    GPSTracker gps;
    private String trucktupe;
    private LinearLayout distance_layout;
    Geocoder geocoder;
    private String tM;
    double lat11 ;
    double lng11 ;
    double lat21 ;
    double lng21 ;

    Double latitude,langitude;
    Double lat1,lang1;
    int i = 1,ignition1;
    private Handler mHandler = new Handler();;
    Marker markerName;

    public void displayReceivedData(String s, String message,String number1,String speed,int ignition) {
        latitude = Double.parseDouble(s);
        langitude = Double.parseDouble(message);
        SharedPreferences preferences = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        trucktupe = preferences.getString("trucktype",String.valueOf(1));

        if (i<=1){
            lat1 = latitude;
            lang1 = langitude;
            i++;
        }
        vehiclespeed = speed;
        speed1.setText(vehiclespeed);
        ignition1 = ignition;

        if (lat1 != Double.parseDouble(s)) {
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(lat1, lang1), new LatLng(latitude, langitude))
                    .width(5)
                    .color(Color.GREEN));
            line.setWidth(10);
            lat1 = latitude;
            lang1 = langitude;

        }

        number = number1;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, langitude)).zoom(16).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
//        map.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ignition1==1){
                            markerName.remove();
                            markerName =map.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title("Vehicle Number").
                                    snippet(number).icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow)));
                        }else {
                            markerName = map.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title("Vehicle Number").
                                    snippet(number).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                        }

                        try {
                            Thread.sleep(100);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();

       currentLocation();
    }

    private void currentLocation() {
        LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        if (location !=null){
            currene_lat = location.getLatitude();
            current_lang = location.getLongitude();
        }
        lat11 = currene_lat;
        lng11 = current_lang;
//        lat21 = latitude;
//        lng21 = langitude;
        new GetDistanceAsyncTask().execute();
    }

    public VehicleMapfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_vehicle_mapfragment, container, false);
        speed1 = (TextView) view.findViewById(R.id.vehiclespeed);
        distance_layout = (LinearLayout) view.findViewById(R.id.distance_layout);
        distance1 = (TextView) view.findViewById(R.id.vehicle_distance);
        onMapReady(map);


        map_satellite = (Button) view.findViewById(R.id.map_satellite);
        map_normal = (Button) view.findViewById(R.id.map_normal);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.vehiclemap);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map_satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        map_normal.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gps = new GPSTracker(getActivity());
        currene_lat = gps.getLatitude();
        current_lang = gps.getLongitude();
    }

    private class GetDistanceAsyncTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String distance = "";
            String url = "http://maps.google.com/maps/api/directions/xml?origin="+lat11+","+lng11+"&destination="+latitude+","+langitude+"&sensor=false&units=metrics";
            String tag[] = {"text"};

            URL url1 = null;
            try {

                url1 = new URL(url);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection)url1.openConnection();
                connection.setReadTimeout(30000);
                connection.setConnectTimeout(30000);
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();

                InputStream is = connection.getInputStream();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(is);
                if (doc != null){
                    NodeList nl;
                    ArrayList args = new ArrayList();
                    for (String s : tag){
                        nl = doc.getElementsByTagName(s);
                        if (nl.getLength() > 0){
                            Node node = nl.item(nl.getLength() - 1);
                            Node node1 = nl.item(nl.getLength() - 2);
                            args.add(node.getTextContent());
                            args.add(node1.getTextContent());

                        }else {
                            args.add(" - ");
                        }
                    }
                    distance = String.format("%s",args.get(0));
                    tM = String.format("%s",args.get(1));
                }else {
                    System.out.print("Doc is null");

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return distance;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                distance1.setText(s);

            }
        }
    }
}
