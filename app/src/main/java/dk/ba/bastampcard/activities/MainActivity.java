package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.helpers.SQLiteHelper;
import dk.ba.bastampcard.helpers.ShopListAdapter;
import dk.ba.bastampcard.model.Shop;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.PriceListProductDBAdapter;
import dk.ba.bastampcard.database.ProductDBAdapter;
import dk.ba.bastampcard.database.PurchaseDBAdapter;
import dk.ba.bastampcard.database.ShopDBAdapter;
import dk.ba.bastampcard.database.UserDBAdapter;

public class MainActivity extends ListActivity {

    private List<Shop> shops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("start activity", "start");
        DBAdapter db = new DBAdapter(this);
        ShopDBAdapter sDB = new ShopDBAdapter(this);
        UserDBAdapter uDB = new UserDBAdapter(this);
        PurchaseDBAdapter purDB = new PurchaseDBAdapter(this);
        ProductDBAdapter proDB = new ProductDBAdapter(this);
        PriceListProductDBAdapter plpDB = new PriceListProductDBAdapter(this);

        shops = new ArrayList<Shop>();
        Shop shopOne = new Shop("Shop 1", "Bymuren 106", "2650", "Hvidovre");
        Shop shopTwo = new Shop("Cafe Phenix", "Valby Langgade 74", "2500", "Valby");
        Shop shopThree = new Shop("Café Ultimatum", "Nordre Fasanvej 267", "2200", "København");

        shops.add(shopOne);
        shops.add(shopTwo);
        shops.add(shopThree);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item_shop, R.id.list_item_shop_name, values);

        ShopListAdapter shopListAdapter = new ShopListAdapter(this, shops);
        setListAdapter(shopListAdapter);
    }

    public void getGeolocation(View view) throws IOException {
        Shop shopOne = new Shop("Shop 1", "Bymuren 106", "2650", "Hvidovre");

        double shopLatitude;
        double shopLongitude;

        Geocoder geocoder = new Geocoder(this);
        String shopLocation = shopOne.getAddress() + ", " + shopOne.getPostalCode() + ", " + shopOne.getCity();
        //Log.d(this.getClass().getName(), shopLocation);
        List<Address> addresses =  geocoder.getFromLocationName("london",1);
        Log.d(this.getClass().getName(), "addresses :" + addresses.size());
        Address shopAddress = addresses.get(0);
        String locality = shopAddress.getLocality();
        Log.d(this.getClass().getName(), locality);

        if(addresses.size() > 0) {
            shopLatitude = addresses.get(0).getLatitude();
            shopLongitude = addresses.get(0).getLongitude();
        }
    }

        //--- add a shop ---
        db.open();
        sDB.open();
        long shopId = sDB.insertShop("KoffeeHouse", "Høvej 3", 2309, "Bisserup");
        uDB.open();
        long userId = uDB.insertUser("Benedicte Veng Christensen");
        purDB.open();
        long purchaseId = purDB.createPurchase(2, 2, 4, "A4-8976", 49, "20-09-2014");
        proDB.open();
        long productId = proDB.insertProduct("Caffe Latte");
        plpDB.open();
        long priceListProductId = plpDB.insertPriceListProduct(1, 3, 35);
        db.close();

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
