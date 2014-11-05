package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.PriceListProductDBAdapter;
import dk.ba.bastampcard.database.ProductDBAdapter;
import dk.ba.bastampcard.database.PurchaseDBAdapter;
import dk.ba.bastampcard.database.ShopDBAdapter;
import dk.ba.bastampcard.database.UserDBAdapter;

public class MainActivity extends Activity {

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

        //--- get all shops ---
//        db.open();
//        Cursor c = db.getAllShops();
//        if (c.moveToFirst()){
//            do{
//                DisplayShop(c);
//            } while (c.moveToNext());
//        }
//        db.close();
//    }
//
//    public void DisplayShop (Cursor c){
//        Toast.makeText(this,
//                "id: " + c.getString(0) + "\n" +
//                "Name: " + c.getString(1) + "\n" +
//                "Address: " + c.getString(2) + "\n" +
//                "Postal Code: " + c.getInt(3) + "\n" +
//                "City: " + c.getString(3),
//                Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    }
}
