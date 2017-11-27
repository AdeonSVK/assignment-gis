package sk.fiit.xrackol.evcharging;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;

import sk.fiit.xrackol.evcharging.java.ConnectionType;
import sk.fiit.xrackol.evcharging.java.JsonParser;
import sk.fiit.xrackol.evcharging.java.POI;
import sk.fiit.xrackol.evcharging.java.Settings;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private int init = 0;
    private ArrayList<POI> pois;
    public static UserLocation location;
    private boolean locationActive = false;
    private Menu menu;
    ProgressDialog PD;

    private String ip = "192.168.0.103";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiYWRlb24iLCJhIjoiY2o5d2luZXJvMG0wcjMzbDd4OGo0Y2tyOCJ9.AL8tzPL3zczRTx6jtt8iow");
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);

        Settings.settings = new Settings(true);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.mipmap.icon);

        location = new UserLocation(getApplicationContext(), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if(Settings.filterChanged || Settings.searchApplied){
            getMarkers();
            Settings.filterChanged = false;
            Settings.searchApplied = false;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        // block rotations
        mapboxMap.getUiSettings().setRotateGesturesEnabled(false);
        mapboxMap.getUiSettings().setTiltGesturesEnabled(false);

        getMarkers();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void createChargingStationPoi(){

    }

    public void getMarkers(){
        //create filter params
        String filterParams = Settings.createFilterParams();
        String url;

        if (Settings.settings.getSearchType() != null && Settings.settings.getSearchType().equals("nearby")){
            url ="http://"+ip+":8080/nearby"+filterParams;
        }
        else if (Settings.settings.getSearchType() != null && Settings.settings.getSearchType().equals("along")){
            url ="http://"+ip+":8080/along"+filterParams;
        }
        else if (Settings.settings.getSearchType() != null && Settings.settings.getSearchType().equals("somewhere")){
            url ="http://"+ip+":8080/somewhere"+filterParams;
        }
        else{
            url = "http://"+ip+":8080/pois"+filterParams;
        }

        // reset search setting
        Settings.settings.setSearchType(null);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        PD = new ProgressDialog(MainActivity.this);
        PD.setTitle("Please Wait..");
        PD.setMessage("Loading data...");
        PD.setCancelable(false);
        PD.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pois = new JsonParser().createPoiList(response);
                        drawMarkers(pois);
                        PD.dismiss();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ABC", error.toString());
                getNewIP();

                PD.dismiss();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void drawMarkers(ArrayList<POI> pois){
        mapboxMap.clear();
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon_grey = iconFactory.fromResource(R.drawable.grey_marker);
        Icon icon_darkgrey = iconFactory.fromResource(R.drawable.darkgrey_marker);
        Icon icon_red = iconFactory.fromResource(R.drawable.red_marker);
        Icon icon_blue = iconFactory.fromResource(R.drawable.blue_marker);
        Icon icon_green = iconFactory.fromResource(R.drawable.green_marker);
        Icon icon_yellow = iconFactory.fromResource(R.drawable.yellow_marker);
        Icon icon_cyan = iconFactory.fromResource(R.drawable.cyan_marker);
        Icon icon_orange = iconFactory.fromResource(R.drawable.orange_marker);
        Icon icon_purple = iconFactory.fromResource(R.drawable.purple_marker);

        if(pois == null) return;

        if(locationActive) showUserLocation();
        for(int i=0; i<pois.size(); i++){

            String connections = "";
            String cost = pois.get(i).getStation().getUsageCost().equals("") ? "Unknown" : pois.get(i).getStation().getUsageCost();
            String description = pois.get(i).getTown()+"\n"
                    +pois.get(i).getAddress()+"\n"
                    +"------\n"
                    +"Status: "+pois.get(i).getStation().getOperationalStatus()+"\n"
                    +"Usage: "+pois.get(i).getStation().getUsage()+"\n"
                    +"Cost: "+cost+"\n"
                    +"Operator: "+pois.get(i).getStation().getOperator()+"\n";

            for(int j=0; j<pois.get(i).getStation().getConnections().size();j++){
                ConnectionType con = pois.get(i).getStation().getConnections().get(j);
                connections = connections + "---------"+(j+1)+"---------\n"
                        +con.getName() + " x"+con.getQuantity()+"\n"
                        +"Power: "+con.getPowerKW()+" KW\n";
            }
            description = description + connections;


            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pois.get(i).getLatitude(), pois.get(i).getLongitude()))
                    .title(pois.get(i).getName())
                    .setSnippet(description)

            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_filter:
                intent = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(intent);
                break;
            case R.id.action_location:
                if(!locationActive){
                    menu.getItem(2).setIcon(getResources().getDrawable(R.mipmap.ic_my_location_green_24dp));
                    locationActive = true;
                    showUserLocation();
                }
                else{
                    locationActive = false;
                    menu.getItem(2).setIcon(getResources().getDrawable(R.mipmap.ic_my_location_black_24dp));
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void showUserLocation(){
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon_green = iconFactory.fromResource(R.drawable.green_marker);
        mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.myLocation.getLatitude(), location.myLocation.getLongitude()))
                        .title("You are here")
                .setIcon(icon_green)

        );
    }

    public void getNewIP(){
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.dialog_ip, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptsView);

        final EditText serverIp = (EditText) promptsView.findViewById(R.id.et_server_ip);
        serverIp.setText(ip);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                ip = serverIp.getText().toString();
                                getMarkers();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
