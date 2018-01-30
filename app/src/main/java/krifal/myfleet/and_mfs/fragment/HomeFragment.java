package krifal.myfleet.and_mfs.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import krifal.myfleet.and_mfs.R;
import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private LinearLayout relativelayout;
    CoordinatorLayout cordnitae;
    PieChart pieChart;
    Button button;
    LinearLayout linearLayout;
    TextView vehiclesize, ignition_on, ignition_off, idle_vehicles;
    String txtUserId, txtPwd, clintid;
    ArrayList<String> vehiclelist = new ArrayList<String>();
    int i;
    static String TASK_FLLET_APP_LOG = "TASK_FLLET_APP_LOG";
    int ion, ioff, value, idleve, ion1, ioff1, idleve1;
    Handler h;
    int delay;
    ProgressBar progress;
    LinearLayout noofvehicles, engineooff, engineon, notrespondingvehicles;
    int l = 0;
    String name = "";
    ArrayList<String> ignon = new ArrayList<>();
    ArrayList<String> ignoff = new ArrayList<>();
    ArrayList<String> idle = new ArrayList<>();
    ArrayList<String> ignon1 = new ArrayList<>();
    ArrayList<String> ignoff1 = new ArrayList<>();
    ArrayList<String> idle1 = new ArrayList<>();
    String urlString2 = "";
    GifImageView green, red;
    FrameLayout framelayout1;
    private String currentDateandTime;
    int km;

    Date d1;
    Date d2;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        noofvehicles = (LinearLayout) view.findViewById(R.id.linearnoofvehicles);
        engineooff = (LinearLayout) view.findViewById(R.id.engineoff);
        engineon = (LinearLayout) view.findViewById(R.id.engineon);
        notrespondingvehicles = (LinearLayout) view.findViewById(R.id.linear1);
        pieChart = (PieChart) view.findViewById(R.id.piechart);
        vehiclesize = (TextView) view.findViewById(R.id.no_of_vehicles);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear);
        ignition_on = (TextView) view.findViewById(R.id.ignition_on);
        ignition_off = (TextView) view.findViewById(R.id.ignition_off);
        progress = (ProgressBar) view.findViewById(R.id.home_progressbar);
        relativelayout = (LinearLayout) view.findViewById(R.id.relative);
        cordnitae = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        idle_vehicles = (TextView) view.findViewById(R.id.vehicle_not_responding);
//        green = (GifImageView) view.findViewById(R.id.green_gif);
//        red = (GifImageView) view.findViewById(R.id.red_gif);
        framelayout1 = (FrameLayout) view.findViewById(R.id.employeeAddressFragmentId);
        final HomeFragment frag = new HomeFragment();
        setRetainInstance(true);
        currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString();


        SharedPreferences getvalue = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        km = getvalue.getInt("kvalue", 0);

        noofvehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                framelayout1.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.employeeAddressFragmentId, new VehicleListFragment());
                fragmentTransaction.replace(R.id.relative, new Sample());
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        notrespondingvehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share = getActivity().getSharedPreferences("idlevehicles", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                if (km == 1) {
                    editor.putString("idleveh", TextUtils.join(",", idle));
                } else {
                    editor.putString("idleveh", TextUtils.join(",", idle1));
                }
                editor.commit();
                if (idleve == 0 && idleve1 == 0) {
                    Toast.makeText(getContext(), "No Vehicles", Toast.LENGTH_SHORT).show();
                } else {
                    framelayout1.setVisibility(View.VISIBLE);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.employeeAddressFragmentId, new IdleVehicleFragment());
                    fragmentTransaction.replace(R.id.relative, new Sample());
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            }
        });

        engineooff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share = getActivity().getSharedPreferences("ignitioff", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                if (km == 1) {
                    editor.putString("ignoff", TextUtils.join(",", ignoff));
                } else {
                    editor.putString("idleveh", TextUtils.join(",", ignoff1));
                }
                editor.commit();
                if (ioff == 0 && ioff1 == 0) {
                    Toast.makeText(getContext(), "No Vehicles", Toast.LENGTH_SHORT).show();
                } else {
                    framelayout1.setVisibility(View.VISIBLE);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.employeeAddressFragmentId, new IgnitionOffFragment());
                    fragmentTransaction.replace(R.id.relative, new Sample());
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            }
        });

        engineon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share = getActivity().getSharedPreferences("ignition", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                if (km == 1) {
                    editor.putString("ign", TextUtils.join(",", ignon));
                } else {
                    editor.putString("idleveh", TextUtils.join(",", ignon1));
                }
                editor.commit();
                if (ion == 0 && ion1 == 0) {
                    Toast.makeText(getContext(), "No Vehicles", Toast.LENGTH_SHORT).show();

                } else {
                    framelayout1.setVisibility(View.VISIBLE);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.employeeAddressFragmentId, new IgnitionOnFragment());
                    fragmentTransaction.replace(R.id.relative, new Sample());
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            }
        });


        try {
            Bundle b = getActivity().getIntent().getExtras();
            i = b.getInt("val", 0);
            vehiclelist = b.getStringArrayList("res1");
            txtUserId = b.getString("userId");
            txtPwd = b.getString("pwd");
            clintid = b.getString("clientid");

            vehiclesize.setText(Integer.toString(i));
            Log.d(TASK_FLLET_APP_LOG, "output" + vehiclesize);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ion = 0;
        ioff = 0;
        value = 0;
        idleve = 0;
        progress.setVisibility(View.VISIBLE);

        if (km == 1) {

            Toast.makeText(getContext(), "Wait Till the Graph Load", Toast.LENGTH_LONG).show();
            for (l = 0; l < vehiclelist.size(); l++) {
                name = vehiclelist.get(l).toString();
                String url = "http://ind.app.myfleetservices.com/taskdvcservice/GetVehicleDetails?uid=" + txtUserId + "&pwd=" + txtPwd + "&fid=" + name;
                urlString2 = url.replaceAll(" ", "%20");
                new PostDataToserverforvehicledata12().execute(urlString2);

            }
        } else {

            SharedPreferences share = getContext().getSharedPreferences("ToatalData", MODE_PRIVATE);
            String idlevehicless = share.getString("idleveh1", null);
            String engineonn = share.getString("ign1", null);
            String engineoff = share.getString("ignoff1", null);
            idle1 = new ArrayList<String>(Arrays.asList(idlevehicless.split(",")));
            ignon1 = new ArrayList<String>(Arrays.asList(engineonn.split(",")));
            ignoff1 = new ArrayList<String>(Arrays.asList(engineoff.split(",")));
            ion1 = share.getInt("engintoncount", 0);
            ioff1 = share.getInt("engineoffcount", 0);
            idleve1 = share.getInt("idlecount", 0);

            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(i, 0));
            entries.add(new Entry(idleve1, 1));
            entries.add(new Entry(ion1, 2));
            entries.add(new Entry(ioff1, 3));

            final PieDataSet dataset = new PieDataSet(entries, "");
            dataset.setSliceSpace(3f);
            dataset.setSelectionShift(5f);

            final ArrayList labels = new ArrayList<>();
            labels.add("All Vehicles");
            labels.add("Not Responding");
            labels.add("Ignition ON");
            labels.add("Ignition OFF");

            final PieData data = new PieData(labels, dataset);
//                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            dataset.setValueTextSize(8f);

            final int[] MY_COLORS = {Color.rgb(35, 172, 240), Color.rgb(155, 70, 235), Color.rgb(255, 255, 0), Color.rgb(255, 69, 0)};
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : MY_COLORS) colors.add(c);

            dataset.setColors(colors);

            progress.setVisibility(View.GONE);
            pieChart.setData(data);
            pieChart.animateY(0);

            ignition_on.setText(Integer.toString(ion1));
//            ignition_on.setTextColor(Color.rgb(255, 255, 255));
            ignition_off.setText(Integer.toString(ioff1));
            idle_vehicles.setText(Integer.toString(idleve1));

        }
//        enginestatus();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (km == 1) {
            SharedPreferences share = getActivity().getSharedPreferences("ToatalData", MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();
            editor.putString("idleveh1", TextUtils.join(",", idle));
            editor.putString("ign1", TextUtils.join(",", ignon));
            editor.putString("ignoff1", TextUtils.join(",", ignoff));
            editor.putInt("idlecount", idleve);
            editor.putInt("engintoncount", ion);
            editor.putInt("engineoffcount", ioff);
            editor.commit();
        }
    }
    //    private void enginestatus() {
//        if (ion == 0 ){
//            green.setVisibility(View.GONE);
//        }else if (ioff == 0 ){
//            red.setVisibility(View.GONE);
//        }
//    }

    protected class PostDataToserverforvehicledata12 extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                String vehclno;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                String str = arg0[0];
                String[] str1 = str.split("=");
                String str2 = str1[3];
                vehclno = str2.replace("%20", " ");
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result = "";
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpclient.getConnectionManager().shutdown();
                String[] vls = result.split(",");
                final int ignitionstatus = Integer.parseInt(vls[3]);
                final String pastdate = vls[6].toString();
                final String res = result;

                d1 = simpleDateFormat.parse(currentDateandTime);
                d2 = simpleDateFormat.parse(pastdate);

                long different = d1.getTime() - d2.getTime();

                long diffMinutes = different / (60 * 1000);

                if (diffMinutes > 20) {
                    idleve++;
                    idle.add(vehclno);
                } else {
                    try {
                        if (vehclno.length() > 0) {
                            switch (ignitionstatus) {
                                case 1:
                                    ion++;
                                    ignon.add(vehclno);
                                    break;
                                case 0:
                                    ioff++;
                                    ignoff.add(vehclno);
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        vehicledetails12(ignitionstatus, res, ion, ioff);
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    private void vehicledetails12(int ignitionstatus, String result, int ion, int ioff) {

        if (!(result.equalsIgnoreCase("0,0"))) {
            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(i, 0));
            entries.add(new Entry(idleve, 1));
            entries.add(new Entry(ion, 2));
            entries.add(new Entry(ioff, 3));

            final PieDataSet dataset = new PieDataSet(entries, "");

            final ArrayList labels = new ArrayList<>();
            labels.add("All Vehicles");
            labels.add("Not Responding");
            labels.add("Ignition ON");
            labels.add("Ignition OFF");

            final PieData data = new PieData(labels, dataset);
//                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            dataset.setValueTextSize(8f);

            final int[] MY_COLORS = {Color.rgb(35, 172, 240), Color.rgb(155, 70, 235), Color.rgb(255, 255, 0), Color.rgb(255, 69, 0)};
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : MY_COLORS) colors.add(c);

            dataset.setColors(colors);
            data.setValueTextColor(Color.rgb(255, 255, 255));



            progress.setVisibility(View.GONE);
            pieChart.setData(data);
            pieChart.animateY(0);

            ignition_on.setText(Integer.toString(ion));
            ignition_off.setText(Integer.toString(ioff));
            idle_vehicles.setText(Integer.toString(idleve));

        } else
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
