package krifal.myfleet.and_mfs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import krifal.myfleet.and_mfs.R;

/**
 * Created by chinn on 6/30/2017.
 */

public class VehicalListAdapter extends RecyclerView.Adapter<VehicalListAdapter.MyViewHolder> {
    private ArrayList<HashMap<String,String>> drawerlist;
    private Context context;
    private String mItem,trucking;
    public List<VehicalListAdapter> items;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView,status;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.txt);
            imageView = (ImageView) view.findViewById(R.id.img);
            status = (ImageView) view.findViewById(R.id.vstatus1);
        }
    }

    public VehicalListAdapter(ArrayList<HashMap<String,String>> vehiclelist, Context context, String truck) {
        this.drawerlist = vehiclelist;
        this.trucking = truck;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_list_adapter, parent, false);
        return new MyViewHolder(itemView);

    }

//    public void  setFilters(ArrayList<HashMap<String,String>> newlist){
////        drawerlist = new ArrayList<>();
//        drawerlist=newlist;
//        notifyDataSetChanged();
//    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            HashMap<String,String> map = drawerlist.get(position);
            holder.textView.setText(map.get("vechcle1"));
            String s1 = (String) map.get("vehicle2");
        int i;
        if (s1.equals("3")){
            holder.status.setImageResource(R.drawable.red);
        }else if (s1.equals("2")){
            holder.status.setImageResource(R.drawable.yellow);
        }else if (s1.equals("1")){
            holder.status.setImageResource(R.drawable.green);
        } else if (s1.equals("4")){
            holder.status.setImageResource(R.drawable.bluecircle);
        }


//        HashMap<String,String> map = drawerlist.get(position);
//        String s1 = (String) map.get("vechcle1");
//        String s2 = (String) map.get("vehicle2");
//
//        holder.textView.setText(s1);
//        holder.status1.setText(s2.toString());


        if (trucking.equals("MTS")){
            holder.imageView.setImageResource(R.drawable.truck);
        }else if (trucking.equals("MFS")){
            holder.imageView.setImageResource(R.drawable.security);
        }
    }

    @Override
    public int getItemCount() {
        return drawerlist.size();
    }
}