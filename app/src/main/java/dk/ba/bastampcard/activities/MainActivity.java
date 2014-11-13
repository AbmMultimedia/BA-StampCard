package dk.ba.bastampcard.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.helpers.ShopListAdapter;
import dk.ba.bastampcard.model.Shop;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.ShopDBAdapter;

/**
 * Created by Anders.
 */
public class MainActivity extends ListActivity {

    private LocationManager locManager;
    private LocationListener locListener;
    private List<Shop> shops;
    private double latitude;
    private double longitude;
    private DBAdapter db;
    private ShopDBAdapter sDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Preparing local variables
        shops = new ArrayList<Shop>();
        db = new DBAdapter(this);
        sDB = new ShopDBAdapter(this);

        //Writing database to device if it doesn't exist
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Loading shops from the database and showing it in a ListView
        getAllShops();
        showAllShops();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Preparing the location listener and getting the location of the device
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new ShopListLocationListener();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locListener);
        Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        //Updating the list of shops
        showAllShops();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //Removing the location listener
        locManager.removeUpdates(locListener);
        locManager = null;
    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }

    //Getting the shops information from the database
    private void getAllShops()
    {
        db.open();
        sDB.open();

        Cursor shopCursor = sDB.getAllShops();

        while(shopCursor.moveToNext())
        {
            int nameIndex = shopCursor.getColumnIndex(sDB.KEY_NAME);
            String name = shopCursor.getString(nameIndex);
            int addressIndex = shopCursor.getColumnIndex(sDB.KEY_ADDRESS);
            String address = shopCursor.getString(addressIndex);
            int postalCodeIndex = shopCursor.getColumnIndex(sDB.KEY_POSTAL);
            String postalCode = shopCursor.getString(postalCodeIndex);
            int cityIndex = shopCursor.getColumnIndex(sDB.KEY_CITY);
            String city = shopCursor.getString(cityIndex);
            int latitudeIndex = shopCursor.getColumnIndex(sDB.KEY_LATITUDE);
            double latitude = shopCursor.getDouble(latitudeIndex);
            int longitudeIndex = shopCursor.getColumnIndex(sDB.KEY_LONGITUDE);
            double longitude = shopCursor.getDouble(longitudeIndex);

            Shop shop = new Shop(name, address, postalCode, city, latitude, longitude);

            int idIndex = shopCursor.getColumnIndex(sDB.KEY_RowID);
            int shopId = shopCursor.getInt(idIndex);
            shop.setId(shopId);

            shops.add(shop);
        }

        db.close();
    }

    //Showing the shops in a ListView
    private void showAllShops() {
        ShopListAdapter shopListAdapter = new ShopListAdapter(this, shops, latitude, longitude);
        setListAdapter(shopListAdapter);
    }

    //Creating the specific location listener.
    public class ShopListLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //Getting the location of the device
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            //Update the list of shops
            showAllShops();
        }

        //Checking the status of the location provider
        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {
            if(status == LocationProvider.AVAILABLE)
            {

            }
            else if(status == LocationProvider.OUT_OF_SERVICE)
            {
                Toast.makeText(getApplicationContext(), "Gps signal not available", Toast.LENGTH_SHORT).show();
            }
            else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE)
            {
                Toast.makeText(getApplicationContext(), "Waiting for Gps signal", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getApplicationContext(), "Gps is enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getApplicationContext(), "Gps is disabled", Toast.LENGTH_SHORT).show();
        }
    }

    //When a list item is clicked
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Get the id of the shop from the Tag in the view
        int shopId = (Integer) v.getTag();
        //Start the activity ShopLocation
        Intent intent = new Intent(getApplicationContext(), ShopLocationActivity.class);
        intent.putExtra("SHOP_ID", shopId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            case R.id.action_stamps:
                intent = new Intent(getApplicationContext(), StampActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_statistics:
                intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_weather:
                intent = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_purchase:
                intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                intent = new Intent(getApplicationContext(), ShareActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}