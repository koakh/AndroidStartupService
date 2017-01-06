package koakh.com.koakhandroidstartupservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    //Application
    private AppSingleton mApp;
    private MenuItem mMenuItemStartService;
    private MenuItem mMenuItemStopService;
    private MenuItem mMenuItemCallServiceMethod;

    //Intent
    private Intent mIntentServiceExample;
    //Service Bind Vars
    private ServiceExample mService;
    private boolean mBound = false;
    //UI
    private TextView mTextView;
    //Moke Data
    private String[] mFileList = {
      "/Articles/ArticleClassRepository",
      "/Articles/ArticleFamilyRepository",
      "/Articles/ArticleRepository",
      "/Articles/ArticleStockRepository",
      "/Articles/ArticleSubFamilyRepository",
      "/Articles/ArticleTypeRepository",
      "/Configuration/ConfigurationCashRegisterRepository",
      "/Configuration/ConfigurationCountryRepository",
      "/Configuration/ConfigurationCurrencyRepository",
      "/Configuration/ConfigurationDeviceRepository",
      "/Configuration/ConfigurationKeyboardRepository",
      "/Configuration/ConfigurationMaintenanceRepository",
      "/Configuration/ConfigurationPaymentConditionRepository",
      "/Configuration/ConfigurationPaymentMethodRepository",
      "/Customers/CustomerDiscountGroupRepository",
      "/Customers/CustomerRepository",
      "/Customers/CustomerTypeRepository",
      "/Documents/CommissionRepository",
      "/Documents/DetailOrderReferenceRepository",
      "/Documents/DetailReferenceRepository",
      "/Documents/DetailRepository",
      "/Documents/MasterPaymentRepository"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get Application Singleton
        mApp = AppSingleton.getInstance();
        mApp.setContext(this.getApplicationContext());
        mApp.setMainActivity(this);

        //get UI Objects References
        mTextView = (TextView)findViewById(R.id.main_textview);

        //Service Intent ServiceExample
        mIntentServiceExample = new Intent(this, ServiceExample.class);
    }

    @Override
    protected void onStart() {

        super.onStart();

        //If Service Running Bind to Service
        if (mApp.isServiceRunning()) {
            bindService(mIntentServiceExample, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();

        // Unbind from the service
        if (mApp.isServiceRunning() && mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Register mMessageReceiver to receive messages.
        // Required com.android.support:support-vx
        LocalBroadcastManager.getInstance(this).registerReceiver(
          mMessageReceiver, new IntentFilter(ServiceExample.LOCAL_SERVICE_MESSAGE)
        );
    }

    @Override
    protected void onPause() {

        super.onPause();

        // Unregister since the activity is not visible
        LocalBroadcastManager
          .getInstance(this)
          .unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //Get References to MenuItems here
        mMenuItemStartService = menu.findItem(R.id.action_menu_service_start);
        mMenuItemStopService = menu.findItem(R.id.action_menu_service_stop);
        mMenuItemCallServiceMethod = menu.findItem(R.id.action_menu_service_call_method);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mApp.isServiceRunning()) {
            mMenuItemStartService.setEnabled(false);
            mMenuItemStopService.setEnabled(true);
            mMenuItemCallServiceMethod.setEnabled(true);
        }
        else {
            mMenuItemStartService.setEnabled(true);
            mMenuItemStopService.setEnabled(false);
            mMenuItemCallServiceMethod.setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_menu_service_start) {
            startServiceExample();
            return true;
        }
        else if (id == R.id.action_menu_service_stop) {
            stopServiceExample();
            return true;
        }
        else if (id == R.id.action_menu_service_call_method) {
            int randomNumber = mService.getRandomNumber();
            String message = String.format("RandomNumber: %d", randomNumber);
            Log.d(mApp.TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //Services Actions

    public void startServiceExample() {

        mIntentServiceExample.putExtra("ImageFileURL", "http://www.codeproject.com/App_Themes/CodeProject/Img/logo125x125.gif");
        mIntentServiceExample.putExtra("FileName", "bob.gif");
        mIntentServiceExample.putExtra("FileList", mFileList);
        //Start Service and Bind to LocalService
        //http://developer.android.com/reference/android/content/Context.html
        startService(mIntentServiceExample);
        bindService(mIntentServiceExample, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopServiceExample() {

        try {
            if (mBound) unbindService(mConnection);
            stopService(mIntentServiceExample);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //Services Bind

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            Log.i(mApp.TAG, "ServiceConnected");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            // Problem
            // java.lang.ClassCastException: android.os.BinderProxy cannot be cast to
            // The problem is declaring service in manifest with android:process=":ServiceExample"
            // This force service to run in a separate application context, turning Binder into BinderProxy
            ServiceExample.LocalBinder binder = (ServiceExample.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            Log.i(mApp.TAG, "ServiceDisconnected");
            mBound = false;
        }
    };

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //LocalBroadcastManager

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String action = intent.getAction();
            Integer progress = intent.getIntExtra("currentSpeed", 0);
            Double currentSpeed = intent.getDoubleExtra("currentSpeed", 0);
            Double currentLatitude = intent.getDoubleExtra("latitude", 0);
            Double currentLongitude = intent.getDoubleExtra("longitude", 0);

            String message = String.format("%s/100 : %s : %s :%s",
              progress,
              currentSpeed.toString(),
              currentLatitude.toString(),
              currentLongitude.toString()
            );

            Log.d(mApp.TAG, message);
            mTextView.setText(message);
        }
    };
}
