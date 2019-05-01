package com.example.jpwp_git;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //do map
    private GoogleMap mMap;
    Location PozycjaLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    LocationCallback locationCallback;
    Marker AktualnaPozycjaMarker;
    LatLng AktualnaPozycjaWspolrzedneLatLang;
    CameraPosition PozycjaKamery;

    //layout
    TextView PoleTekstowe1;
    TextView PoleTekstowe2;
    TextView PoleTekstowe3;
    Button Guzik;




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


        //utworzenie location request potrzebnego do uzyskania aktualnej lokacji, ustawia się parametry odświeżania lokalizacji i dokładność
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        //LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        //        .addLocationRequest(locationRequest);


        //klasa wykorzystywana do do otrzymywania aktualizacji przy zmianie położenia
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    //zmiany po ustaleniu lokacji
                    PozycjaLocation = location;
                    PoleTekstowe1.setText("Latitude  =" + PozycjaLocation.getLatitude());
                    PoleTekstowe2.setText("Longitude  =" + PozycjaLocation.getLongitude());
                    //AktualnaPozycjaMarker.setPosition(AktualnaPozycjaWspolrzedneLatLang);
                    //AktualnaPozycjaMarker = mMap.addMarker(new MarkerOptions().position(AktualnaPozycjaWspolrzedneLatLang).title("Marker in Sydney"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(AktualnaPozycjaWspolrzedneLatLang));
                    LatLng sydney = new LatLng(PozycjaLocation.getLatitude(), PozycjaLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("kek"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                }
            }


        };


















    }



    @Override
    protected void onResume() {
        super.onResume();
        if (true) { //warunek jesli otrzymywanie lokalizacji jest wlaczone - requestingLocationUpdates
            startLocationUpdates();
        }
    }


    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }


    // do obsługi klawisza
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

                            PozycjaLocation = location;
                            PoleTekstowe1.setText("Latitude  =" + PozycjaLocation.getLatitude());
                            PoleTekstowe2.setText("Longitude  =" + PozycjaLocation.getLongitude());
                            LatLng sydney = new LatLng(PozycjaLocation.getLatitude(), PozycjaLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title("kek"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    }
                });
    } ;


    // wywoływane po tym jak mapa jest gotowa do użytku
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // AktualnaPozycjaWspolrzedneLatLang = new LatLng(-34, 10);
       // AktualnaPozycjaMarker = mMap.addMarker(new MarkerOptions().position(AktualnaPozycjaWspolrzedneLatLang).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(AktualnaPozycjaWspolrzedneLatLang));

    }


}
