package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.database.PriceListProductDBAdapter;
import dk.ba.bastampcard.database.ProductDBAdapter;
import dk.ba.bastampcard.model.PriceListProduct;
import dk.ba.bastampcard.model.Product;
import dk.ba.bastampcard.model.Purchase;
import dk.ba.bastampcard.model.Shop;
import dk.ba.bastampcard.model.User;

public class PurchaseActivity extends Activity{

    List<Purchase> purchaseList;
    PriceListProductDBAdapter plpDB;
    ProductDBAdapter pDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        purchaseList = new ArrayList<Purchase>();
        plpDB = new PriceListProductDBAdapter(this);
        pDB = new ProductDBAdapter(this);

        JSONObject jsPurchase = null;
        String purchase = "{S:1,C:676767,PL:[{Pid:1,Q:2}]}";
        try {
            jsPurchase = new JSONObject(purchase);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getPurchaseList(jsPurchase);
    }

    private void scanCode()
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        intent.putExtra("SCAN_WIDTH", 800);
        intent.putExtra("SCAN_HEIGHT", 200);
        //intent.putExtra("RESULT_DISPLAY_DURATION_MS", 3000L);
        //intent.putExtra("PROMPT_MESSAGE", "Custom prompt to scan a product");
        startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                Toast.makeText(this, contents,Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }

    public void getPurchaseList(JSONObject joPurchases)
    {
        User user = new User("Hans");
        Shop shop = null;
        int confirmationCode = 0;
        Date date = new Date();

        JSONArray products = null;
        try {
            shop = new Shop(joPurchases.getInt("S"));
            confirmationCode = joPurchases.getInt("C");
            products = joPurchases.getJSONArray("PL");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i < products.length(); i++)
        {
            JSONObject joProduct = null;
            Product product = null;
            int quantity  = 0;
            PriceListProduct priceListProduct = null;

            try {
                joProduct = products.getJSONObject(i);
                product = new Product(joProduct.getInt("Pid"));
                quantity = joProduct.getInt("Q");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            pDB.open();

            Cursor productCursor = pDB.getProduct(product.getId());
            if(productCursor.moveToFirst())
            {
                int productNameIndex = productCursor.getColumnIndex(pDB.KEY_ProductName);
                product.setName(productCursor.getColumnName(productNameIndex));
            }

            plpDB.open();
            Cursor plpCursor = plpDB.getPriceListProduct(product.getId(), shop.getId());

            if(plpCursor.moveToFirst()) {
                int priceIndex = plpCursor.getColumnIndex(plpDB.KEY_PRICE);
                float price = plpCursor.getFloat(priceIndex);

                priceListProduct = new PriceListProduct(product, shop, price);
            }

            pDB.close();
            plpDB.close();

            Purchase purchase = new Purchase(priceListProduct, shop, user, confirmationCode, quantity , date);
            purchaseList.add(purchase);
        }

        showPurchaces();
    }

    private void showPurchaces()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.d(this.getClass().getName(), Integer.toString(purchaseList.size()));
        for(Purchase p : purchaseList)
        {
            String productName;
            TextView productInfo = new TextView(this);

            //Toast.makeText(this, dateFormat.format(p.getDate()), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, Float.toString(p.getValue()), Toast.LENGTH_SHORT).show();
        }
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
