package krifal.myfleet.and_mfs.fragment;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import krifal.myfleet.and_mfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleDetailFragment extends Fragment {

    private Handler h;
    private Runnable myRunnable;
//    int flag = 0;
    static  String  TASK_FLLET_APP_LOG =  "TASK_FLLET_APP_LOG";


    String txtUserId,txtPwd;
    String vehiclenumber;
    String url,urls;
    ProgressBar progress;
    int ignitionstatus1;
    private Double updatedlat = 0.0,updatedlang =0.0;

    SendMessage SM;
    TextView address,igstatus,acstatus,speed,datetime,temperature,vehiclenumber2,status;

    public VehicleDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vehicle_details, container, false);

        progress = (ProgressBar) view.findViewById(R.id.progressdetail);
        vehiclenumber2 = (TextView) view.findViewById(R.id.TextView01);
        address=(TextView) view.findViewById(R.id.txtAddress);
        igstatus=(TextView) view.findViewById(R.id.txtignitionstatus1);
        acstatus=(TextView) view.findViewById(R.id.txtacstatus2);
        speed=(TextView) view.findViewById(R.id.txtspeed1);
        datetime=(TextView) view.findViewById(R.id.txtdatetime2);
        temperature=(TextView) view.findViewById(R.id.txttemperature);
        status = (TextView) view.findViewById(R.id.vehiclenumber_status);



        try {
            Bundle b = getActivity().getIntent().getExtras();
            txtUserId = b.getString("userid");
            txtPwd = b.getString("pass");
            vehiclenumber = b.getString("vehiclenumber");
            progress.setVisibility(View.VISIBLE);

        }catch (Exception e){
            e.printStackTrace();
        }

//        url="http://ind.app.myfleetservices.com/taskdvcservice/GetVehicleDetails?uid="+txtUserId +"&pwd="+txtPwd+"&fid="+vehiclenumber;
//        String urlString = url.replaceAll(" ", "%20");
//        new PostDataToserverforvehicledata().execute(urlString);


        h = new Handler();
        final int delay=3000;
        h.postDelayed(myRunnable = new Runnable(){
            public void run(){
                url="http://ind.app.myfleetservices.com/taskdvcservice/GetVehicleDetails?uid="+txtUserId +"&pwd="+txtPwd+"&fid="+vehiclenumber;
                String urlString = url.replaceAll(" ", "%20");
                new PostDataToserverforvehicledata().execute(urlString);
                h.postDelayed(this, delay);
            }
        }, delay);


        return view;
    }

    //Send Message start
    public interface SendMessage {
        void sendData(String s, String message,String number,String speed,int ignition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }   //Send Message fnish

    private class PostDataToserverforvehicledata extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result="";
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                httpclient.getConnectionManager().shutdown();
                String [] vls= result.split(",");
                int total = vls.length;
                if (7 <= total) {
                    final double lat = Double.parseDouble(vls[0].toString());
                    final double lng = Double.parseDouble(vls[1].toString());
                    final float speed = Float.parseFloat(vls[2]);
                    final int ignitionstatus = Integer.parseInt(vls[3]);
                    final int acstatus = Integer.parseInt(vls[4]);
                    final float temperature1 = Float.parseFloat(vls[5]);
                    final String datetime1 = vls[6].toString();
//                flag = 1;

                    final String res = result;

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progress.setVisibility(View.GONE);
                            vehicledetails(lat, lng, speed, ignitionstatus, acstatus, temperature1, datetime1, res);
                        }
                    });
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            status.setVisibility(View.VISIBLE);
                            status.setText("Not Responding");
                            progress.setVisibility(View.GONE);
                            vehiclenumber2.setText(vehiclenumber);
                            igstatus.setText(": - ");
                            acstatus.setText(": - ");
                            speed.setText(": - ");
                            datetime.setText(": - ");
                            address.setText(" - ");
                            address.setSingleLine(false);
                            temperature.setText(": - ");

                        }
                    });
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "UnSuccess";
        }
    }

    private void vehicledetails(double lat, double lng,float speed1,int ignitionstatus,int acstatus1,float temperatue1,String datetime1, String result){
        if(!(result.equalsIgnoreCase("0,0,0,0,0,0")))
        {
            String addressText="";
            Geocoder geocoder;
            String address1="";
            String city="";
            String country="";
            String result1="";
            ignitionstatus1 = ignitionstatus;
            List<Address> addresses=null;
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                if(addresses.size() > 0) {
                    address1 = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getAddressLine(1);
                    country = addresses.get(0).getAddressLine(2);
                }
                Log.v(TASK_FLLET_APP_LOG, "###### addressText" + address);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            result1 =address1+" "+city+" "+country;
            String ignition="",ac="";
            try {
                if(ignitionstatus==1){
                    ignition="On";
                }
                else {
                    ignition = "Off";
                }
                if(acstatus1==1){
                    ac="On";
                }
                else
                    ac="Off";

                Double lattitude = lat;
                Double longitude = lng;

                vehiclenumber2.setText(vehiclenumber);
                igstatus.setText(": "+ignition);
                acstatus.setText(": "+ac);
                speed.setText(": "+Float.toString(speed1));
                datetime.setText(": "+datetime1);
                address.setText(result1);
                address.setSingleLine(false);
                temperature.setText(": "+Float.toString(temperatue1));

                if (lattitude.equals(updatedlat)) {

                }else {
                    SM.sendData(lattitude.toString(), longitude.toString(), vehiclenumber, Float.toString(speed1), ignitionstatus1);
                    updatedlat = lattitude;
                    updatedlang = longitude;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return;
    }

//    private class PostDataToserverforvehicledata1 extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... arg0) {
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpGet request = new HttpGet(arg0[0]);
//                ResponseHandler<String> handler = new BasicResponseHandler();
//                String result="";
//                try {
//                    result = httpclient.execute(request, handler);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                httpclient.getConnectionManager().shutdown();
//                String [] vls= result.split(",");
//                final double lat=Double.parseDouble(vls[0].toString());
//                final double lng=Double.parseDouble(vls[1].toString());
//                final float speed=Float.parseFloat(vls[2]);
//
//                final String res = result;
//
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        progress.setVisibility(View.GONE);
//                        vehicledetails1(lat,lng,speed,res);
//                    }
//                });
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//    }

//    private void vehicledetails1(double lat, double lng, float speed, String res) {
//        if(!(res.equalsIgnoreCase("0,0,0,0,0,0")))
//        {
//
//            try {
//                Double lattitude = lat;
//                Double longitude = lng;
//
//                SM.sendData(lattitude.toString(),longitude.toString(),vehiclenumber,Float.toString(speed),ignitionstatus1);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return;
//    }

    @Override
    public void onDestroy() {
        h.removeCallbacks(myRunnable);
        super.onDestroy();
    }
}
