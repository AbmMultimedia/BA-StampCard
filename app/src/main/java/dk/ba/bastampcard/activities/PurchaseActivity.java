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
import org.w3c.dom.Text;

import java.text.DecimalFormat;
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

/**
 * Created by Anders.
 */
public class PurchaseActivity extends Activity{

    private static final int confirmationCode = 676767;

    List<Purchase> purchaseList;
    User user;
    float totalValue;

    PriceListProductDBAdapter plpDB;
    ProductDBAdapter pDB;
    PurchaseDBAdapter purchaseDB;
    UserDBAdapter uDB;

    LinearLayout linearLayoutPurchases;
    Button btnScan;
    Button btnConfirm;
    Button btnUseStamps;

    DecimalFormat df;
    boolean payWithStamps;
    int stampUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        //Preparing view layout, hiding and showing buttons
        btnScan = (Button) findViewById(R.id.btnScanCode);
        btnConfirm = (Button) findViewById(R.id.btnConfirmPurchase);
        btnConfirm.setVisibility(View.GONE);
        btnUseStamps = (Button) findViewById(R.id.btnUseStamps);
        btnUseStamps.setVisibility(View.GONE);
        linearLayoutPurchases = (LinearLayout) findViewById(R.id.purchase_list);

        //Preparing database and list of purchases
        purchaseList = new ArrayList<Purchase>();
        plpDB = new PriceListProductDBAdapter(this);
        pDB = new ProductDBAdapter(this);
        uDB = new UserDBAdapter(this);
        purchaseDB = new PurchaseDBAdapter(this);

        //Getting the device user
        user = getUser(1);

        //Setting decimal format for prices
        df = new DecimalFormat("###,##0.00");

        payWithStamps = false;
    }

    //Request a QR code from the camera
    public void onClickScanCode(View view)
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);
    }

    //Method called when the result comes back from the camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Getting the result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            //Getting the content of the result
            String contents = result.getContents();
            if (contents != null) {
                btnScan.setVisibility(View.GONE);
                JSONObject jsPurchase = null;
                String purchase = contents;
                //String purchase = "{S:1,C:676767,PL:[{Pid:1,Q:2},{Pid:1,Q:3},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2},{Pid:1,Q:2}]}";

                //Creating an JSON object from the result
                try {
                    jsPurchase = new JSONObject(purchase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getPurchaseList(jsPurchase);
                showUseStamps();

            } else {

            }
        }
    }

    //Getting information about the user from the database
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

    //Creating the list of purchases from the JSON object
    public void getPurchaseList(JSONObject joPurchases)
    {
        Shop shop = null;
        int confirmationCodePurchase = 0;
        Date date = new Date();
        JSONArray products = null;

        //Getting information from the JSON object
        try {
            shop = new Shop(joPurchases.getInt("S"));
            confirmationCodePurchase = joPurchases.getInt("C");
            products = joPurchases.getJSONArray("PL");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Getting information from the database about the products in the JSON array
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

        //Checking the confirmation code
        if(isConfirmationCodeOk(confirmationCodePurchase)){
            showPurchases();
        }
        else{
            btnScan.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.confirmcode_not_ok, Toast.LENGTH_LONG).show();
            purchaseList.clear();
        }

    }

    //Showing information about the bought products in the view to the user
    private void showPurchases()
    {
        totalValue = 0;
        Log.d(this.getClass().getName(), Integer.toString(purchaseList.size()));
        for(Purchase p : purchaseList)
        {
            String productName = p.getPriceListProduct().getProduct().getName();
            float price = p.getPriceListProduct().getPrice();
            int quantity = p.getQuantity();
            float value = p.getValue();
            totalValue += value;
            TextView productInfo = new TextView(this);
            productInfo.setText(productName +" "+ price +" x"+ quantity +" : "+ value);
            linearLayoutPurchases.addView(productInfo);
        }

        TextView totalPrice = new TextView(this);
        totalPrice.setTextSize(22);
        totalPrice.setText( getString(R.string.total_price) +": "+ df.format(totalValue));

        linearLayoutPurchases.addView(totalPrice);
        btnConfirm.setVisibility(View.VISIBLE);

    }

    //Confirm the purchase and store them in the database
    public void onClickConfirmPurchase(View view)
    {
        Log.d(getClass().getName(), "Confirm purchase");
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
        btnUseStamps.setVisibility(View.GONE);
        btnScan.setVisibility(View.VISIBLE);
        linearLayoutPurchases.removeAllViews();

        //Calculate the new number of stamps for the user.
        calculateStamps();

        if(payWithStamps){
            payWithStamps = false;
        }

        purchaseList.clear();
    }

    //Calculate the number of stamps the user gets from the purchase
    private void calculateStamps()
    {
        int currentStamps = user.getStamps();
        int newStamps = (int) totalValue/Purchase.STAMP_PURCHASE_RATIO ;
        newStamps = newStamps + currentStamps;
        user.setStamps(newStamps);

        if(payWithStamps)
        {
            newStamps = user.getStamps() - stampUsed;
            user.setStamps(newStamps);
        }

        Toast.makeText(this, getString(R.string.new_stamps) +": "+ Integer.toString(newStamps), Toast.LENGTH_LONG).show();

        //Store the number of stamps in the database
        uDB.open();
        uDB.updateUserStamps(user.getId(), newStamps);
        uDB.close();
    }

    //Check the confirmation code
    private boolean isConfirmationCodeOk(int confirmCode)
    {
        boolean confirmCodeOk = false;
        if(confirmCode == confirmationCode){
            confirmCodeOk = true;
        }
        else{
            confirmCodeOk = false;
        }

        return confirmCodeOk;
    }

    //Use stamps to pay for the purchases
    public void onClickUseStamps(View view)
    {
        payWithStamps = true;
        float stampValue = user.getStamps()*Purchase.STAMP_USE_RATIO;
        float newPrice = 0;

        if(stampValue <= totalValue) {
            newPrice = totalValue - stampValue;
            stampUsed = (int) stampValue/Purchase.STAMP_USE_RATIO;
        }
        else{
            newPrice = 0;
            stampUsed = (int) totalValue/Purchase.STAMP_USE_RATIO;
        }

        totalValue = newPrice;

        TextView tvStampValue = new TextView(this);
        tvStampValue.setText( getString(R.string.stamps_value) +": "+ df.format(stampValue));
        TextView tvNewPrice = new TextView(this);
        tvNewPrice.setTextSize(22);
        tvNewPrice.setText( getString(R.string.your_price) +": "+ df.format(newPrice));

        linearLayoutPurchases.addView(tvStampValue);
        linearLayoutPurchases.addView(tvNewPrice);

        btnUseStamps.setVisibility(View.GONE);

    }

    //Checking if the user is can use stamps to pay for the purchases
    private void showUseStamps()
    {
        if(user.getStamps() < 5){
            btnUseStamps.setVisibility(View.GONE);
        }
        else{
            btnUseStamps.setVisibility(View.VISIBLE);
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
