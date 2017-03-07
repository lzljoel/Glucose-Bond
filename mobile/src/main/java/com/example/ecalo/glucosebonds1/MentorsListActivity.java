

package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kangsik on 4/27/16.
 */
public class MentorsListActivity extends AppCompatActivity {

    private Double latitude;
    private Double longitude;
    private Context context;


    //For map
    GoogleMap googleMap;
    private String currentLatString;
    private String currentLngString;

    //For Addresses
    private BaasBox client;
    private ArrayList<LatLng> mentorsPointsList = new ArrayList<LatLng>();
    private Long numDocs;


    //for ListView
    private ListView mentorListView;
    private ArrayList<String> mentorsNames;
    private String name;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //For View
        setContentView(R.layout.mentors_list);

        System.out.println("=============================");
        System.out.println("This is MentorsListActivity");
        System.out.println("=============================");
        //Mentors List Heatmap

        BaasDocument.count("mentorAddresses",new BaasHandler<Long> () {
            @Override
            public void handle(BaasResult<Long> res) {
                if (res.isSuccess()) {
                    Log.d("LOG","visible document count is: "+res.value());
                    numDocs = res.value();
                } else {
                    Log.e("LOG","Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        });

        mentorsNames = new ArrayList<String>();


        //Get mentor Addresses and put heat map when all addresses are pulled from server.
        BaasDocument.fetchAll("mentorAddresses",
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {

                        if (res.isSuccess()) {
                            for (BaasDocument doc : res.value()) {
                                Log.d("LOG", "Doc: " + doc);
                                /*this is so hacky way to do it, basically, if condition checks
                                whether all documents are pulled from server by size of documents,
                                then, if it is the last one, we put heat map.
                                 */
                                address = doc.getString("Address");
                                name = doc.getString("Name");
                                //For Map
                                mentorsPointsList.add(getLatLngFromAddress(address));
                                //For ListView
                                mentorsNames.add(name);
                                if(mentorsPointsList.size() == numDocs){
                                    HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                                            .data(mentorsPointsList)
                                            .radius(50)
                                            .build();
                                    // Add a tile overlay to the map, using the heat map tile provider.
                                    googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                                }else{

                                }
                                //
                            }
                        } else {
                            Log.e("LOG", "Error", res.error());

                        }
                    }
                });








        //Get Intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        currentLatString = extras.getString("LATITUDE");
        currentLngString= extras.getString("LONGITUDE");
        latitude = Double.parseDouble(currentLatString);
        longitude = Double.parseDouble(currentLngString);





        //Get map & user's current location
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        final LatLng currentLocation = new LatLng(latitude , longitude);
        Marker TP = googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        CameraUpdate center = CameraUpdateFactory.newLatLng(currentLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);


        //For List View

        mentorListView = (ListView) findViewById(R.id.listViewMentors);



        final MyAdapter mentorAdapter = new MyAdapter(MentorsListActivity.this, mentorsNames);

        mentorListView.setAdapter(mentorAdapter);

        mentorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent disscussIntent = new Intent(context, MessageActivity.class);
                Bundle extras = new Bundle();
                String mentorName = (String) adapterView.getItemAtPosition(position);


                extras.putString("MENTOR_NAME", mentorName);
                extras.putBoolean("IS_MENTOR", false);

                disscussIntent.putExtras(extras);
                startActivity(disscussIntent);

            }
        });



        /* Original
        Double[] mentorPoint1 = getLocationFromAddress("2232 Durant Ave , CA");
        final LatLng point1 = new LatLng(mentorPoint1[0] , mentorPoint1[1]);
        Double[] mentorPoint2 = getLocationFromAddress("6400 Christie Ave , CA");
        final LatLng point2 = new LatLng(mentorPoint2[0] , mentorPoint2[1]);
        Double[] mentorPoint3 = getLocationFromAddress("2234 Durant Ave , CA");
        final LatLng point3 = new LatLng(mentorPoint3[0] , mentorPoint3[1]);
        */



        /*/////
        final LatLng point1 = getLatLngFromAddress("2232 Durant Ave , CA");
        final LatLng point2 = getLatLngFromAddress("2234 Durant Ave , CA");
        final LatLng point3 = getLatLngFromAddress("2236 Durant Ave , CA");
        final LatLng point4 = getLatLngFromAddress("2238 Durant Ave , CA");
        final LatLng point5 = getLatLngFromAddress("2239 Durant Ave , CA");
        mentorsPointsList.add(point1);
        mentorsPointsList.add(point2);
        mentorsPointsList.add(point3);
        mentorsPointsList.add(point4);
        mentorsPointsList.add(point5);




        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(mentorsPointsList)
                .radius(50)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        */////




        //Adding Additional Marker
        /*

        googleMap.addMarker(new MarkerOptions()
                .position(mentorPoint)
                .title("MentorPoint")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .alpha(0.7f));
        */



    }
    /*
    public Double[] getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            Double[] point = new Double[2];
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location = address.get(0);
            point[0] = location.getLatitude();
            point[1] = location.getLongitude();
            return point;
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    */

    public LatLng getLatLngFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address==null) {
                return null;
            }
            Address location = address.get(0);
            LatLng pointLL = new LatLng(location.getLatitude() ,location.getLongitude());
            return pointLL;

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
