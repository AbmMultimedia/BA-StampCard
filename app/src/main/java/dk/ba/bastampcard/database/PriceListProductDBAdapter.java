package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Benedicte on 05-11-2014.
 */
public class PriceListProductDBAdapter {

    //--- Constants for various fields that are being created in the database ---
    //--- Product table - column names ---
    static final String KEY_PriceListProductID = "priceListProductId";
    static final String KEY_ProductID = "productId";
    static final String KEY_ShopID = "shopId";
    static final String KEY_PRICE = "price";

    //--- Table name ---
    private static final String DATABASE_PriceListProduct_TABLE = "priceListProduct";

    private final Context plpContext;
    private DatabaseHelper plpDBHelper;
    private SQLiteDatabase plpDB;

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

    public PriceListProductDBAdapter (Context ctx){
        this.plpContext = ctx;
    }

    public PriceListProductDBAdapter open() throws SQLException {
        this.plpDBHelper = new DatabaseHelper(this.plpContext);
        this.plpDB = this.plpDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.plpDBHelper.close();
    }

    public long insertPriceListProduct(int productId, int shopId, int price){
        ContentValues values = new ContentValues();
        values.put(KEY_ProductID, productId);
        values.put(KEY_ShopID, shopId);
        values.put(KEY_PRICE, price);
        return this.plpDB.insert(DATABASE_PriceListProduct_TABLE, null, values);
    }
}
