package krifal.myfleet.and_mfs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import krifal.myfleet.and_mfs.Bean.VehicleHistoryList;
import krifal.myfleet.and_mfs.R;

/**
 * Created by chinn on 7/15/2017.
 */

public class VehicleHistoryAdapter extends BaseAdapter {
    private Context context;
    Activity activity;
    ArrayList<VehicleHistoryList> adapterItems;
    public static Toolbar toolbar;


    public VehicleHistoryAdapter(Context context, ArrayList<VehicleHistoryList> customersList) {
        // super(context, R.layout.vehicle_history_data);
        this.context = context;
        this.adapterItems = customersList;

    }

    @Override
    public int getCount() {
        return adapterItems.size();
    }

    @Override
    public Object getItem(int postion) {
        return adapterItems.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return adapterItems.indexOf(getItem(postion));
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        rowView = mInflater.inflate(R.layout.vehicle_history_data, null);

//        toolbar = (Toolbar) rowView.findViewById(R.id.history_toolbar);
//        setSupportActionBar(toolbar);

        ViewHolder holder;
        holder = new ViewHolder();


        holder.distance = (TextView) rowView.findViewById(R.id.history_distance);
        holder.acime = (TextView) rowView.findViewById(R.id.history_ac_time);
        holder.actrip = (TextView) rowView.findViewById(R.id.history_ac_trip);
        holder.totalidle = (TextView) rowView.findViewById(R.id.history_total_idle);
        holder.idletrip = (TextView) rowView.findViewById(R.id.history_idle_trip);
        holder.engineon = (TextView) rowView.findViewById(R.id.history_engine_on);
        holder.engineooff = (TextView) rowView.findViewById(R.id.history_engine_off);
        holder.date1 = (TextView) rowView.findViewById(R.id.date);

        if (adapterItems.get(position).getDistance() != null) {
            holder.distance.setText(":   " + adapterItems.get(position).getDistance()+" Kms");
        }

         if (adapterItems.get(position).getActime() != null) {
            holder.acime.setText(":   " + adapterItems.get(position).getActime()+" Minutes");
        } if (adapterItems.get(position).getActriptime() != null) {
            holder.actrip.setText(":   " + adapterItems.get(position).getActriptime()+" Minutes");
        }  if (adapterItems.get(position).getIdletrip() != null) {
            holder.totalidle.setText(":   " + adapterItems.get(position).getIdletrip()+" Minutes");
        }  if (adapterItems.get(position).getIdletrip() != null) {
            holder.idletrip.setText(":   " + adapterItems.get(position).getIdletrip()+" Minutes");
        }  if (adapterItems.get(position).getEngineon() != null) {
            holder.engineon.setText(":   " + adapterItems.get(position).getEngineon()+" Minutes");
        }  if (adapterItems.get(position).getEngineoff() != null) {
            holder.engineooff.setText(":   " + adapterItems.get(position).getEngineoff()+" Minutes");
        }
        if (adapterItems.get(position).getDate() != null) {
            holder.date1.setText(adapterItems.get(position).getDate());
        }
        return rowView;
    }

    private class ViewHolder {
        TextView distance, acime, actrip, totalidle, idletrip, engineon, engineooff,date1;
    }
}