package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.database.DBAdapter;
import dk.ba.bastampcard.database.ProductDBAdapter;
import dk.ba.bastampcard.database.UserDBAdapter;
import dk.ba.bastampcard.model.Product;

/**
 * Created by Benedicte on 06-11-2014.
 */
public class StampActivity extends Activity {
    int stamps;
    UserDBAdapter uDB;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamps);

        uDB = new UserDBAdapter(this);
    }

    public void onResume(){
        super.onResume();
        collectStamps();
    }

    public int getNumberOfStamps(){
        uDB.open();
        int numberOfStamps = 0;

        Cursor cursor = uDB.getAllStamps();

        if(cursor.moveToFirst()){
            numberOfStamps = cursor.getInt(0);
        }
        uDB.close();
        return numberOfStamps;
    }

    public void collectStamps(){
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.stamp_img);
        linearLayout1.removeAllViewsInLayout();
        stamps = getNumberOfStamps();


        for(int x = 0; x < stamps; x++) {
            ImageView image = new ImageView(StampActivity.this);
            image.setPadding(10, 10, 10, 10);
            image.setImageResource(R.drawable.cup_color);
            linearLayout1.addView(image);
        }

        for(int i = stamps; i < 5; i++){
            ImageView image = new ImageView(StampActivity.this);
            image.setPadding(10, 10, 10, 10);
            image.setImageResource(R.drawable.cup_blackwhite);
            linearLayout1.addView(image);
        }
    }

    public void onClickUseStamps(View v){
        stamps = getNumberOfStamps();

        if(stamps >= 5){
            uDB.open();
            uDB.updateStamps();
            uDB.close();
            collectStamps();
        } else {
            Toast.makeText(getApplicationContext(), "Not enough stamps", Toast.LENGTH_SHORT).show();
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
