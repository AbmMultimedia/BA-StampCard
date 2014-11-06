package dk.ba.bastampcard.activities;

import android.app.ListActivity;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.helpers.ShopListAdapter;
import dk.ba.bastampcard.model.Shop;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.ShopDBAdapter;

public class MainActivity extends ListActivity {

    private List<Shop> shops;
    DBAdapter db;
    ShopDBAdapter sDB;

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

        showAllShops();
    }

    private void showAllShops()
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

            Shop shop = new Shop(name, address, postalCode, city);
            shops.add(shop);
        }

        db.close();

        ShopListAdapter shopListAdapter = new ShopListAdapter(this, shops);
        setListAdapter(shopListAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
