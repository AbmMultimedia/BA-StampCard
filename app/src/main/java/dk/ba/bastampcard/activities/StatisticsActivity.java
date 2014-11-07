package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import dk.ba.bastampcard.R;

/**
 * Created by Benedicte on 07-11-2014.
 */
public class StatisticsActivity extends Activity{

    TableLayout statisticsTable;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        statisticsTable = (TableLayout) findViewById(R.id.table_statistics);
    }

    @Override
    public void onResume(){
        super.onResume();

        for (int i=0; i < 5; i++){
            TableRow tr = new TableRow(this);
            tr.setId(1000 + i);

            TextView labelDate = new TextView(this);
            labelDate.setId(2000 + 1);
            labelDate.setText("31-10-2014");
            labelDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            tr.addView(labelDate);

            TextView labelProduct = new TextView(this);
            labelProduct.setId(3000 + i);
            labelProduct.setText("Caffe Latte");
            labelProduct.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            tr.addView(labelProduct);

            TextView labelShop = new TextView(this);
            labelShop.setId(4000 + i);
            labelShop.setText("Ultimatum");
            labelShop.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            tr.addView(labelShop);

            statisticsTable.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        }
    }
}
