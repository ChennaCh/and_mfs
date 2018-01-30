package krifal.myfleet.and_mfs.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import krifal.myfleet.and_mfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    public static final String PREFS_NAME = "LoginPrefs";
    TextView mail,phone,usertype,name;
    Button logout;
    String accnt_phone,accnt_email,accnt_usertype,type;
    String uname = "MyFleetServices",umsts ="MyTruckServices", fleet = "MFS_SCH";
    String trucking;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        name = (TextView)view.findViewById(R.id.accnt_name) ;
        mail = (TextView)view.findViewById(R.id.accnt_email);
        phone = (TextView)view.findViewById(R.id.accnt_number);
        usertype = (TextView)view.findViewById(R.id.accnt_usertype);

        SharedPreferences preferences = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        accnt_email = preferences.getString("mail",null);
        accnt_phone = preferences.getString("phone",null);
        accnt_usertype = preferences.getString("usertype",null);
        type = preferences.getString("type",null);
        trucking = preferences.getString("trucktype",String.valueOf(1));

        if (trucking.equals(fleet)){
            name.setText(uname);
        }else {
            name.setText(umsts);
        }
//        name.setText(uname);
        mail.setText(accnt_email);
        phone.setText(accnt_phone);
        usertype.setText(type);

        return view;
    }
}
