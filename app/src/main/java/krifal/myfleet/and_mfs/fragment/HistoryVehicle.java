package krifal.myfleet.and_mfs.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.HashMap;

import krifal.myfleet.and_mfs.Adapter.VehicalListAdapter;
import krifal.myfleet.and_mfs.R;
import krifal.myfleet.and_mfs.RecyclerItemClickListener;
import krifal.myfleet.and_mfs.VehicleHistoryDetails;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryVehicle extends Fragment {
    private RecyclerView recyclerView;
    private VehicalListAdapter mAdapter;
    private ProgressBar prgs;
    String txtUserId, txtPwd, clintid, mobilenumber;
    String vehiclenumber;
    ArrayList<String> vehiclelist = new ArrayList<String>();
    String phone;
    private String vehicledata,vehiclestatusdata;
    ArrayList<String> vehiclestatusid = new ArrayList<String>();
    ArrayList<HashMap<String,String>> vehiclestatus = new ArrayList<>();

    static String TASKLOG = "TASK ACTIVITY";

    String res,trucktype;


    public HistoryVehicle() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        prgs = (ProgressBar) view.findViewById(R.id.progressBar1);


        try {

            Bundle b = getActivity().getIntent().getExtras();
            vehiclelist = b.getStringArrayList("res1");
            txtUserId = b.getString("userId");
            vehiclestatusid = b.getStringArrayList("vehstatus");
            txtPwd = b.getString("pwd");
            clintid = b.getString("clientid");
//            mobilenumber = b.getString("phone");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i=0; i < vehiclelist.size(); i++){
            vehicledata = vehiclelist.get(i);
            vehiclestatusdata = vehiclestatusid.get(i);
            // tmp hash map for single contact
            HashMap<String, String> contact = new HashMap<>();
            contact.put("vechcle1",vehicledata);
            contact.put("vehicle2",vehiclestatusdata);
            // adding contact to contact list
            vehiclestatus.add(contact);
        }


        SharedPreferences preferences = getActivity().getSharedPreferences("app", MODE_PRIVATE);
        phone = preferences.getString("phone", String.valueOf(0));
        trucktype = preferences.getString("trucktype",String.valueOf(0));

        mAdapter = new VehicalListAdapter(vehiclestatus, getContext(),trucktype);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                try {
                    vehiclenumber = vehiclelist.get(position);
                    String url = "http://ind.app.mytruckservices.com/taskdvcservice/vehicleweekreport?uid=" + txtUserId + "&pwd=" + txtPwd + "&fid=" + vehiclenumber + "&cell=" + phone;
                    String urlString = url.replaceAll(" ", "%20");
                    new PostDataToserverforvehicledata().execute(urlString);
                    prgs.setVisibility(View.VISIBLE);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onItemLongClick(View view, int position) {

                Toast.makeText(getContext(), "LongClicked", Toast.LENGTH_SHORT).show();
                // ...
            }
        }));

        return view;
    }

    private class PostDataToserverforvehicledata extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result = "";
                try {
                    result = httpclient.execute(request, handler);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpclient.getConnectionManager().shutdown();
                res = result.trim();


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        int a = res.length();
                        if (res.length() > 0) {
                            gotoSettingshistory(res);
                        }else {
                            prgs.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

    }

    private void gotoSettingshistory(String res1) {
        Log.i(TASKLOG, "%%%%%%%%%% Login Result : " + res1);

        try {
            if (res1 != null && !res1.equalsIgnoreCase("[]") && !res1.equalsIgnoreCase("")) {
//                String resultout,resultout1;
//                resultout1 = res1.replace("[", "");
//                resultout = resultout1.replace("]", "");

                String[] vehlist2 = res1.split(";;");

                Intent intent = new Intent(getActivity(), VehicleHistoryDetails.class);
                intent.putExtra("vehiclehistory",vehlist2);
                intent.putExtra("venumber",vehiclenumber);
                startActivity(intent);
                prgs.setVisibility(View.GONE);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
        return ;
    }
}
