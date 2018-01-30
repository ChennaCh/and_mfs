package com.example.navigationdrawer;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	String mTitle = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTitle = (String) getTitle();
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
        		R.string.drawer_open, R.string.drawer_close){
        	
        	@Override
        	public void onDrawerClosed(View drawerView) {
        		getActionBar().setTitle(mTitle);
        		invalidateOptionsMenu();
        	}
        	
        	@Override
			public void onDrawerOpened(View drawerView) {
        		getActionBar().setTitle("Select a river");
        		invalidateOptionsMenu();
			}
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.drawer_list_item, getResources()
						.getStringArray(R.array.rivers));
        mDrawerList.setAdapter(adapter);
        
        getActionBar().setHomeButtonEnabled(true);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				String[] rivers = getResources().getStringArray(R.array.rivers);
				
				mTitle = rivers[position];
				
				RiverFragment rFragment = new RiverFragment();
				
				Bundle data = new Bundle();
				
				data.putInt("position", position);
				
				rFragment.setArguments(data);
				
				android.app.FragmentManager fragmentManager = getFragmentManager();
				
				FragmentTransaction ft = fragmentManager.beginTransaction();
				
				ft.replace(R.id.content_frame, rFragment);
				
				ft.commit();
				
				mDrawerLayout.closeDrawer(mDrawerList);
			}
        	
		});
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	mDrawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(mDrawerToggle.onOptionsItemSelected(item)){
    	   return true;
       }
       return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    	menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
    }
}
