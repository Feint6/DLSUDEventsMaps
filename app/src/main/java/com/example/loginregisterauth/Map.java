package com.example.loginregisterauth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.loginregisterauth.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Map extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    Button placedetails;

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    SupportMapFragment mapFragment;
    private static final int LOCATION_PERMISSION_CODE = 101;

    private LatLng cscs = new LatLng(14.320297867523124, 120.96299338350136);
    private LatLng ceat = new LatLng(14.3229061585126, 120.9583943964856);
    private LatLng ccje = new LatLng(14.322529066456635, 120.95958545386446);
    private LatLng cthm = new LatLng(14.32206577062579, 120.96279020359026);
    private LatLng cbaa = new LatLng(14.32393360551646, 120.9582090543651);
    private LatLng library = new LatLng(14.32071969006479, 120.96198708337597);
    private LatLng pch = new LatLng(14.320930766377842, 120.96291665586041);
    private LatLng registrar = new LatLng(14.320749170787145, 120.96336745917377);
    private LatLng ictc = new LatLng(14.322474741976983, 120.96298635681016);
    private LatLng alumni = new LatLng(14.322779451084301, 120.96115116355617);
    private LatLng museo = new LatLng(14.321346162013363, 120.96117178690498);
    private LatLng dorm = new LatLng(14.321008981187193, 120.95971678405523);
    private LatLng cth = new LatLng(14.323459003226395, 120.95925361252054);
    private LatLng ldh = new LatLng(14.322617989253372, 120.95977996074183);
    private LatLng gmh = new LatLng(14.32295366208585, 120.95912701203714);
    private LatLng fch = new LatLng(14.322555683549686, 120.95970404087599);
    private LatLng uls = new LatLng(14.326922678892938, 120.95740871600518);
    private LatLng hs = new LatLng(14.325548114797748, 120.95897066874653);
    private LatLng swafo = new LatLng(14.322486321776534, 120.96346738275314);

    private Marker mcscs;
    private Marker mceat;
    private Marker mccje;
    private Marker mcthm;
    private Marker mcbaa;
    private Marker mlibrary;
    private Marker mpch;
    private Marker mregistrar;
    private Marker mictc;
    private Marker malumni;
    private Marker mmuseo;
    private Marker mdorm;
    private Marker mcth;
    private Marker mldh;
    private Marker mgmh;
    private Marker mfch;
    private Marker muls;
    private Marker mhs;
    private Marker mswafo;


    private ArrayList<LatLng> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        placedetails = (Button) findViewById(R.id.placeDetais);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //permission to access user location
        if (isLocationPermissionGranted()){
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        else {
            requestLocationPermission();
        }


        locationArrayList = new ArrayList<>();

        locationArrayList.add(cscs);
        locationArrayList.add(ceat);
        locationArrayList.add(cthm);
        locationArrayList.add(ccje);
        locationArrayList.add(cbaa);
        locationArrayList.add(library);
        locationArrayList.add(registrar);
        locationArrayList.add(pch);
        locationArrayList.add(ictc);
        locationArrayList.add(alumni);
        locationArrayList.add(museo);
        locationArrayList.add(dorm);
        locationArrayList.add(cth);
        locationArrayList.add(ldh);
        locationArrayList.add(gmh);
        locationArrayList.add(fch);
        locationArrayList.add(uls);
        locationArrayList.add(hs);
        locationArrayList.add(swafo);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in DLSUD and move the camera
        LatLng dlsud = new LatLng(14.3269, 120.9575);
        //mMap.addMarker(new MarkerOptions().position(dlsud).title("De La Salle University - Dasmarinas"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dlsud));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }

        List<Marker> markerList = new ArrayList<>();

        mcscs = mMap.addMarker(new MarkerOptions().position(cscs).title("College of Science Building"));
        mcscs.setTag(0);
        markerList.add(mcscs);

        mcbaa = mMap.addMarker(new MarkerOptions().position(cbaa).title("College of Business Administration and Accountancy"));
        mcbaa.setTag(0);
        markerList.add(mcbaa);

        mceat = mMap.addMarker(new MarkerOptions().position(ceat).title("College of Engineering and Architecture"));
        mceat.setTag(0);
        markerList.add(mceat);

        mccje = mMap.addMarker(new MarkerOptions().position(ccje).title("College of Criminal Justice Education"));
        mccje.setTag(0);
        markerList.add(mccje);

        mcthm = mMap.addMarker(new MarkerOptions().position(cthm).title("College of Tourism and Hospitality Management"));
        mcthm.setTag(0);
        markerList.add(mcthm);

        mregistrar = mMap.addMarker(new MarkerOptions().position(registrar).title("Ayuntamiento Building - Office of Registrar, Center of Student Admissions"));
        mregistrar.setTag(0);
        markerList.add(mregistrar);

        mlibrary = mMap.addMarker(new MarkerOptions().position(library).title("Aklatang Emilio Aguinaldo"));
        mlibrary.setTag(0);
        markerList.add(mlibrary);

        mpch = mMap.addMarker(new MarkerOptions().position(pch).title("Paulo Campos Hall"));
        mpch.setTag(0);
        markerList.add(mpch);

        mictc = mMap.addMarker(new MarkerOptions().position(ictc).title("Information and Communications Technology Building"));
        mictc.setTag(0);
        markerList.add(mictc);

        malumni = mMap.addMarker(new MarkerOptions().position(alumni).title("Alumni Building"));
        malumni.setTag(0);
        markerList.add(malumni);

        mmuseo = mMap.addMarker(new MarkerOptions().position(museo).title("Museo De La Salle"));
        mmuseo.setTag(0);
        markerList.add(mmuseo);

        mdorm = mMap.addMarker(new MarkerOptions().position(dorm).title("Student Dormitories"));
        mdorm.setTag(0);
        markerList.add(mdorm);

        mcth = mMap.addMarker(new MarkerOptions().position(cth).title("Candido Tirona Hall"));
        mcth.setTag(0);
        markerList.add(mcth);

        mldh = mMap.addMarker(new MarkerOptions().position(ldh).title("Ladislao Diwa Hall"));
        mldh.setTag(0);
        markerList.add(mldh);

        mgmh = mMap.addMarker(new MarkerOptions().position(gmh).title("Gregoria Montoya Hall"));
        mgmh.setTag(0);
        markerList.add(mgmh);

        mfch = mMap.addMarker(new MarkerOptions().position(fch).title("Felipe Calderon Hall"));
        mfch.setTag(0);
        markerList.add(mfch);

        muls = mMap.addMarker(new MarkerOptions().position(uls).title("Ugnayang La Salle"));
        muls.setTag(0);
        markerList.add(muls);

        mhs = mMap.addMarker(new MarkerOptions().position(hs).title("DLSU-D High School"));
        mhs.setTag(0);
        markerList.add(mhs);

        mswafo = mMap.addMarker(new MarkerOptions().position(swafo).title("Student Welfare and Formation Office"));
        mswafo.setTag(0);
        markerList.add(mswafo);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.getTitle().equals("Aklatang Emilio Aguinaldo")) {
                    Intent intent = new Intent(Map.this, MapPlaceDetails.class);
                    startActivity(intent);
                }
                if (marker.getTitle().equals("Felipe Calderon Hall")) {
                    Intent intent = new Intent(Map.this, DetailsFCH.class);
                    startActivity(intent);
                }
                if (marker.getTitle().equals("College of Science Building")) {
                    Intent intent = new Intent(Map.this, DetailsCSCS.class);
                    startActivity(intent);
                }

                if (marker.getTitle().equals("Candido Tirona Hall")) {
                    Intent intent = new Intent(Map.this, DetailsCTH.class);
                    startActivity(intent);
                }
                if (marker.getTitle().equals("Gregoria Montoya Hall")) {
                    Intent intent = new Intent(Map.this, DetailsGMH.class);
                    startActivity(intent);
                }


                return false;
            }
        });

        googleMap.setOnInfoWindowClickListener(this);
    }

    private boolean isLocationPermissionGranted () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }

        else { return false;}
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {



    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        mlibrary.getPosition();
        placedetails.setVisibility(View.VISIBLE);
        placedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapPlaceDetails.class));
            }
        });


        return false;
    }
}