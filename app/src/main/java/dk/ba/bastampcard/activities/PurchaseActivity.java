package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import dk.ba.bastampcard.database.PurchaseDBAdapter;
import dk.ba.bastampcard.database.UserDBAdapter;
import dk.ba.bastampcard.model.PriceListProduct;
import dk.ba.bastampcard.model.Product;
import dk.ba.bastampcard.model.Purchase;
import dk.ba.bastampcard.model.Shop;
import dk.ba.bastampcard.model.User;

public class PurchaseActivity extends Activity{

    List<Purchase> purchaseList;
    User user;

    PriceListProductDBAdapter plpDB;
    ProductDBAdapter pDB;
    PurchaseDBAdapter purchaseDB;
    UserDBAdapter uDB;

    LinearLayout linearLayoutPurchases;
    Button btnScan;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        btnScan = (Button) findViewById(R.id.btnScanCode);
        btnConfirm = (Button) findViewById(R.id.btnConfirmPurchase);
        btnConfirm.setVisibility(View.GONE);

        linearLayoutPurchases = (LinearLayout) findViewById(R.id.purchase_list);

        purchaseList = new ArrayList<Purchase>();
        plpDB = new PriceListProductDBAdapter(this);
        pDB = new ProductDBAdapter(this);
        uDB = new UserDBAdapter(this);
        purchaseDB = new PurchaseDBAdapter(this);

        user = getUser(1);
    }

    public void onClickScanCode(View view)
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                //Toast.makeText(this, contents,Toast.LENGTH_LONG).show();
                btnScan.setVisibility(View.GONE);
                JSONObject jsPurchase = null;
                String purchase = contents;
                //String purchase = "{S:1,C:676767,PL:[{Pid:1,Q:2},{Pid:1,Q:3},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2}]}";
                try {
                    jsPurchase = new JSONObject(purchase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getPurchaseList(jsPurchase);

            } else {

            }
        }
    }

    private User getUser(int userId)
    {
        User u = null;
        uDB.open();
        Cursor userCursor = uDB.getUser(userId);
        if(userCursor.moveToFirst())
        {
            int indexUserName = userCursor.getColumnIndex(uDB.KEY_USERNAME);
            String usersName = userCursor.getString(indexUserName);
            int indexUserStamps = userCursor.getColumnIndex(uDB.KEY_STAMPS);
            int userStamps = userCursor.getInt(indexUserStamps);

            u = new User(usersName);
            u.setStamps(userStamps);
            u.setId(userId);
        }

        uDB.close();

        return u;
    }

    public void getPurchaseList(JSONObject joPurchases)
    {
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
                product.setName(productCursor.getString(productNameIndex));
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

        showPurchases();
    }

    private void showPurchases()
    {
        Log.d(this.getClass().getName(), Integer.toString(purchaseList.size()));
        for(Purchase p : purchaseList)
        {
            String productName = p.getPriceListProduct().getProduct().getName();
            float price = p.getPriceListProduct().getPrice();
            int quantity = p.getQuantity();
            float value = p.getValue();
            TextView productInfo = new TextView(this);
            productInfo.setText(productName +" "+ price +" x"+ quantity +" : "+ value);
            linearLayoutPurchases.addView(productInfo);
        }
        btnConfirm.setVisibility(View.VISIBLE);

    }

    public void onClickConfirmPurchase(View view)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        purchaseDB.open();
        for(Purchase p : purchaseList)
        {
            int productId = p.getPriceListProduct().getProduct().getId();
            int shopId = p.getShop().getId();
            int userId = user.getId();
            int quantity = p.getQuantity();
            String date = dateFormat.format(p.getDate());
            purchaseDB.createPurchase(productId, shopId, userId, quantity, date);
        }
        Toast.makeText(this, R.string.purchase_confirmed , Toast.LENGTH_SHORT).show();
        purchaseDB.close();
        btnConfirm.setVisibility(View.GONE);
        btnScan.setVisibility(View.VISIBLE);
        linearLayoutPurchases.removeAllViews();
        purchaseList.clear();

        calculateStamps();
    }

    private void calculateStamps()
    {
        int currentStamps = user.getStamps();
        int newStamps = 0;
        for(Purchase p : purchaseList)
        {
            newStamps =+ (int) p.getValue()/Purchase.STAMP_RATIO ;
        }

        newStamps = newStamps + currentStamps;

        uDB.open();
        uDB.updateUserStamps(user.getId(), newStamps);
        uDB.close();
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
