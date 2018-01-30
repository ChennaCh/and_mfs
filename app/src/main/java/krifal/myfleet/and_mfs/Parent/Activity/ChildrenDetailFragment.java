package krifal.myfleet.and_mfs.Parent.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import krifal.myfleet.and_mfs.R;
import krifal.myfleet.and_mfs.Utils.CameraUtility;
import krifal.myfleet.and_mfs.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildrenDetailFragment extends Fragment {

    Spinner spinner,spinnerpicktype;
    String item;
    public static final String PREFS_NAME = "parent";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    Button parentlogout;
    ArrayList<String> firstname = new ArrayList<>();
    ArrayList<String> lastname = new ArrayList<>();
    ArrayList<String> section = new ArrayList<>();
    String txtUserId,txtPwd,clientid;
    HashMap<String,String> spinnerMap = new HashMap<>();
    TextView stdvehicleno,stdclass1,stdname,stdtrip,stdfather1,stdphno,stdpickup,stdschdtime,trip_history,parentstatus,addresss;
    static ProgressBar childrenprogress;
    private Runnable myRunnable1;
    String spinnertype,pickspinner;
    int tripid= 1;
    String lastnameurl = "",sectionurl = "";
    ImageView clientImage;
    private ImageButton logout_parent_btn;
    private String tripcodehistory;
    private String drop;
    private Double updatedlat = 0.0,updatedlang =0.0;

    private ImageView clentimage;
    SharedPreferences shre;
    Bitmap thumbnail;
    public static final String MyPREFERENCES = "MyPre" ;//file name
    public static final String  key = "nameKey";

    Handler handler1;
    int delay;

    SendMessage SM;



    public ChildrenDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_children_detail, container, false);

//        parentlogout = (Button) view.findViewById(parent_logout);
        stdvehicleno = (TextView) view.findViewById(R.id.parent_vehicleno);
        stdclass1 = (TextView) view.findViewById(R.id.parent_class);
        stdname = (TextView) view.findViewById(R.id.parent_stdname);
        stdtrip = (TextView) view.findViewById(R.id.parent_tripcode);
        addresss = (TextView) view.findViewById(R.id.parent_address);
        stdfather1 = (TextView) view.findViewById(R.id.parent_fathername);
        stdphno = (TextView) view.findViewById(R.id.parent_phno);
        stdpickup = (TextView) view.findViewById(R.id.parent_pickup);
        stdschdtime = (TextView) view.findViewById(R.id.parent_schdtime);
//        stdsection1 = (TextView) view.findViewById(R.id.parent_section);
        childrenprogress = (ProgressBar) view.findViewById(R.id.children_preogressbar);
        parentstatus = (TextView) view.findViewById(R.id.parent_status);
        spinnerpicktype = (Spinner) view.findViewById(R.id.parent_spinner1);
        logout_parent_btn = (ImageButton) view.findViewById(R.id.logout_parent_btn);
        trip_history = (TextView) view.findViewById(R.id.trip_history);
        clentimage = (ImageView) view.findViewById(R.id.clentimage);

//        parentstatus.setText("Please Select TripType");

        shre =  getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (shre.contains(key))
        {//save required image
            String u=shre.getString(key, "");
            thumbnail=decodeBase64(u);
            clientImage.setImageBitmap(thumbnail);
        }


        try {
            Intent i = getActivity().getIntent();
            Bundle b=i.getExtras();
//			    studentdeatils = b.getStringArray("res1");
            firstname = b.getStringArrayList("fname");
            lastname = b.getStringArrayList("lname");
            section = b.getStringArrayList("section");
            clientid = b.getString("clientidd");
            txtUserId=b.getString("userId");
            txtPwd=b.getString("pwd");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        sectionurl = section.get(1);
        String[] spinnerArray = new String[firstname.size()];
        for (int i = 0; i < firstname.size(); i++)
        {
//            spinnerMap.put(firstname.get(i),firstname.get(i));
            spinnerArray[i] = firstname.get(i);
        }

        clentimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        spinner = (Spinner) view.findViewById(R.id.parent_spinner);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_parent_spinner, spinnerArray);
        dataAdapter.setDropDownViewResource(R.layout.custom_parent_spinner);
        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View arg1,
                                       int arg2, long arg3) {

                for (int k= 0 ; k< firstname.size(); k ++) {
                    if (adapterView.getSelectedItem().equals(firstname.get(k))){
                        lastnameurl = lastname.get(k);
                        sectionurl = section.get(k);

                    }
                }

//                childrenprogress.setVisibility(View.VISIBLE);
                stdpickup.setText("");
                stdschdtime.setText("");
                stdvehicleno.setText("");
                stdtrip.setText("");

                spinnertype = spinner.getSelectedItem().toString().trim();
                picktypespinner(spinnertype);

                return ;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        logout_parent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.alerticon)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preferences = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
                                preferences.edit().clear().commit();
                                getActivity().finish();
                                Intent intent= new Intent(getActivity(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        trip_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Parent_Histoy_Activity.class);
                intent.putExtra("codehistory",tripcodehistory);
                intent.putExtra("triptype1",drop);
                startActivity(intent);
            }
        });

        return view;
    }

    //Image Select
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= CameraUtility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
        childrenprogress.setVisibility(View.GONE);


    }
    private void galleryIntent()
    {
        childrenprogress.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                //onSelectFromGalleryResult(data);
                Bitmap bm=null;

                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    storeImage(bm);
                    clentimage.setImageBitmap(bm);

                }
            }

            else if (requestCode == REQUEST_CAMERA) {
                //onCaptureImageResult(data);
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                storeImage(bm);
                clentimage.setImageBitmap(bm);

            }
        }
    }
    private void storeImage(Bitmap thumbnail) {
        // Removing image saved earlier in shared prefernces
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
        createFolder();
        // this code is use to generate random number and add to file
        // name so that each file should be different
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";

        // set the file path
        // sdcard/PictureFolder/ is the folder created in create folder method
        String filePath = "/sdcard/PictureFolder/"+fname;
        // the rest of the code is for saving the file to filepath mentioned above
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

        //choose another format if PNG doesn't suit you
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bos);
        shre =  getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shre.edit();
        editor.putString(key,encodeTobase64(thumbnail));
        editor.commit();
        try {
            bos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void createFolder()
    {
        // here PictureFolder is the folder name you can change it offcourse
        String RootDir = Environment.getExternalStorageDirectory()
                + File.separator + "PictureFolder";
        File RootFile = new File(RootDir);
        RootFile.mkdir();
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;

    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void picktypespinner(final String spinnertype) {
        final String spinnertype1 = spinnertype;
        List<String> type_categories12 = new ArrayList<>();
        type_categories12.add("Trip Type");
        type_categories12.add("PickUp");
        type_categories12.add("Drop");

        final ArrayAdapter<String> type_dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_parent_spinner, type_categories12);
        type_dataAdapter.setDropDownViewResource(R.layout.custom_parent_spinner);
        spinnerpicktype.setAdapter(type_dataAdapter);
//        pickspinner = spinnerpicktype.getSelectedItem().toString().trim();

        spinnerpicktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pickspinner = spinnerpicktype.getSelectedItem().toString().trim();

                if (pickspinner.equals("PickUp")){
                    tripid = 1;
                }
//                else if (pickspinner.equals("Trip Type")){
////                    Toast.makeText(getActivity(), "Please Select TripType", Toast.LENGTH_SHORT).show();
//                    parentstatus.setText("Please Select TripType");
//                    stdpickup.setText(" :  ");
//                    EmptyText();
//                    tripid = 0;
//                    childrenprogress.setVisibility(View.GONE);
//                }
                else{
                    tripid = 2;
                }

                if (pickspinner.equals("Drop")){
                    drop = "Drop";
                    stdpickup.setText(" :  " + drop);
//                    String  url="http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId +"&pwd="+txtPwd+"&triptype="+tripid+"&studentname="+spinnertype1+"&studentnam1="+lastnameurl+"&section1="+sectionurl;
                    String url = "http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId+"&pwd="+txtPwd+"&triptype="+tripid+"&studentname="+spinnertype1+"&studentnam1="+lastnameurl+"&section1="+sectionurl;
                    childrenprogress.setVisibility(View.VISIBLE);
                    EmptyText();
                    new PostDataToserverforstudentdata().execute(url);
                }
                else if (pickspinner.equals("PickUp")){
                    drop = "PickUp";
                    stdpickup.setText(" :  " + drop);
//                    String  url="http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId +"&pwd="+txtPwd+"&triptype="+tripid+"&studentname="+spinnertype1+"&studentnam1="+lastnameurl+"&section1="+sectionurl;
//                    String  url="http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId +"&pwd="+txtPwd+"&triptype="+spinnertype;
                    String url = "http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId+"&pwd="+txtPwd+"&triptype="+tripid+"&studentname="+spinnertype1+"&studentnam1="+lastnameurl+"&section1="+sectionurl;
                    childrenprogress.setVisibility(View.VISIBLE);
                    EmptyText();
                    new PostDataToserverforstudentdata().execute(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void EmptyText() {
        stdname.setText(" :  ");
        stdschdtime.setText(" :  ");
        stdtrip.setText(" :  ");
        stdvehicleno.setText(" :  ");
        stdclass1.setText(" :  ");
        addresss.setText(" :  ");
//                                stdsection1.setText(" :  ");
        stdfather1.setText(" :  ");
        stdphno.setText(" :  ");
    }


    interface SendMessage {
        void sendData(String s, String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    private class PostDataToserverforstudentdata extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result="";
                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpclient.getConnectionManager().shutdown();
                String[] Studentdetails = result.split(";;");
                if (result.length() >= 10) {
                    String[] studentdetail = Studentdetails[0].split("::");
                    final String name1 = studentdetail[0].toString()+" "+lastnameurl;
//                final int triptype1=Integer.parseInt(studentdetail[1]);
                    final String time = studentdetail[2].toString();
                    final String tripcod = studentdetail[3].toString();
                    final String vehiclecod = studentdetail[4].toString();
//                final int tripstatus=Integer.parseInt(studentdetail[5]);
                    final String stdclass = studentdetail[6];
                    final String stdsection = studentdetail[7].toString();
                    final String stdfather = studentdetail[8].toString();
                    final String stdmoblenumber = studentdetail[9].toString();
                    final Double lat = Double.parseDouble(studentdetail[10].toString());
                    final Double lng = Double.parseDouble(studentdetail[11].toString());
//                    final double lat = 17.490997;
//                    final double lng = 78.328316;

                final String res = result;
                    getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        parentstatus.setVisibility(View.GONE);
                        studentdetails(stdclass, stdsection,stdfather,stdmoblenumber,lat,lng,name1
                                ,time,tripcod,vehiclecod);
                    }
                });
                }else {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                childrenprogress.setVisibility(View.GONE);
                                parentstatus.setText("No Data Found");
                                stdname.setText(" :  ");
                                stdschdtime.setText(" :  ");
                                stdtrip.setText(" :  ");
                                stdvehicleno.setText(" :  ");
                                stdclass1.setText(" :  ");
//                                stdsection1.setText(" :  ");
                                stdfather1.setText(" :  ");
                                stdphno.setText(" :  ");

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

    }
    private void studentdetails(String stclass,String stsection,String stfather,String stmobile,double lat
            ,double lng,String name1,String schtime,String trpcod,String fleetcod){

        try {
            stdname.setText(name1);
            stdschdtime.setText(" :  " + schtime);
            stdtrip.setText(" :  " + trpcod);
            tripcodehistory = trpcod;
            stdvehicleno.setText(fleetcod);
            stdclass1.setText(stclass);
//            stdsection1.setText(" :  " + stsection);
            stdfather1.setText(" :  " + stfather);
            stdphno.setText(" :  " + stmobile);
            childrenprogress.setVisibility(View.GONE);

            Geocoder geocoder;
            String address1="";
            String city="";
            String country="";
            String result12="";
            List<Address> addresses=null;
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                if(addresses.size() > 0) {
                    address1 = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getAddressLine(1);
                    country = addresses.get(0).getAddressLine(2);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            result12 =address1+" "+city+" "+country;
            result12 =address1;
            addresss.setText("  "+result12);

            Double lattitude = lat;
            Double longitude = lng;

            SM.sendData(lattitude.toString(),longitude.toString());

            getHandler();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return;
    }

    private void getHandler() {
        handler1 = new Handler();
        delay = 5000; //milliseconds
        handler1.postDelayed(myRunnable1 = new Runnable(){
            public void run(){
                String tripname = spinner.getSelectedItem().toString().trim();
                String trpid = spinnerMap.get(tripname);
//                String  url="http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId +"&pwd="+txtPwd+"&triptype="+tripid+"&studentname="+spinnertype+"&studentnam1="+lastnameurl+"&section1="+sectionurl;
                String url = "http://ind.app.myfleetservices.com/taskdvcservice/Parentlogins?action=Studenttripdetails&uid="+txtUserId+"&pwd="+txtPwd+"&triptype="+tripid+"&studentname="+spinnertype+"&studentnam1="+lastnameurl+"&section1="+sectionurl;

                new PostDataToserverforstudentdata1().execute(url);
                handler1.postDelayed(this, delay);
            }
        }, delay);
    }

    private class PostDataToserverforstudentdata1 extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(arg0[0]);
                ResponseHandler<String> handler = new BasicResponseHandler();
                String result="";

                try {
                    result = httpclient.execute(request, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpclient.getConnectionManager().shutdown();

                    String[] studentdetail = result.split("::");
                    final double lat = Double.parseDouble(studentdetail[10].toString());
                    final double lng = Double.parseDouble(studentdetail[11].toString());

                    final String res = result;

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            studentdetails1(lat, lng);
                        }
                    });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

    }

    private void studentdetails1(double lat, double lng) {

        try {
            Double lattitude = lat;
            Double longitude = lng;
            if (lattitude.equals(updatedlat)) {

            }else {

                SM.sendData(lattitude.toString(),longitude.toString());
                updatedlat = lattitude;
                updatedlang = longitude;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return;
    }
}
