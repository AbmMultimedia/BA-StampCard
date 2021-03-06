package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Benedicte on 05-11-2014.
 */
public class ShopDBAdapter {

    //--- Constants for various fields that are being created in the database ---
    //--- Shop table - column names ---
    public static final String KEY_RowID = "_id";
    public static final String KEY_NAME = "shopName";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_POSTAL = "postalCode";
    public static final String KEY_CITY = "city";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    //--- Table name ---
    private static final String DATABASE_SHOP_TABLE = "shop";

    private final Context sContext;
    private DatabaseHelper sDBHelper;
    private SQLiteDatabase sDB;

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

    public ShopDBAdapter (Context ctx){
        this.sContext = ctx;
    }

    public ShopDBAdapter open() throws SQLException {
        this.sDBHelper = new DatabaseHelper(this.sContext);
        this.sDB = this.sDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.sDBHelper.close();
    }

    //--- insert shop into database ---
    public long insertShop(String shopName, String address, int postal, String city){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shopName);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_POSTAL, postal);
        values.put(KEY_CITY, city);
        return this.sDB.insert(DATABASE_SHOP_TABLE, null, values);
    }

    //--- retrieves all shop ---
    public Cursor getAllShops(){
        return this.sDB.query(DATABASE_SHOP_TABLE, new String[] {KEY_RowID, KEY_NAME, KEY_ADDRESS, KEY_POSTAL, KEY_CITY, KEY_LATITUDE, KEY_LONGITUDE}, null, null, null, null, null);
    }

    //--- retrieves a particular shop ---
    public Cursor getShop(long rowId) throws SQLException{
        Cursor mCursor = this.sDB.query(true, DATABASE_SHOP_TABLE, new String[]{KEY_RowID, KEY_NAME, KEY_ADDRESS, KEY_POSTAL, KEY_CITY, KEY_LATITUDE, KEY_LONGITUDE}, KEY_RowID + "=" + rowId, null, null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return  mCursor;
    }
}
