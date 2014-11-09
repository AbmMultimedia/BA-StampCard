package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.ProductDBAdapter;
import dk.ba.bastampcard.database.PurchaseDBAdapter;
import dk.ba.bastampcard.database.ShopDBAdapter;
import dk.ba.bastampcard.model.Product;
import dk.ba.bastampcard.model.Shop;

/**
 * Created by Benedicte on 07-11-2014.
 */
public class StatisticsActivity extends Activity{

    TableLayout statisticsTable;
    PurchaseDBAdapter purDB;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        purDB = new PurchaseDBAdapter(this);
        statisticsTable = (TableLayout) findViewById(R.id.table_statistics);
    }

    @Override
    public void onResume(){
        super.onResume();
        purDB.open();

        Cursor cursor = purDB.getStatistics();

        cursor.moveToFirst();
        do {
            String purchaseDate = cursor.getString(0);
            String shopName = cursor.getString(1);
            String productName = cursor.getString(2);

            //Creating new rows to the statistics table
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView dateField = (TextView) findViewById(R.id.statistics_cell_date);
            ViewGroup.LayoutParams params= dateField.getLayoutParams();
            TextView labelDate = new TextView(this);
            labelDate.setText(purchaseDate);
            labelDate.setLayoutParams(params); //Gets the layout parameters from TextView with id statistics_cell_date, so header and rows have the same layout
            labelDate.setGravity(Gravity.CENTER);
            tr.addView(labelDate);

            TextView shopField = (TextView) findViewById(R.id.statistics_cell_shop);
            ViewGroup.LayoutParams shopParams = shopField.getLayoutParams();
            TextView labelShop = new TextView(this);
            labelShop.setText(shopName);
            labelShop.setLayoutParams(shopParams); //Gets the layout parameters from TextView with id statistics_cell_shop, so header and rows have the same layout
            labelShop.setGravity(Gravity.CENTER);
            tr.addView(labelShop);

            TextView productField = (TextView) findViewById(R.id.statistics_cell_product);
            ViewGroup.LayoutParams productParams = productField.getLayoutParams();
            TextView labelProduct = new TextView(this);
            labelProduct.setText(productName);
            labelProduct.setLayoutParams(productParams); //Gets the layout parameters from TextView with id statistics_cell_product, so header and rows have the same layout
            labelProduct.setGravity(Gravity.CENTER);
            tr.addView(labelProduct);

            statisticsTable.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        } while (cursor.moveToNext());

        purDB.close();
    }
}
