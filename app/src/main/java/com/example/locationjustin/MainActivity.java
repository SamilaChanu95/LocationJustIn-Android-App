package com.example.locationjustin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.List;

//using the google api service must implement this interfaces                                                                *Implements this interfaces*
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,View.OnClickListener, LocationListener {

    public static final String TAG = "TAG";
    private static final int REQUEST_CODE = 1000;

    private GoogleApiClient googleApiClient;
    //private Location location;
   // private TextView txtLocation;

    EditText edtAddress;
    EditText edtMilesPerHour;
    EditText edtMetersPerMile;
    TextView txtDistanceValue;
    TextView txtTime;
    Button btnGetTheData;

    private String destinationLocationAddress = "";

    //initialize the taxiManager variable
    private TaxiManager taxiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //txtLocation = (TextView) findViewById(R.id.txtLocation);

        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtMilesPerHour = (EditText) findViewById(R.id.edtMilesPerHour);
        edtMetersPerMile = (EditText) findViewById(R.id.edtMetersPerMile);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDistanceValue = (TextView) findViewById(R.id.txtDistanceValue);
        btnGetTheData = (Button) findViewById(R.id.btnGetTheData);

        btnGetTheData.setOnClickListener(MainActivity.this);

        //initialize the taxiManager variable
        taxiManager = new TaxiManager();

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API).build();
     }

    @Override
    public void onClick(View v) {

        String addressValue = edtAddress.getText().toString();
        boolean isGeoCoding = true;// that convert the latitude and Longitude to an actual address

        if(!addressValue.equals(destinationLocationAddress))
        {
            destinationLocationAddress = addressValue;

            Geocoder geocoder = new Geocoder(getApplicationContext());

            try {

                List<Address> myAddresses =
                        geocoder.getFromLocationName(destinationLocationAddress, 4);

                if(myAddresses != null) {

                    double latitude = myAddresses.get(0).getLatitude();
                    double longitude = myAddresses.get(0).getLongitude();

                    Location locationAddress = new Location("MyDestination");
                    locationAddress.setLatitude(latitude);
                    locationAddress.setLongitude(longitude);

                    taxiManager.setDestinationLocation(locationAddress);

                }

            } catch (Exception e) {

                isGeoCoding = false;

                e.printStackTrace();

            }
        }

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {

            FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
            Location userCurrentLocation = fusedLocationProviderApi.getLastLocation(googleApiClient);
            if(userCurrentLocation != null && isGeoCoding) {

                txtDistanceValue.setText(taxiManager.returnTheMilesBetweenCurrentLocationAndDestinationLocation(userCurrentLocation, Integer.parseInt(edtMetersPerMile.getText().toString())));
                txtTime.setText(taxiManager.returnTheTimeLeftToGetToDestinationLocation(userCurrentLocation, Float.parseFloat(edtMilesPerHour.getText().toString()), Integer.parseInt(edtMetersPerMile.getText().toString())));
            }

        } else {

            txtDistanceValue.setText("This App is not allowed to access the location.");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d(TAG, "We are connected to the user's location.");
        //showTheUserLocation();

        FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); //every 10s location that updated
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(5);

        if(googleApiClient.isConnected()) {

            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest,MainActivity.this);
        }

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

