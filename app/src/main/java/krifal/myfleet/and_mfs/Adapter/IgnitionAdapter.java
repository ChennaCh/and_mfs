package krifal.myfleet.and_mfs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import krifal.myfleet.and_mfs.R;

/**
 * Created by chinn on 8/2/2017.
 */

public class IgnitionAdapter extends RecyclerView.Adapter<IgnitionAdapter.MyViewHolder> {
    private ArrayList<String> drawerlist;
    private Context context;
    private String mItem,truck;
    public List<IgnitionAdapter> items;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.txt);
            imageView = (ImageView) view.findViewById(R.id.img);
        }
    }

    public IgnitionAdapter(ArrayList<String> vehiclelist, Context context,String truck1) {
        this.drawerlist = vehiclelist;
        this.truck = truck1;
    }

    @Override
    public IgnitionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_list_adapter, parent, false);
        return new IgnitionAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(drawerlist.get(position));
        if (truck.equals("MTS")){
            holder.imageView.setImageResource(R.drawable.truck);
        }else if (truck.equals("MFS")){
            holder.imageView.setImageResource(R.drawable.security);
        }
    }

    @Override
    public int getItemCount() {
        return drawerlist.size();
    }
}
