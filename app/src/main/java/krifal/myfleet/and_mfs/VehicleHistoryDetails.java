package krifal.myfleet.and_mfs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import krifal.myfleet.and_mfs.Adapter.VehicleHistoryAdapter;
import krifal.myfleet.and_mfs.Bean.VehicleHistoryList;

public class VehicleHistoryDetails extends AppCompatActivity {
    String[] vehiclehistory;
    VehicleHistoryList list;
    ListView listView;
    String vehiclenumber12;
    private ArrayList<VehicleHistoryList> adapterItems = new ArrayList<VehicleHistoryList>();
    ArrayList<String> velist = new ArrayList<String>();
    String[] velist1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_history_list);

        listView = (ListView) findViewById(R.id.list);
        Toolbar toolbar = (Toolbar)findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Bundle b = getIntent().getExtras();
            vehiclehistory = b.getStringArray("vehiclehistory");
            vehiclenumber12 = b.getString("venumber");
        }catch (Exception e){
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(vehiclenumber12);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backarrow);
        int o = vehiclehistory.length;

        for (int i = 0; i < vehiclehistory.length; i++) {
//            velist.add(vehiclehistory[i]);
            velist1 = vehiclehistory[i].split("::");
            String distance = velist1[0];
            String actime = velist1[1];
            String actrip = velist1[2];
            String totalidle = velist1[3];
            String idletrip = velist1[4];
            String engineon = velist1[5];
            String engineoff = velist1[6];
            String sdate = velist1[7];

            adapterItems.add(new VehicleHistoryList(distance, actime, actrip, totalidle, idletrip, engineon, engineoff,sdate));
            Log.d("adapterItems",distance +":"+actime);
        }
        VehicleHistoryAdapter adapter = new VehicleHistoryAdapter(getApplicationContext(),adapterItems);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
