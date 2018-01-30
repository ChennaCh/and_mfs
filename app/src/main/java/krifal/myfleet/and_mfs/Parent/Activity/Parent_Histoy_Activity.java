package krifal.myfleet.and_mfs.Parent.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.Calendar;

import krifal.myfleet.and_mfs.R;

public class Parent_Histoy_Activity extends AppCompatActivity {

    MapView mapView;
    GoogleMap map;
    private TextView time1,code2;
    private ProgressBar histroy_progress;
    Calendar mCurrentDate;
    int day, month, year;
    private ImageButton calender_btn;
    private String tripcodehistory;
    private String triptype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent__histoy_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);


        time1 = (TextView) findViewById(R.id.triptime_parent);
        code2 = (TextView) findViewById(R.id.tripcode_parent);
        histroy_progress = (ProgressBar) findViewById(R.id.histroy_progress);
        calender_btn = (ImageButton) findViewById(R.id.calender_btn);

        Bundle b = getIntent().getExtras();
        tripcodehistory = b.getString("codehistory");
        triptype = b.getString("triptype1");

        calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datepickerdialog = new DatePickerDialog(Parent_Histoy_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dayofmonth) {

                        ArrayList<String> fulldate = new ArrayList<String>();
                        StringBuilder builder = new StringBuilder();
                        monthofyear = monthofyear +1;
                        fulldate.add(String.valueOf(year));
                        fulldate.add(String.valueOf(monthofyear));
                        if (dayofmonth == 1 || dayofmonth == 2 || dayofmonth == 3 || dayofmonth == 4 || dayofmonth == 5 || dayofmonth == 6
                                || dayofmonth == 7 || dayofmonth == 8 || dayofmonth == 9){
                            ArrayList<String> dateformat = new ArrayList<String>();
                            int va = 0;
                            dateformat.add(String.valueOf(va));
                            dateformat.add(String.valueOf(dayofmonth));
                            StringBuilder builder1 = new StringBuilder();
                            for (String s2: dateformat) {
                                builder1.append(s2);
                                builder1.toString().trim();
                            }
                            fulldate.add(String.valueOf(builder1));

                        }else {
                            fulldate.add(String.valueOf(dayofmonth));
                        }
                        for (String s : fulldate) {
                            builder.append(s);
                            builder.toString().trim();
                        }

                        if (triptype.equals("PickUp")) {
                            String url = "http://ind.app.taskoncloud.com/taskdvcservice/routeweekreport?uid=admin@chirecinternationalschool.com&pwd=chirec123&tripcode=ROUTE----KP01P_" + builder;
                            String urlString = url.replaceAll(" ", "%20");
                            histroy_progress.setVisibility(View.VISIBLE);
                            map.clear();
                            new PostDataToserverparenthistory().execute(urlString);
                        }else if (triptype.equals("Drop")){
                            String url = "http://ind.app.taskoncloud.com/taskdvcservice/routeweekreport?uid=admin@chirecinternationalschool.com&pwd=chirec123&tripcode=ROUTE---KP01D_" + builder;
                            String urlString = url.replaceAll(" ", "%20");
                            histroy_progress.setVisibility(View.VISIBLE);
                            map.clear();
                            new PostDataToserverparenthistory().execute(urlString);
                        }
                    }
                },year,month,day);
                datepickerdialog.show();
            }
        });

        mapView = (MapView) findViewById(R.id.mapview1);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);

        try {
            MapsInitializer.initialize(Parent_Histoy_Activity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        String url = "http://ind.app.taskoncloud.com/taskdvcservice/routeweekreport?uid=admin@chirecinternationalschool.com&pwd=chirec123&tripcode="+tripcodehistory;
        String urlString = url.replaceAll(" ", "%20");
        histroy_progress.setVisibility(View.VISIBLE);
        new PostDataToserverparenthistory().execute(urlString);
    }

    private class PostDataToserverparenthistory extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {

            HttpClient httpclient=new DefaultHttpClient();
            final HttpGet request=new HttpGet(strings[0]);
            ResponseHandler<String> handler=new BasicResponseHandler();
            String result = "";
            try{
                result=httpclient.execute(request,handler);
            }catch(Exception e){
                e.printStackTrace();
                result="0";
            }
                final String res = result;
                runOnUiThread(new Runnable() {
                    public void run() {
                        vehicledetails1(res);
                        histroy_progress.setVisibility(View.GONE);
                    }
                });
            return null;
        }
    }

    private void vehicledetails1(String res) {

        if (res.equals("\n") || res.equals("") || res.equals(null) || res.equals("0")) {
//                histroy_progress.setVisibility(View.GONE);
            Toast.makeText(this, "Trip Details Not Found Please try with another Date !", Toast.LENGTH_LONG).show();
            map.clear();
        }
        else  if (res != null){
            Double latitutde,langitude,lat=null,lng=null;
            String[] vehlist2 = res.split(";;");
            int vale = vehlist2.length-1;
            for (int i = 0 ; i < vale-1 ; i ++) {
                String[] vehlist1 = vehlist2[i].split("::");
                if (i == 0 || i == vale-2) {
                    String v = vehlist1[3].toString();
                    time1.setText("Time :"+(vehlist1[3]).toString());
                    code2.setText("TripCode :"+(vehlist1[0].toString()));
                    latitutde = Double.valueOf(vehlist1[1]);
                    langitude = Double.valueOf(vehlist1[2]);
                    lat = latitutde;
                    lng = langitude;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitutde, langitude)).zoom(12).build();
                    map.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                    map.addMarker(new MarkerOptions().position(new LatLng(latitutde,
                            langitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Vehicle Number"));
                }else {
                    latitutde = Double.valueOf(vehlist1[1]);
                    langitude = Double.valueOf(vehlist1[2]);
                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(new LatLng(lat, lng), new LatLng(latitutde, langitude))
                            .width(5)
                            .color(Color.RED));
                    line.setWidth(10);
                    lat = latitutde;
                    lng = langitude;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitutde, langitude)).zoom(12).build();
                    map.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
//                    map.addMarker(new MarkerOptions().position(new LatLng(latitutde, langitude))
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Vehicle Number"));

                }
            }
        }
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
