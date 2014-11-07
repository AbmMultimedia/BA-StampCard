package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import dk.ba.bastampcard.R;
import dk.ba.bastampcard.model.Shop;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_location);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        shop = new Shop("Shop name", "Adrresse 1", "2222", "Copenahgen", 55, 12);
        shopLocation = new LatLng(shop.getLatitude(), shop.getLongitude());
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
