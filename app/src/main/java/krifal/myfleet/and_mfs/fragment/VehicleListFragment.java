package krifal.myfleet.and_mfs.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import krifal.myfleet.and_mfs.Adapter.VehicalListAdapter;
import krifal.myfleet.and_mfs.R;
import krifal.myfleet.and_mfs.RecyclerItemClickListener;
import krifal.myfleet.and_mfs.activity.VehicleDetailViewPager;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */

public class VehicleListFragment extends Fragment {
    private RecyclerView recyclerView;
    private VehicalListAdapter mAdapter;
    private ProgressBar prgbar;
    String txtUserId,txtPwd;
    String vehiclenumber;
    ArrayList<String> vehiclelist = new ArrayList<String>();
    ArrayList<String> clintid = new ArrayList<String>();
    ArrayList<String> vehiclestatusid = new ArrayList<String>();
    ArrayList<HashMap<String,String>> vehiclestatus = new ArrayList<>();
    String trucking;
    private String vehicledata,vehiclestatusdata;


    public VehicleListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        prgbar = (ProgressBar) view.findViewById(R.id.progressBar1);

        SharedPreferences preferences = getActivity().getSharedPreferences("app", MODE_PRIVATE);
        trucking = preferences.getString("trucktype",String.valueOf(1));

        try {

            Bundle b = getActivity().getIntent().getExtras();
            vehiclelist = b.getStringArrayList("res1");
            txtUserId=b.getString("userId");
            vehiclestatusid = b.getStringArrayList("vehstatus");
            txtPwd=b.getString("pwd");
            clintid=b.getStringArrayList("clientid");

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
            contact.put("vehicle2",new Integer(vehiclestatusdata).toString());
            // adding contact to contact list
            vehiclestatus.add(contact);
        }


        mAdapter = new VehicalListAdapter(vehiclestatus,getContext(),trucking);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                vehiclenumber = vehiclelist.get(position);

                Intent intent = new Intent(getContext(), VehicleDetailViewPager.class);
                intent.putExtra("userid",txtUserId);
                intent.putExtra("pass",txtPwd);
                intent.putExtra("vehiclenumber",vehiclenumber);
                startActivity(intent);



//                url="http://ind.app.myfleetservices.com/taskdvcservice/GetVehicleDetails?uid="+txtUserId +"&pwd="+txtPwd+"&fid="+vehiclenumber;
//                progressBar.setVisibility(View.VISIBLE);
//                new PostDataToserverforvehicledata().execute(url);
//                h = new Handler();
//                delay = 1000; //milliseconds
//                h.postDelayed(new Runnable(){
//                    public void run(){
//
//                        h.postDelayed(this, delay);
//                    }
//                }, delay);


            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }

        }
        ));
        return view;

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.search,menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(this);
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        newText = newText.toLowerCase();
//        ArrayList<String> newlist = new ArrayList<>();
//        for ( String vehicle : vehiclelist ){
//            String name = vehicle.getClass().toString();
//            if (name.contains(newText)){
//                newlist.add(vehicle);
//            }
//        }
//        return true;
//    }
}
