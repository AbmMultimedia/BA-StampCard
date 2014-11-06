package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Benedicte on 04-11-2014.
 */
public class DBAdapter {

    //---Logcat tag ---
    static final String TAG = "DBAdapter";

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/dk.ba.bastampcard/databases/";

    //--- Database name ---
    static final String DATABASE_NAME = "MyDB";

    //--- Database version ---
    static final int DATABASE_VERSION = 5;

    //--- Table Create Statements ---
    //--- Shop table create statement ---
       static final String CREATE_TABLE_SHOP = "create table shop(" +
            ShopDBAdapter.KEY_RowID + " integer primary key autoincrement, " +
            ShopDBAdapter.KEY_NAME + " text not null, " +
            ShopDBAdapter.KEY_ADDRESS + " text not null, " +
            ShopDBAdapter.KEY_POSTAL + " integer not null, " +
            ShopDBAdapter.KEY_CITY + " text not null);";

    //--- User table create statement ---
    static final String CREATE_TABLE_USER = "create table user(" +
            UserDBAdapter.KEY_UserID + " integer primary key autoincrement, " +
            UserDBAdapter.KEY_USERNAME + " text not null);";

    //--- Purchase table create statement ---
    static final String CREATE_TABLE_PURCHASE = "create table purchase(" +
            PurchaseDBAdapter.KEY_PurchaseID + " integer primary key autoincrement, " +
            PurchaseDBAdapter.KEY_PURCHASE_ProductID + " integer not null, " +
            PurchaseDBAdapter.KEY_PURCHASE_ShopID + " integer not null, " +
            PurchaseDBAdapter.KEY_PURCHASE_UserID + " integer not null, " +
            PurchaseDBAdapter.KEY_CONFIRMATION_CODE + " text not null, " +
            PurchaseDBAdapter.KEY_VALUE + " integer not null, " +
            PurchaseDBAdapter.KEY_DATE + " text not null);";

    //--- Product table create statement ---
    static final String CREATE_TABLE_PRODUCT = "create table product(" +
            ProductDBAdapter.KEY_ProductID + " integer primary key autoincrement, " +
            ProductDBAdapter.KEY_ProductName + " text not null);";

    //--- PriceListProduct table create statement ---
    static final String CREATE_TABLE_PriceListProduct = "create table priceListProduct(" +
            PriceListProductDBAdapter.KEY_PriceListProductID + " integer primary key autoincrement, " +
            PriceListProductDBAdapter.KEY_ProductID + " integer not null, " +
            PriceListProductDBAdapter.KEY_ShopID + " integer not null, " +
            PriceListProductDBAdapter.KEY_PRICE + " integer not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter (Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){ //Creates a new database if not exist
            /**
            try {
                db.execSQL(CREATE_TABLE_SHOP);
                db.execSQL(CREATE_TABLE_USER);
                db.execSQL(CREATE_TABLE_PURCHASE);
                db.execSQL(CREATE_TABLE_PRODUCT);
                db.execSQL(CREATE_TABLE_PriceListProduct);
                Log.d("db", "database created");
            } catch (SQLException e){
                Log.d(this.getClass().getName(), "Error: " + e.getMessage());
            }
             **/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS shop");
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS purchase");
            onCreate(db);
        }
    }

    //--- Open database ---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //--- Closes database ---
    public void close(){
        DBHelper.close();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
            Log.d(this.TAG, "Database exist");
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            DBHelper.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
            Log.d(this.TAG, "Database doest not exist" + e.getMessage());
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


//    public long insertShop(String name, String address, int postal, String city){
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_NAME, name);
//        initialValues.put(KEY_ADDRESS, address);
//        initialValues.put(KEY_POSTAL, postal);
//        initialValues.put(KEY_CITY, city);
//        return db.insert(DATABASE_SHOP_TABLE, null, initialValues);
//    }

//
//    //--- deletes a particular shop ---
//    public boolean deleteShop(long rowId){
//        return db.delete(DATABASE_SHOP_TABLE, KEY_RowID + "=" + rowId, null) > 0;
//    }
//
//
//    //--- updates a shop ---
//    public boolean updateShop(long rowId, String name, String address, int postal, String city){
//        ContentValues args = new ContentValues();
//        args.put(KEY_NAME, name);
//        args.put(KEY_ADDRESS, address);
//        args.put(KEY_POSTAL, postal);
//        args.put(KEY_CITY, city);
//        return db.update(DATABASE_SHOP_TABLE, args, KEY_RowID + "=" + rowId, null) > 0;
//    }
}
