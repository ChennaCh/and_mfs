package krifal.myfleet.and_mfs.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import krifal.myfleet.and_mfs.Adapter.IgnitionAdapter;
import krifal.myfleet.and_mfs.R;
import krifal.myfleet.and_mfs.RecyclerItemClickListener;
import krifal.myfleet.and_mfs.activity.VehicleDetailViewPager;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class IgnitionOnFragment extends Fragment {
    private RecyclerView recyclerView;
    private IgnitionAdapter mAdapter;
    private ProgressBar prgbar;

    String txtUserId,txtPwd,clintid;
    ArrayList<String> vehiclelist = new ArrayList<String>();
    String vehiclenumber,trucking;


    public IgnitionOnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        prgbar = (ProgressBar) view.findViewById(R.id.progressBar1);
        TextView txt = (TextView) view.findViewById(R.id.ignition_type);
        txt.setText("IgnitionOn Vehicles");
        txt.setTextColor(Color.rgb(0,0,0));
        txt.setVisibility(View.VISIBLE);

        try {

            Bundle b = getActivity().getIntent().getExtras();
            vehiclelist = b.getStringArrayList("res1");
            txtUserId=b.getString("userId");
            txtPwd=b.getString("pwd");
            clintid=b.getString("clientid");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SharedPreferences share  = getActivity().getSharedPreferences("ignition", Context.MODE_PRIVATE);
        String serialized = share.getString("ign", null);

        SharedPreferences preferences = getActivity().getSharedPreferences("app", MODE_PRIVATE);
        trucking = preferences.getString("trucktype",null);

//        String serialized = getArguments().getString("ign");
        final ArrayList<String>engineon = new ArrayList<String>(Arrays.asList(serialized.split(",")));

        mAdapter = new IgnitionAdapter(engineon,getContext(),trucking);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                vehiclenumber = engineon.get(position);

                Intent intent = new Intent(getContext(), VehicleDetailViewPager.class);
                intent.putExtra("userid",txtUserId);
                intent.putExtra("pass",txtPwd);
                intent.putExtra("vehiclenumber",vehiclenumber);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }

        }
        ));

        return view;
    }

}
