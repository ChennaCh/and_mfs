package krifal.myfleet.and_mfs.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import krifal.myfleet.and_mfs.Netowrk.MyApplication;
import krifal.myfleet.and_mfs.R;
import krifal.myfleet.and_mfs.fragment.AccountFragment;
import krifal.myfleet.and_mfs.fragment.HistoryVehicle;
import krifal.myfleet.and_mfs.fragment.HomeFragment;
import krifal.myfleet.and_mfs.fragment.VehicleListFragment;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    public static final int FLAG_ACTIVITY_CLEAR_TOP = 67108864;


    private static long back_pressed;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private Boolean saveLogin;
    private SharedPreferences loginPreferences;
    private View view;

    int k = 1, l, network = 1;

    private Handler h;
    private Runnable myRunnable;
    String trucking;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_OVERVIEW = "overview";
    private static final String TAG_VEHICLELIST = "vehiclelist";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_LOGOUT = "logout";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_PRIVACY_POLICY = "privacypolicy";
    public static String CURRENT_TAG = TAG_OVERVIEW;
    private Snackbar snackbar;
    Handler handler;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        imgProfile = (ImageView) findViewById(R.id.img_header_bg);
//        toolbr = (Toolbar) findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbr);\
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
        trucking = preferences.getString("trucktype", String.valueOf(1));

        mHandler = new Handler();

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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
//        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_OVERVIEW;
            loadHomeFragment();
        }
    }

    // Method to change the text status
    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        if (isConnected) {

        } else {
            Snackbar.make(toolbar, "Please check your Internet connection", Snackbar.LENGTH_LONG).show();

        }
    }

    private void loadNavHeader() {
        // name, website
//        txtName.setText("Ravi Tamada");
        SharedPreferences preferences = getSharedPreferences("app", Context.MODE_PRIVATE);
        String accnt_email = preferences.getString("mail", null);
        txtWebsite.setText(accnt_email);

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
//            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
//        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Overview
                if (k == 1) {
                    k++;
                    l = 1;
                    SharedPreferences getvalue = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor eidt = getvalue.edit();
                    eidt.putInt("kvalue", l);
                    eidt.commit();
                    HomeFragment homeFragment = new HomeFragment();
                    return homeFragment;

                } else {
                    k = 2;
                    l = 2;
                    SharedPreferences getvalue = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor eidt = getvalue.edit();
                    eidt.putInt("kvalue", l);
                    eidt.commit();
                    HomeFragment homeFragment = new HomeFragment();
                    return homeFragment;
                }

            case 1:
                // VehicleList
                VehicleListFragment vehicleListFragment = new VehicleListFragment();
                return vehicleListFragment;
            case 2:
                // Account
                AccountFragment accountFragment = new AccountFragment();
                return accountFragment;
            case 3:
                // VehicleHistory
                HistoryVehicle vehicleHistory = new HistoryVehicle();
                return vehicleHistory;


//            case 4:
//                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger
            // on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_overview:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_OVERVIEW;
                        break;
                    case R.id.nav_vehiclelist:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_VEHICLELIST;
                        break;
                    case R.id.nav_acount:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ACCOUNT;
                        break;
                    case R.id.nav_history:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_HISTORY;
                        break;
                    case R.id.nav_logout:
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.alerticon)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to Logout ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                                        preferences.edit().clear().commit();
                                        MainActivity.this.finish();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_OVERVIEW;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
//        else if (navItemIndex == 1 ){
//            getMenuInflater().inflate(R.menu.search, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.alerticon)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                            preferences.edit().clear().commit();
                            MainActivity.this.finish();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        if (id == R.id.action_closeapp) {

            finish();
            moveTaskToBack(true);
        }
        if (id == R.id.action_refresh) {
            Fragment fragment1 = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            l = 1;
            SharedPreferences getvalue = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor eidt = getvalue.edit();
            eidt.putInt("kvalue", l);
            eidt.commit();
//            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment1, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }

        return super.onOptionsItemSelected(item);
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
