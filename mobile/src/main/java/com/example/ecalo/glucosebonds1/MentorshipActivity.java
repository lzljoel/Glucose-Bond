package com.example.ecalo.glucosebonds1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baasbox.android.BaasBox;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

public class MentorshipActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private BaasBox client;

    Context context;
    private ImageButton findMentor, beMentor;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private final static int PLAY_SERVICE_RESOLUTION_REQUEST = 1000;


    //FOR CURRENT LOCATION
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;
    private static final String TAG = "myMessage";

    private LinearLayout layout1, layout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("=============================");
        System.out.println("This is MentorshipActivity");
        System.out.println("=============================");
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.mentorship_homepage);
        findMentor = (ImageButton) findViewById(R.id.findButton);
        beMentor = (ImageButton) findViewById(R.id.beButton);
        findMentor.setOnClickListener(this);
        beMentor.setOnClickListener(this);
        layout1 = (LinearLayout) findViewById(R.id.findMentorLayout);
        layout2 = (LinearLayout) findViewById(R.id.beMentorLayout);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (this.checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findButton:
                Intent MentorsListIntent = new Intent(context, MentorsListActivity.class);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.e("T", "failing permissions");
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if(mLastLocation != null){
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();
                    System.out.println("==================================================");
                    System.out.println(latitude);
                    System.out.println("==================================================");
                    MentorsListIntent.putExtra("LATITUDE",Double.toString(latitude) );
                    MentorsListIntent.putExtra("LONGITUDE",Double.toString(longitude) );

                    startActivity(MentorsListIntent);

                }else{

                    System.out.println("No Last Location Detected");
                    System.out.println("==================================================");
                }

                break;
            case R.id.beButton:
                Intent beMentorActivity = new Intent(context, BeMentorActivity.class);
                startActivity(beMentorActivity);
                break;

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;

        }
        return true;
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            System.out.println("==================================================");
            System.out.println("Successfully Got current Location by Google API");
            System.out.println(latitude);
            System.out.println(longitude);
            System.out.println("==================================================");
        }else{
            System.out.println("mLastLocation is null!!!!!!!!");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location Changed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed: " + connectionResult.getErrorCode());
    }




}
