package krifal.myfleet.and_mfs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

import krifal.myfleet.and_mfs.Netowrk.NetworkCheck;
import krifal.myfleet.and_mfs.Parent.Activity.ViewPagerCheck;
import krifal.myfleet.and_mfs.R;

public class StartingActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    static String TASKLOG = "TASK ACTIVITY";
    ArrayList<String> velist = new ArrayList<String>();
    String uname, pass, phone, userype,schtype;
    int i, j = 0;
    String res;
    String trucking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_gif);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
/* Create an Intent that will start the Menu-Activity. */

                if (NetworkCheck.getInstance(StartingActivity.this).isOnline()) {

                    SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                    uname = preferences.getString("mail", String.valueOf(0));
                    pass = preferences.getString("pass", String.valueOf(0));
                    phone = preferences.getString("phone", String.valueOf(0));
                    userype = preferences.getString("type", null);
                    trucking = preferences.getString("trucktype",null);
                    schtype = preferences.getString("usertype",null);

                    if (uname.equals("0")) {
                        Intent mainIntent = new Intent(StartingActivity.this, LoginActivity.class);
                        StartingActivity.this.startActivity(mainIntent);
                        StartingActivity.this.finish();
                    } else {

                        if (uname.length() > 0 && pass.length() > 0 && phone.length() > 0 && userype.length() > 0) {
                            if (userype.equals("Admin") || userype.equals("Transport Agency") || userype.equals("Trucking Company")
                                    || userype.equals("Logistric Service Provider") || userype.equals("SecurityCompany")
                                    || userype.equals("MFS_PER") || userype.equals("Personal")
                                    || userype.equals("Warehouse Client") || userype.equals("Consumer goods Manufacture")
                                    || userype.equals("Track Only Customer") || userype.equals("Direct Sales Agentr")) {
                                String url = "http://ind.app.mytruckservices.com/taskdvcservice/al?uid=" + uname + "&pwd=" + pass + "&imei=&cell=" + phone + "&lat=&long=&usertype=" + schtype;
                                String urlString = url.replaceAll(" ", "%20");
                                new PostDataToserverstarting().execute(urlString);
//                            String url = "http://ind.app.taskoncloud.com/taskdvcservice/fleetlist?uid="+uname+"&pwd="+ pass;
//                            String urlString = url.replaceAll(" ", "%20");
//                            new PostDataToserver().execute(urlString);
                            } else if (userype.equals("Parent")) {

                                if (uname.length() > 0 && pass.length() > 0) {
                                    String url = "http://ind.app.taskoncloud.com/taskdvcservice/Parentlogins?action=patientlogin&uid=" + uname + "&pwd=" + pass;

                                    new PostDataToserverrequestStartingParent().execute(url);
                                    return;
                                }

                            }
                            else {
                                Intent mainIntent = new Intent(StartingActivity.this, LoginActivity.class);
                                StartingActivity.this.startActivity(mainIntent);
                                StartingActivity.this.finish();
                            }
                        } else {
                            Intent mainIntent = new Intent(StartingActivity.this, LoginActivity.class);
                            StartingActivity.this.startActivity(mainIntent);
                            StartingActivity.this.finish();
                        }


                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartingActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("No Internet");
                    builder.setMessage("Please Check your Internet Connection !!");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            moveTaskToBack(true);
                        }
                    });

                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            run();
                        }
                    });
                    AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                    dialog.show();
//                    Toast.makeText(StartingActivity.this, "Network Unavailable!", Toast.LENGTH_LONG).show();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private class PostDataToserver extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(strings[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result = null;
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "0";
                }
                httpclient.getConnectionManager().shutdown();
                Log.i(TASKLOG, "######################### lOGIN Result:" + result);
                res = result.trim();
                if (res.equals("\n") || res.equals("") || res.equals(null)) {
                    Intent intent1 = new Intent(StartingActivity.this, LoginActivity.class);
                    startActivity(intent1);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            gotoSettings(res);
                        }
                    });
                }
            } catch (Exception e) {
                Log.v(TASKLOG, e.toString());
            }
            Log.v(TASKLOG, "Inside AFTER CALL PostDataToserver");
            return null;
        }
    }

    private void gotoSettings(String res) {
        //@ TODO KRIS
        ArrayList<String> clint = new ArrayList<String>();
        ArrayList<String> vlist = new ArrayList<String>();
        Log.i(TASKLOG, "%%%%%%%%%% Login Result : " + res);
        if (res != null && !res.equalsIgnoreCase("[]") && !res.equalsIgnoreCase("")) {
//            res = res.replace("[", "");
//            res = res.replace("]", "");
//            String[] vehlist1 = res.split(",");
////            String clint = vehlist1[1];
////            String vlist = vehlist1[0];
//            String[] vehlist2 = vlist.split("::");
            String[] vehlist2 = res.split("::");
            for (i = 0; i < vehlist2.length; i++) {
                String[] vehlist1 = vehlist2[i].split(",");
                velist.add(vehlist1[0]);
                vlist.add(vehlist1[1]);
                clint.add(vehlist1[2]);
            }

            String text1 = uname.replaceAll("\\s+", "");
            String text2 = pass;
            Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();

            Intent in = new Intent(StartingActivity.this, MainActivity.class);
            in.putExtra("res1", velist);
            in.putExtra("val", i);
            in.putExtra("vehstatus", vlist);
            in.putExtra("clientid", clint);
            in.putExtra("pwd", text2);
            in.putExtra("userId", text1);
            in.putExtra("phone", phone);

            StartingActivity.this.startActivity(in);
            StartingActivity.this.finish();
        }
//        else {
//            Intent mainIntent = new Intent(StartingActivity.this, LoginActivity.class);
//            StartingActivity.this.startActivity(mainIntent);
//            StartingActivity.this.finish();
//        }
        return;
    }

    private class PostDataToserverrequestStartingParent extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
//    		Log.v(TASKLOG,"INSIDE BEFORE CALL POSTDATASERVER::"+arg0[0]);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result = null;
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "0";
                }

                httpclient.getConnectionManager().shutdown();
//    			Log.i(TASKLOG,"######################### lOGIN Result:"+result);

                final String res = result.trim();
//                final String res1 = res;
//                String[] vehlist3 = res.split("::");
//                final int finalre = Integer.parseInt(vehlist3[0]);
//                if (finalre == 0){
//                    res = String.valueOf(finalre);
//                }
                String[] studentdetails=result.split("::");
                String star = studentdetails[0];

                if (star.equals("0")){
                    Intent intent1 = new Intent(StartingActivity.this, LoginActivity.class);
                    startActivity(intent1);

                }else if (res.equals("\n") || res.equals("") || res.equals(null) || res.equals("0")) {
                    Intent intent1 = new Intent(StartingActivity.this, LoginActivity.class);
                    startActivity(intent1);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            gotoSettingsStartingParent(res);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void gotoSettingsStartingParent(String res) {
        ArrayList<String> firstname = new ArrayList<String>();
        ArrayList<String> section = new ArrayList<String>();
        ArrayList<String> lastname = new ArrayList<String>();
        String[] triplist1 = null;
        String clientid1 = "";
        int triptype1=1;
        if (res!=null &&  !res.equalsIgnoreCase("[]") && res!="" && !"".equals(res) && !res.equalsIgnoreCase("0")) {
            res = res.replace("[", "");
            res = res.replace("]", "");

            String[] Studentdetails = res.split(";;");
            String text1 = uname.replaceAll("\\s+", "");
            String text2 = pass;

            float va = Studentdetails.length/2;

//            if (va==0 || va==1 || va==3 || va==5 || va==7){
//                va++;
//            }

//            String stdntype = "Select Student";
//            firstname.add(stdntype);
            for (int i = 0; i < va; i++) {

                triplist1 = Studentdetails[i].split("::");

                firstname.add(triplist1[0]);
                section.add(triplist1[6]);
                lastname.add(triplist1[7]);
                clientid1 = triplist1[5];
                triptype1++;
            }
            if (triptype1 > 0) {
                Intent i = new Intent(getApplicationContext(), ViewPagerCheck.class);
                i.putExtra("fname", firstname);
                i.putExtra("lname", lastname);
                i.putExtra("section", section);
                i.putExtra("pwd", text2);
                i.putExtra("userId", text1);
                i.putExtra("clientidd",clientid1);
                Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        }
        return;
    }

    private class PostDataToserverstarting extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(strings[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result = "";
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "0";
                }
                httpclient.getConnectionManager().shutdown();
                Log.i(TASKLOG, "######################### lOGIN Result:" + result);
                final String res = result.trim();
                final int value = Integer.parseInt(res);
                if (value == 0 ){
                    Intent startingintent = new Intent(StartingActivity.this, LoginActivity.class);
                    startActivity(startingintent);
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (value == 1) {
                            String url = "http://ind.app.taskoncloud.com/taskdvcservice/fleetlist?uid=" + uname + "&pwd=" + pass;
                            String urlString = url.replaceAll(" ", "%20");
                            new PostDataToserver().execute(urlString);
                        } else {
                            Intent startingintent = new Intent(StartingActivity.this, LoginActivity.class);
                            startActivity(startingintent);
                        }
                    }
                });
            } catch (Exception e) {
                Log.v(TASKLOG, e.toString());
            }
            Log.v(TASKLOG, "Inside AFTER CALL PostDataToserver");
            return null;
        }
    }
}
