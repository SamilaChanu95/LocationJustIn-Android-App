package com.example.locationjustin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

//using the google api service must implement this interfaces
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = "TAG";
    private static final int REQUEST_CODE = 1000;

    private GoogleApiClient googleApiClient;
    //private Location location;
   // private TextView txtLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //txtLocation = (TextView) findViewById(R.id.txtLocation);

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API).build();
     }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d(TAG, "We are connected to the user's location.");
        //showTheUserLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(TAG, "The connection is suspected.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"Connection is failed.");

        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(MainActivity.this, REQUEST_CODE);
            } catch (Exception e) {
                Log.d(TAG, e.getStackTrace().toString());
            }
        }
        else {
            Toast.makeText(MainActivity.this, "GooglePlay Services is not working. Exit!",Toast.LENGTH_LONG).show();
            finish(); // close this activity
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && requestCode == RESULT_OK) {

            googleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(googleApiClient != null) {

            googleApiClient.connect();
        }
    }
/*
    //custom method
    private void showTheUserLocation(){

        //permission initialize in to variable
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        //check the permission as if user give the permission (permission was granted)
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){

            //get the google's FusedLocation API
            FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

            location = fusedLocationProviderApi.getLastLocation(googleApiClient);

            if(location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                //return the latitude and longitude
                txtLocation.setText(latitude + ", "+longitude);
            }else{
                txtLocation.setText("The app is not able to access the location now.Try again later.");
            }

        }
        //permission denied
        else{

            txtLocation.setText("This app not allowed to access the location");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            // 1 is the request code
            //ActivityCompat.requestPermissions();
        }
    }*/
}

