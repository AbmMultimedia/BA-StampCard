package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dk.ba.bastampcard.model.Product;

/**
 * Created by Benedicte on 05-11-2014.
 */
public class ProductDBAdapter {

    //--- Constants for various fields that are being created in the database ---
    //--- Product table - column names ---
    static final String KEY_ProductID = "productId";
    static final String KEY_ProductName = "productName";

    //--- Table name ---
    private static final String DATABASE_PRODUCT_TABLE = "product";

    private final Context proContext;
    private DatabaseHelper proDBHelper;
    private SQLiteDatabase proDB;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){ //Creates a new database if not exist
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        }
    }

    public ProductDBAdapter (Context ctx){
        this.proContext = ctx;
    }

    public ProductDBAdapter open() throws SQLException{
        this.proDBHelper = new DatabaseHelper(this.proContext);
        this.proDB = this.proDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.proDBHelper.close();
    }

    public long insertProduct(String productName){
        ContentValues values = new ContentValues();
        values.put(KEY_ProductName, productName);
        return this.proDB.insert(DATABASE_PRODUCT_TABLE, null, values);
    }
}
