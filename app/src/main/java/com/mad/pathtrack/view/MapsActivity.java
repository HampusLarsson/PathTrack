package com.mad.pathtrack.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mad.pathtrack.R;
import com.mad.pathtrack.model.RecordedRun;
import com.mad.pathtrack.viewModel.MapsViewModel;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.mad.pathtrack.view.RecordedRunAdapter.RecordedRunViewHolder.ID_KEY;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapsViewModel mMapsViewModel;
    private PolylineOptions mPath;
    private Polyline mPolyline;
    private ArrayList<LatLng> mLocations;

    private String mDescription;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Boolean mRecording;
    private String mTypeOfActivity;
    private int mRunId;

    public static final String TAG = "MyActivity";
    public static final int FASTEST_INTERVAL = 5000;
    public static final int UPDATE_INTERVAL = 10000;
    public static final int MY_REQUEST_FINE_LOCATION = 11;
    public static final int MY_REQUEST_INTERNET = 12;
    public static final int MY_REQUEST_COARSE_LOCATION = 13;
    public static final int MY_REQUEST_NETWORK_STATE = 14;
    public static final String DIALOG_TITLE = "Enter Description";
    public static final String DIALOG_OK = "OK";
    public static final String DIALOG_CANCEL = "Cancel";
    public static final String PATH_KEY = "mPath";
    public static final String TYPE_KEY = "type";
    public static final String RECORD_KEY = "record";
    public static final String DISPLAY_KEY = "display";
    public static final String LAST_LOCATION_ERROR = "Error trying to get last GPS location";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get information from intent
        Intent intent = getIntent();
        mTypeOfActivity = intent.getStringExtra(TYPE_KEY);

        //Initiate viewModel and locations arrayList and PolyLineOptions
        mLocations = new ArrayList<>();
        mMapsViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
        mPath = new PolylineOptions();

        //If it's a display activity fetch the correct activity by Id
        if(mTypeOfActivity.equals(DISPLAY_KEY)) {
            mRunId = intent.getIntExtra(ID_KEY, 0);
            new LoadRunAsync(mRunId).execute();
        }



        //Create the LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        //Create the location callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        //Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        //Check if location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        builder.setNeedBle(true);




    }

    private class LoadRunAsync extends AsyncTask<Void, Void, Void>{

        int mId;

        public LoadRunAsync(int id){
            mId = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMapsViewModel.getRunById(mId);

            return null;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            googleMap.setMyLocationEnabled(true);
        }catch(SecurityException e){
            e.printStackTrace();
            checkPermissions();
        }

        if(mTypeOfActivity.equals(DISPLAY_KEY)){
            //Draw the mPath from the viewModel
            drawPolyline(mMapsViewModel.getCurrentRun().getPath());


        }else if(mTypeOfActivity.equals(RECORD_KEY)){
            getDescription();
            mMapsViewModel.setCurrentRun(new RecordedRun(mDescription));

        }

        getLastLocation();
        startLocationUpdates();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    /**
     * apply the location request and location callback to the fused location provider
     * to start getting location updates.
     */
    private void startLocationUpdates() {
        try {
            getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper());
        }catch(SecurityException e){
            e.printStackTrace();
            checkPermissions();
        }
    }

    /**
     * When location is updated the callback calls this method and provides the new location
     * If the activity is in record mode the activity is added to the current mPath and the location
     * is also added to the mPolyline that is beeing drawn on the map
     * @param location
     */
    public void onLocationChanged(Location location) {
        if(mTypeOfActivity.equals(RECORD_KEY)) {
            String msg = "Updated Location: " +
                    Double.toString(location.getLatitude()) + "," +
                    Double.toString(location.getLongitude());
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            LatLng latest = new LatLng(location.getLatitude(), location.getLongitude());
            mLocations.add(latest);
            if (mLocations.size() > 1) {
                mPolyline.setPoints(mLocations);
            }
        }


    }

    /**
     * Attempts to get the last known location of the device on startup
     */
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                if(mTypeOfActivity.equals(RECORD_KEY)) {
                                    LatLng lastKnown = new LatLng(location.getLatitude(), location.getLongitude());
                                    mLocations.add(lastKnown);
                                    mPath = new PolylineOptions().add(lastKnown);
                                    mPolyline = mMap.addPolyline(mPath);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, LAST_LOCATION_ERROR);
                            e.printStackTrace();
                        }
                    });
        }catch(SecurityException e){
            e.printStackTrace();
            checkPermissions();
        }
    }

    public void drawPolyline(ArrayList<Location> locations){

        Log.d("drawPoly", Integer.toString(locations.size()));

        for(int i = 0; i<locations.size();i++){
            LatLng tempLoc = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            mPath.add(tempLoc);
        }

        mPolyline = mMap.addPolyline(mPath);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude()),14));


    }

    public void stopLocationUpdates(){
        getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);
    }

    public void getDescription(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_TITLE);


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);

        builder.setPositiveButton(DIALOG_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDescription = input.getText().toString();
            }
        });
        builder.setNegativeButton(DIALOG_CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Methods for checking permissions and asking for permissions
    //Don't think they all are needed but didn't work before I added them

    public void checkPermissions(){
        checkFinePermissions();
        checkCoarsePermissions();
        checkInternetPermissions();
        checkNetworkPermissions();
    }

    private void checkFinePermissions() {
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestFinePermissions();
        }
    }

    private void requestFinePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_REQUEST_FINE_LOCATION);
    }

    private void checkInternetPermissions() {
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)) {
            requestInternetPermissions();
        }
    }

    private void requestInternetPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},
                MY_REQUEST_INTERNET);
    }

    private void checkCoarsePermissions() {
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ){
            requestCoarsePermissions();
        }
    }

    private void requestCoarsePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},
                MY_REQUEST_COARSE_LOCATION);
    }

    private void checkNetworkPermissions() {
        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED)) {
            requestNetworkPermissions();
        }
    }

    private void requestNetworkPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},
                MY_REQUEST_NETWORK_STATE);
    }


}
