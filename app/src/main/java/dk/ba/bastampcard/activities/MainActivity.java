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

        shops = new ArrayList<Shop>();
        db = new DBAdapter(this);
        sDB = new ShopDBAdapter(this);

        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getAllShops();
        showAllShops();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new ShopListLocationListener();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locListener);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        locManager.removeUpdates(locListener);
        locManager = null;
    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }

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

    private void showAllShops() {
        ShopListAdapter shopListAdapter = new ShopListAdapter(this, shops, latitude, longitude);
        setListAdapter(shopListAdapter);
    }


    public class ShopListLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            showAllShops();
        }

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

    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
        int shopId = (Integer) v.getTag();
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
            case R.id.action_stamps:
                intent = new Intent("dk.ba.bastampcard.activities.StampActivity");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        
    }
}
