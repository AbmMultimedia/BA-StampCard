package dk.ba.bastampcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Benedicte on 05-11-2014.
 */
public class UserDBAdapter {

    //--- Constants for various fields that are being created in the database ---
    //--- User table - column names ---
    static final String KEY_UserID = "userId";
    static final String KEY_USERNAME = "userName";

    //--- Table name ---
    private static final String DATABASE_USER_TABLE = "user";

    private final Context uContext;
    private DatabaseHelper uDBHelper;
    private SQLiteDatabase uDB;

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

    public UserDBAdapter (Context ctx){
        this.uContext = ctx;
    }

    public UserDBAdapter open() throws SQLException {
        this.uDBHelper = new DatabaseHelper(this.uContext);
        this.uDB = this.uDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.uDBHelper.close();
    }

    //--- insert user into database ---
    public long insertUser(String user){
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user);
        return this.uDB.insert(DATABASE_USER_TABLE, null, values);
    }
}