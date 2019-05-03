package com.JPWP;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import com.JPWP.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class MainMapa extends AppCompatActivity implements OnMapReadyCallback {


    public class Miejsce{

        private String _NazwaWydarzenia;
        private LatLng _LokalizacjaLatLng;
        private Marker _Marker;
        private Location _Location;
        private float _Odleglosc;

        //konstruktor automatycznie dodaje marker
        public Miejsce(String NazwaWydarzenia, LatLng LokalizacjaLatLng)
        {
            _NazwaWydarzenia = NazwaWydarzenia;
            _LokalizacjaLatLng = LokalizacjaLatLng;
            _Location = new Location(_NazwaWydarzenia);
            _Location.setLongitude(_LokalizacjaLatLng.longitude);
            _Location.setLatitude(_LokalizacjaLatLng.latitude);
            this.stworzMarker();
        }

        public String getNazwa()
        {
            return _NazwaWydarzenia;
        }

        public LatLng getLatLng()
        {
            return _LokalizacjaLatLng;
        }

        public Location getLocation()
        {
            return _Location;
        }

        public void stworzMarker() {
            _Marker = mMap.addMarker(new MarkerOptions().position(_LokalizacjaLatLng).title(_NazwaWydarzenia));
        }

        public float getOdleglosc() {
            this.obliczOdleglosc();
            return _Odleglosc;
        }

        public void obliczOdleglosc()
        {
            //używać tylko po ustaleniu lokalizacji
            if (AktualnaPozycjaLocation != null)
                _Odleglosc = (AktualnaPozycjaLocation.distanceTo(_Location)) / 1000;
            else
                _Odleglosc = -1;
        }


    }


    //do map
    public GoogleMap mMap;
    Location AktualnaPozycjaLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    LocationCallback locationCallback;
    Marker AktualnaPozycjaMarker;
    LatLng AktualnaPozycjaWspolrzedneLatLang;
    boolean PierwszeUstaleniePozycji = true;
    List<Miejsce> ListaWydarzen = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sprawdzenie czy są przyznane uprawnienia do lokalizacji, jeśli nie to o nie prosi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //ustawia jako ekran activity_maps.xml
        setContentView(R.layout.activity_maps);

        //zamiana domyślnego ActionBar na customowy ze swoim menu
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Tworzenie instancji Fused Location Provider Client, wykorzystywana do lokalizacji
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                    //zmiany po aktualizacji lokacji
                    AktualnaPozycjaLocation = location;

                    //zmiana pozycji głównego markera
                    AktualnaPozycjaWspolrzedneLatLang = new LatLng(AktualnaPozycjaLocation.getLatitude(), AktualnaPozycjaLocation.getLongitude());
                    AktualnaPozycjaMarker.setPosition(AktualnaPozycjaWspolrzedneLatLang);

                    //uaktualnianie odleglosci do markerow
                    for (int i = 0; i < ListaWydarzen.size(); i++)
                    {
                        ListaWydarzen.get(i).obliczOdleglosc();
                    }

                    //po pierwszym ustaleniu lokalizacji pokazanie markera, najazd kamery
                    if (PierwszeUstaleniePozycji) {
                        AktualnaPozycjaMarker.setVisible(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AktualnaPozycjaWspolrzedneLatLang, 10.0f));
                        PierwszeUstaleniePozycji = false;
                    }
                }
            }
        };


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //dodawanie swojego menu zamiast domyslnego
        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    //obsługa opcji menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.MenuOpcjaLista:
            {
                opcjaLista();
                return true;
            }

            case R.id.MenuOpcjaMapa:
            {
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void opcjaLista()
    {
        List<String> NazwaSwap,OdlegloscSwap;
        NazwaSwap = new ArrayList<>();
        OdlegloscSwap = new ArrayList<>();

        for(int i = 0; i < ListaWydarzen.size(); i++)
        {
            NazwaSwap.add(ListaWydarzen.get(i).getNazwa());
            OdlegloscSwap.add(Float.toString(ListaWydarzen.get(i).getOdleglosc()));
        }

        //przesłanie list do nowego activity
        Intent intent = new Intent(this, com.JPWP.ListaWydarzen.class);
        intent.putStringArrayListExtra("nazwa", (ArrayList<String>) NazwaSwap);
        intent.putStringArrayListExtra("odleglosc", (ArrayList<String>) OdlegloscSwap);
        startActivity(intent);

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


    // wywoływane po tym jak mapa jest gotowa do użytku
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //marker na aktualną pozycję, niebieski, początkowo ukryty, bo ma domyślną lokalizację
        AktualnaPozycjaMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(new LatLng(50, 20)).title("Tu jesteś"));
        AktualnaPozycjaMarker.setVisible(false);

        //markery na wydarzenia
        ListaWydarzen.add(new Miejsce("Wydarzenie111111111111111112222222222222222222222222222333333333333333333333333333333333333333", new LatLng(50.067790, 19.991362)));
        ListaWydarzen.add(new Miejsce("Wydarzenie2", new LatLng(50.077337, 19.981565)));
        ListaWydarzen.add(new Miejsce("Wydarsdgsdgsdgie3", new LatLng(50.087220, 19.891578)));
        ListaWydarzen.add(new Miejsce("Wydarzenie4", new LatLng(50.097985, 19.971365)));
        ListaWydarzen.add(new Miejsce("Wydretbzenie5", new LatLng(50.017802, 19.961765)));
        ListaWydarzen.add(new Miejsce("Wydfsdfssdffsdnie6", new LatLng(50.027795, 19.591987)));
        ListaWydarzen.add(new Miejsce("Wydareeeeeeeeeeeeeeeeeeeeeeeezenie7", new LatLng(49.178628, 19.941363)));
        ListaWydarzen.add(new Miejsce("Wydarzenie8", new LatLng(50.267790, 19.291365)));
        ListaWydarzen.add(new Miejsce("Wydasddddddddddddddddddie9", new LatLng(32.267790, 32.291365)));
        ListaWydarzen.add(new Miejsce("Wydarzenie10", new LatLng(51.267790, 20.291365)));
        ListaWydarzen.add(new Miejsce("Wydarzenie11", new LatLng(52.267790, 21.291365)));
        ListaWydarzen.add(new Miejsce("Wysgsdgsdgsdgdgsdgsdge12", new LatLng(53.267790, 18.291365)));
        ListaWydarzen.add(new Miejsce("Wydarzenie13", new LatLng(54.267790, 17.291365)));
    }


    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */);
    }


    private void stopLocationUpdates()
    {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }



}
