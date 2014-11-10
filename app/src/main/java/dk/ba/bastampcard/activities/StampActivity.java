package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
            image.setBackgroundResource(R.drawable.cup_color);
            linearLayout1.addView(image);
        }

        for(int i = stamps; i < 5; i++){
            ImageView image = new ImageView(StampActivity.this);
            image.setBackgroundResource(R.drawable.cup_blackwhite);
            linearLayout1.addView(image);
        }
    }

    public void onClickUseStamps(View v){
        stamps = getNumberOfStamps();

        if(stamps == 5){
            uDB.open();
            uDB.updateStamps();
            uDB.close();
            collectStamps();
        } else {
            Toast.makeText(getApplicationContext(), "Not enough stamps", Toast.LENGTH_SHORT).show();
        }
    }
}
