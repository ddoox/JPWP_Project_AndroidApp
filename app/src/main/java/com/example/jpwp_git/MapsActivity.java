package com.example.jpwp_git;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //do map
    private GoogleMap mMap;
    Location Pozycja;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    //layout
    TextView PoleTekstowe1;
    TextView PoleTekstowe2;
    TextView PoleTekstowe3;
    Button Guzik;



    private void refresh() {

        //trzeba sprawdzić czy jest uprawnienie do lokalizacji
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              return;
        }


        //uzyskanie ostatniej lokalizacji za pomocą API Googla
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            Pozycja = location;
                            PoleTekstowe1.setText("Latitude  =" + Pozycja.getLatitude());
                            PoleTekstowe2.setText("Longitude  =" + Pozycja.getLongitude());
                            LatLng sydney = new LatLng(Pozycja.getLatitude(), Pozycja.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title("kek"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    }
                });
    } ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //ustawia jako ekran activity_maps.xml
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Tworzenie instancji Fused Location Provider Client
        //https://developer.android.com/training/location/retrieve-current
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // przypisanie elementów layoutu do zmiennych
        PoleTekstowe1 = findViewById(R.id.textView);
        PoleTekstowe2 = findViewById(R.id.textView2);
        PoleTekstowe3 = findViewById(R.id.textView3);
        Guzik = findViewById(R.id.button);


        //po naciśnięciu guzika odświeża pola tekstowe
        Guzik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoleTekstowe3.setText("test");
                refresh();
            }


        });
    }







    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Googl
     * e Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 10);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    }
