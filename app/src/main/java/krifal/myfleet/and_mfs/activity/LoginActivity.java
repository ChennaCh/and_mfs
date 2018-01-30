package krifal.myfleet.and_mfs.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.leo.simplearcloader.SimpleArcLoader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import krifal.myfleet.and_mfs.Netowrk.MyApplication;
import krifal.myfleet.and_mfs.Netowrk.NetworkCheck;
import krifal.myfleet.and_mfs.Parent.Activity.ViewPagerCheck;
import krifal.myfleet.and_mfs.Permission.AbsRuntimePermission;
import krifal.myfleet.and_mfs.R;


public class LoginActivity extends AbsRuntimePermission implements AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;

    public static final String PREFS_NAME = "LoginPrefs";
    private static final int REQUEST_PERMISSION = 10;

    private int network = 1;

    EditText uname, pwd, phone;
    //    private ProgressBar progressBar;
    Button login;
    Spinner spinner, typespinner;
    TextView txtStatus;
    String username, passwrd, mobilenumber;
    String type, type1;
    String Parent, Teacher, Vendor, Admin;
    static String TASKLOG = "TASK ACTIVITY";
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String mPhoneNumber, IMEINumber, spinner_type, item, mts_dataitems;
    List<String> categories = new ArrayList<String>();
    private ImageView logochange;
    private SimpleArcLoader progressBar;

    private Handler h;
    private Runnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestAppPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                },
                R.string.msg, REQUEST_PERMISSION);

        uname = (EditText) findViewById(R.id.txtUserId1);
        pwd = (EditText) findViewById(R.id.txtPwd);
        phone = (EditText) findViewById(R.id.edt_phone);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        progressBar = (SimpleArcLoader) findViewById(R.id.progressBar);
        typespinner = (Spinner) findViewById(R.id.type_spinner);
        spinner = (Spinner) findViewById(R.id.spinner);
        logochange = (ImageView) findViewById(R.id.user_profile_photo);

        h = new Handler();
        final int delay = 3000;
        h.postDelayed(myRunnable = new Runnable() {
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    changeTextStatus(true);
                    network = 1;
                } else {
                    if (network == 1) {
                        changeTextStatus(false);
                        network++;
                    }
                }
                h.postDelayed(this, delay);
            }
        }, delay);

//        TelephonyManager tMgr1 = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        mPhoneNumber = tMgr1.getLine1Number();
//        phone.setText(mPhoneNumber);

        username = uname.getText().toString();
        mobilenumber = phone.getText().toString();
        passwrd = pwd.getText().toString();


        typespinner.setOnItemSelectedListener(this);
        List<String> type_categories = new ArrayList<>();
        type_categories.add("MFS_SCH");
        type_categories.add("MTS");
        type_categories.add("MFS");

        final ArrayAdapter<String> type_dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_textview_to_spinner, type_categories);
        type_dataAdapter.setDropDownViewResource(R.layout.custom_textview_to_spinner);
        typespinner.setAdapter(type_dataAdapter);
        spinner_type = typespinner.getSelectedItem().toString();

        login = (Button) findViewById(R.id.btn1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkCheck.getInstance(LoginActivity.this).isOnline()) {
                    //hiding keyboard when we click button
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    txtStatus.setText("");
                    username = uname.getText().toString();
                    passwrd = pwd.getText().toString();
                    mobilenumber = phone.getText().toString();
                    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("logged", "logged");
                    editor.commit();

                    SharedPreferences Preferences = getSharedPreferences("app", MODE_PRIVATE);
                    SharedPreferences.Editor editormain = Preferences.edit();
                    editormain.putString("mail", username);
                    editormain.putString("phone", mobilenumber);
                    editormain.putString("pass", passwrd);
                    editormain.putString("usertype", item);
                    editormain.putString("type", type);
                    editormain.putString("trucktype", type1);
                    editormain.commit();

                    if (username.equals("")) {

                        uname.requestFocus();
                        uname.setError("Please Enter valid UserName ");

                    } else if (passwrd.equals("")) {

                        pwd.requestFocus();
                        pwd.setError(" Please enter password");

                    } else if (mobilenumber.length() < 10) {
                        phone.requestFocus();
                        phone.setError("Please enter 10 digits mobile number");
                    } else if (type.equals("Admin") || type.equals("Transport Agency") || type.equals("Trucking Company") || type.equals("Logistric Service Provider")
                            || type.equals("Warehouse Client") || type.equals("Consumer goods Manufacture") || type.equals("MFS_PER")
                            || type.equals("Personal")
                            || type.equals("SecurityCompany") || type.equals("Track Only Customer") || type.equals("Direct Sales Agentr")) {
                        if (uname.getText().toString().trim().length() > 0 && pwd.getText().toString().trim().length() > 0) {
                            String url = "http://ind.app.mytruckservices.com/taskdvcservice/al?uid=" + username + "&pwd=" + passwrd + "&imei=&cell=" + mobilenumber + "&lat=&long=&usertype=" + item;
                            String urlString = url.replaceAll(" ", "%20");
                            new PostDataToserver1().execute(urlString);
                            progressBar.setVisibility(View.VISIBLE);
                            //"&imei=&cell="+mobilenumber+
                            return;
                        }
                    } else if (type.equals("Parent")) {

                        if (uname.getText().toString().trim().length() > 0 && pwd.getText().toString().trim().length() > 0) {

                            String url = "http://ind.app.taskoncloud.com/taskdvcservice/Parentlogins?action=patientlogin&uid=" + username + "&pwd=" + passwrd;
                            new PostDataToserverrequestParent().execute(url);
                            progressBar.setVisibility(View.VISIBLE);
                            return;
                        }
                    } else if (type.equals("Teacher/Employee")) {

                        Toast.makeText(getApplicationContext(), "Employee/Teacher Feature is Under Process ", Toast.LENGTH_LONG).show();


                    } else if (type.equals("Vendor")) {
                        Toast.makeText(getApplicationContext(), "Vendor Feature is Under Process ", Toast.LENGTH_LONG).show();

                    }
                    return;

                } else {
                    changeTextStatus(false);
                }
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
//        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }

    // Method to change the text status
    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        if (isConnected) {
//            Snackbar.make(toolbar, "Internet conneted.",Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(txtStatus, "Please check your Internet connection", Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getSelectedItemPosition()) {
            case 0:
                logochange.setImageResource(R.drawable.mfslogo);
                categories.clear();
                categories.add("Admin");
                categories.add("Parent");
                categories.add("Teacher/Employee");
                categories.add("Vendor");

                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_textview_to_spinner, categories);
                dataAdapter.setDropDownViewResource(R.layout.custom_textview_to_spinner);
                spinner.setAdapter(dataAdapter);
                item = spinner.getSelectedItem().toString();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item = adapterView.getItemAtPosition(i).toString();
                        if (item != null) {
                            type = "MFS_SCH";
                            String temp = item;
                            item = type;
                            type = temp;
                            type1 = item;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case 1:
//                List<String> mts_items = new ArrayList<String>();
                logochange.setImageResource(R.drawable.mtslogo);
                categories.clear();
                categories.add("Transport Agency");
                categories.add("Trucking Company");
                categories.add("Logistric Service Provider");
                categories.add("Warehouse Client");
                categories.add("Consumer goods Manufacture");
                categories.add("Track Only Customer");
                categories.add("Direct Sales Agent");

                type1 = "MTS";

                final ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, R.layout.custom_textview_to_spinner, categories);
                dataAdapter1.setDropDownViewResource(R.layout.custom_textview_to_spinner);
                spinner.setAdapter(dataAdapter1);
                item = spinner.getSelectedItem().toString();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item = adapterView.getItemAtPosition(i).toString();
                        if (item.equals("Transport Agency")) {
                            type = "MTS_TA";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Trucking Company")) {
                            type = "MTS_TC";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Logistric Service Provider")) {
                            type = "MTS_LSP";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Warehouse Client")) {
                            type = "MTS_WH";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Consumer goods Manufacture")) {
                            type = "MTS_FMCG";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Track Only Customer")) {
                            type = "MTS_TRACK";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Direct Sales Agent")) {
                            type = "MTS_DSA";
                            String temp = item;
                            item = type;
                            type = temp;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case 2:
                logochange.setImageResource(R.drawable.mfslogo);
                categories.clear();
                categories.add("SecurityCompany");
                categories.add("Personal");

                type1 = "MFS";

                final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.custom_textview_to_spinner, categories);
                dataAdapter2.setDropDownViewResource(R.layout.custom_textview_to_spinner);
                spinner.setAdapter(dataAdapter2);
                item = spinner.getSelectedItem().toString();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item = adapterView.getItemAtPosition(i).toString();
                        if (item.equals("SecurityCompany")) {
                            type = "MFS_SECURITYCOMPANY";
                            String temp = item;
                            item = type;
                            type = temp;
                        } else if (item.equals("Personal")) {
                            type = "MFS_PER";
                            String temp = item;
                            item = type;
                            type = temp;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

        }
        return;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //VEHICLELIST DATA
    private class PostDataToserver extends AsyncTask<String, Void, Void> {

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
                if (result != null) {

                } else {
                    progressBar.setVisibility(View.GONE);
                    txtStatus.setText("Vehicles Not Found");
                }
                httpclient.getConnectionManager().shutdown();
                Log.i(TASKLOG, "######################### lOGIN Result:" + result);
                final String res = result.trim();
                runOnUiThread(new Runnable() {
                    public void run() {
                        gotoSettings(res);
                    }
                });
            } catch (Exception e) {
                Log.v(TASKLOG, e.toString());
            }
            Log.v(TASKLOG, "Inside AFTER CALL PostDataToserver");
            return null;
        }
    }

    //lOGIN CHECKING
    private class PostDataToserver1 extends AsyncTask<String, Void, Void> {

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
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (value == 1) {
                            String url = "http://ind.app.taskoncloud.com/taskdvcservice/fleetlist?uid=" + username + "&pwd=" + passwrd;
                            String urlString = url.replaceAll(" ", "%20");
                            new PostDataToserver().execute(urlString);
                        } else {
                            txtStatus.setText("Please enter Valid Username and password");
                            uname.requestFocus();
                            progressBar.setVisibility(View.GONE);
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

    private void gotoSettings(String res) {
        //@ TODO KRIS
        Log.i(TASKLOG, "%%%%%%%%%% Login Result : " + res);
        ArrayList<String> velist = new ArrayList<String>();
        ArrayList<String> clint = new ArrayList<String>();
        ArrayList<String> vlist = new ArrayList<String>();
        if (res != null && !res.equalsIgnoreCase("[]") && !res.equalsIgnoreCase("")) {
            int i;
            res = res.replace("[", "");
            res = res.replace("]", "");
//            String[] vehlist1 = res.split(",");
//            String clint = vehlist1[1];
//            String vlist = vehlist1[0];
            String[] vehlist2 = res.split("::");
            for (i = 0; i < vehlist2.length; i++) {
                String[] vehlist1 = vehlist2[i].split(",");
                velist.add(vehlist1[0]);
                vlist.add(vehlist1[1]);
                clint.add(vehlist1[2]);
            }
            String text1 = uname.getText().toString().replaceAll("\\s+", "");
            String text2 = pwd.getText().toString();
            Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();

            Intent inte = new Intent(getApplicationContext(), MainActivity.class);
            progressBar.setVisibility(View.GONE);
            inte.putExtra("res1", velist);
            inte.putExtra("clientid", clint);
            inte.putExtra("val", i);
            inte.putExtra("vehstatus", vlist);
            inte.putExtra("pwd", text2);
            inte.putExtra("userId", text1);
            inte.putExtra("phne", mobilenumber);
            startActivity(inte);
//            Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
//            startActivity(intent);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show();
        }
        return;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alerticon)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LoginActivity.this.finish();

                    }

                })
                .setNegativeButton("No", null)
                .show();
//        moveTaskToBack(false);
    }


    private class PostDataToserverrequestParent extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
//    		Log.v(TASKLOG,"INSIDE BEFORE CALL POSTDATASERVER::"+arg0[0]);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result = "";
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "0";
                }

//                String[] studentdetails=result.split("::");
//                String star = studentdetails[0];

                httpclient.getConnectionManager().shutdown();
//    			Log.i(TASKLOG,"######################### lOGIN Result:"+result);

//                final String res = result.trim();
                final String res = result;

                final String resulttrim = res.trim();

                String[] studentdetails = result.split("::");
                String star = studentdetails[0];

                if (star.equals("0")) {
//                    progressBar.setVisibility(View.GONE);
                    txtStatus.setText("No Trips For Student ");


                } else if (resulttrim.equals("\n") || resulttrim.equals("") || resulttrim.equals(null) || resulttrim.equals("0")) {
                    progressBar.setVisibility(View.GONE);
                    txtStatus.setText("Please Enter Valid Username/Password");
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            gotoSettingsParent(resulttrim);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            } catch (Exception e) {
//				Log.v(TASKLOG, e.toString());
            }
//			Log.v(TASKLOG, "Inside AFTER CALL PostDataToserver");
            return null;
        }
    }

    private void gotoSettingsParent(String res) {
        ArrayList<String> firstname = new ArrayList<String>();
        ArrayList<String> section = new ArrayList<String>();
        ArrayList<String> lastname = new ArrayList<String>();
        String clientid1 = "";
        String[] triplist1 = null;
        int triptype1 = 1;
        if (res != null && !res.equalsIgnoreCase("[]") && res != "" && !"".equals(res) && !res.equalsIgnoreCase("0")) {
            res = res.replace("[", "");
            res = res.replace("]", "");

            String[] Studentdetails = res.split(";;");
            String text1 = (uname.getText()).toString().replaceAll("\\s+", "");
            String text2 = (pwd.getText()).toString();

            float va = Studentdetails.length / 2;

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
                clientid1 = triplist1[6];
                triptype1++;
            }
            if (triptype1 > 0) {
                Intent i = new Intent(getApplicationContext(), ViewPagerCheck.class);
                i.putExtra("fname", firstname);
                i.putExtra("lname", lastname);
                i.putExtra("section", section);
                i.putExtra("pwd", text2);
                i.putExtra("userId", text1);
                i.putExtra("clientidd", clientid1);
                Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();
                startActivity(i);
            } else {
                txtStatus.setText("" +
                        ".");
                uname.requestFocus();
            }

        } else {
            txtStatus.setText("Wrong Username/Psaaword");
            uname.requestFocus();
        }
        return;
    }

    @Override
    protected void onPause() {

        super.onPause();
        MyApplication.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.activityResumed();// On Resume notify the Application
    }
}