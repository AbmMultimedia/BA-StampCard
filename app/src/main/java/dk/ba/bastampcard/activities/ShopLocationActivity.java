package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import dk.ba.bastampcard.R;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.ShopDBAdapter;
import dk.ba.bastampcard.model.Shop;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ShopLocationActivity extends Activity {

    Context context = this;
    private GoogleMap map;
    Shop shop;
    LatLng shopLocation;
    private DBAdapter db;
    private ShopDBAdapter sDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_location);

        long shopId = 1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shopId = extras.getInt("SHOP_ID");
        }

        db = new DBAdapter(this);
        sDB = new ShopDBAdapter(this);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        shop = getShopInfo(shopId);

        shopLocation = new LatLng(shop.getLatitude(), shop.getLongitude());

        showMap(map);
    }

    private Shop getShopInfo(long shopId)
    {
        db.open();
        sDB.open();

        Cursor shopCursor = sDB.getShop(shopId);

        String name = null;
        String address = null;
        String postalCode = null;
        String city = null;
        double latitude = 0;
        double longitude = 0;

        if(shopCursor.moveToFirst())
        {
            int nameIndex = shopCursor.getColumnIndex(sDB.KEY_NAME);
            name = shopCursor.getString(nameIndex);
            int addressIndex = shopCursor.getColumnIndex(sDB.KEY_ADDRESS);
            address = shopCursor.getString(addressIndex);
            int postalCodeIndex = shopCursor.getColumnIndex(sDB.KEY_POSTAL);
            postalCode = shopCursor.getString(postalCodeIndex);
            int cityIndex = shopCursor.getColumnIndex(sDB.KEY_CITY);
            city = shopCursor.getString(cityIndex);
            int latitudeIndex = shopCursor.getColumnIndex(sDB.KEY_LATITUDE);
            latitude = shopCursor.getDouble(latitudeIndex);
            int longitudeIndex = shopCursor.getColumnIndex(sDB.KEY_LONGITUDE);
            longitude = shopCursor.getDouble(longitudeIndex);
        }

        sDB.close();
        db.close();

        return shop = new Shop(name, address, postalCode, city, latitude, longitude);
    }

    public void showMap(GoogleMap map)
    {
        if (map != null)
        {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.setMyLocationEnabled(true);

            map.addMarker(new MarkerOptions()
                            .position(shopLocation)
                            .title(shop.getName())
                            .snippet(shop.getAddress() + " " + shop.getPostalCode() + " " + shop.getCity())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))

            );
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(shopLocation));

        // Change zoom level, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shop_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
