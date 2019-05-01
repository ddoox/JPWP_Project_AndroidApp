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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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


    private class Miejsce{

        private String _NazwaWydarzenia;
        private LatLng _LokalizacjaLatLng;
        private int _MarkerID;

        public Miejsce(int MarkerID, String NazwaWydarzenia, LatLng LokalizacjaLatLng)
        {
            _NazwaWydarzenia = NazwaWydarzenia;
            _LokalizacjaLatLng = LokalizacjaLatLng;
            _MarkerID = MarkerID;
        }

        public String getNazwa()
        {
            return _NazwaWydarzenia;
        }
        public LatLng getLatLng(){
            return _LokalizacjaLatLng;
        }
        public void StworzMarker()
        {
            Marker _MarkerID =  mMap.addMarker(new MarkerOptions().position(_LokalizacjaLatLng).title(_NazwaWydarzenia));
        }

    }


    //do map
    public GoogleMap mMap;
    Location PozycjaLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    LocationCallback locationCallback;
    Marker AktualnaPozycjaMarker;
    LatLng AktualnaPozycjaWspolrzedneLatLang;
    CameraPosition PozycjaKamery;
    boolean PierwszeUstaleniePozycji = true;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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


         //klasa wykorzystywana do do otrzymywania aktualizacji przy zmianie położenia
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                {
                    return;
                }
                for (Location location : locationResult.getLocations())
                {
                    //zmiany po ustaleniu lokacji
                    PozycjaLocation = location;
                    PoleTekstowe1.setText("Latitude  =" + PozycjaLocation.getLatitude());
                    PoleTekstowe2.setText("Longitude  =" + PozycjaLocation.getLongitude());

                    //zmiana pozycji głównego markera
                    AktualnaPozycjaWspolrzedneLatLang = new LatLng(PozycjaLocation.getLatitude(),PozycjaLocation.getLongitude());
                    AktualnaPozycjaMarker.setPosition(AktualnaPozycjaWspolrzedneLatLang);

                    if(PierwszeUstaleniePozycji)
                    {
                        AktualnaPozycjaMarker.setVisible(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AktualnaPozycjaWspolrzedneLatLang, 10.0f));
                        PierwszeUstaleniePozycji = false;
                    }
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


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    // wywoływane po tym jak mapa jest gotowa do użytku
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //marker na aktualną pozycję, niebieski, początkowo ukryty, bo ma domyślną lokalizację, po ustaleniu się pojawi
        AktualnaPozycjaMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(new LatLng(50,20)).title("Tu jesteś"));
        AktualnaPozycjaMarker.setVisible(false);


        //markery na wydarzenia
        Miejsce Tauron = new Miejsce(1,"Tauron",new LatLng(50.067790, 19.991360));
        Tauron.StworzMarker();

    }


    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }


    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
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





}
