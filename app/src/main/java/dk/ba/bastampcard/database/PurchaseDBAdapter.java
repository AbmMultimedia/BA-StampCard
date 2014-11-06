package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Benedicte on 05-11-2014.
 */
public class PurchaseDBAdapter {

    //--- Constants for various fields that are being created in the database ---
    //--- Purchase table - column names ---
    static final String KEY_PurchaseID = "purchaseId";
    static final String KEY_PURCHASE_ProductID = "productId";
    static final String KEY_PURCHASE_ShopID = "shopId";
    static final String KEY_PURCHASE_UserID = "userId";
    static final String KEY_CONFIRMATION_CODE = "confirmationCode";
    static final String KEY_VALUE = "value";
    static final String KEY_DATE = "date";

    //--- Table name ---
    private static final String DATABASE_PURCHASE_TABLE = "purchase";

    private final Context purContext;
    private DatabaseHelper purDBHelper;
    private SQLiteDatabase purDB;

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

    public PurchaseDBAdapter (Context ctx){
        this.purContext = ctx;
    }

    public PurchaseDBAdapter open() throws SQLException{
        this.purDBHelper = new DatabaseHelper(this.purContext);
        this.purDB = this.purDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.purDBHelper.close();
    }

    //--- insert purchase into database ---
    public long createPurchase(int productId, int shopId, int userId, String code, int value, String date){
        ContentValues values = new ContentValues();
        values.put(KEY_PURCHASE_ProductID, productId);
        values.put(KEY_PURCHASE_ShopID, shopId);
        values.put(KEY_PURCHASE_UserID, userId);
        values.put(KEY_CONFIRMATION_CODE, code);
        values.put(KEY_VALUE, value);
        values.put(KEY_DATE, date);
        return this.purDB.insert(DATABASE_PURCHASE_TABLE, null, values);
    }
}
