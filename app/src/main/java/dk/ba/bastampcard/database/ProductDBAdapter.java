package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public static final String KEY_ProductID = "_id";
    public static final String KEY_ProductName = "productName";

    //--- Table name ---
    public static final String DATABASE_PRODUCT_TABLE = "product";

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

    //--- retrieves all products ---
    public Cursor getAllProducts(){
        return this.proDB.query(DATABASE_PRODUCT_TABLE, new String[] {KEY_ProductID, KEY_ProductName}, null, null, null, null, null);
    }

    public Cursor getProduct(int rowId) throws SQLException{
        Cursor mCursor = this.proDB.query(true, DATABASE_PRODUCT_TABLE, new String[]{KEY_ProductID, KEY_ProductName}, KEY_ProductID + "=" + rowId, null, null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return  mCursor;
    }
}
