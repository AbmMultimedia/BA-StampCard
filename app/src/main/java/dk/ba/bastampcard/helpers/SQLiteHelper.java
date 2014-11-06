package dk.ba.bastampcard.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Anders on 03-11-2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    private static String CLASS_NAME;

    private static final String DATABASE_NAME = "StampCard.db";
    private static final int DATABASE_VERSION = 1;

    private static int numAsyncCalls = 0;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(CLASS_NAME, "Create database");
        CLASS_NAME = getClass().getName();
    }

    public void create() {
        Log.d(CLASS_NAME, "Create");
        open();
    }

    public SQLiteDatabase open() {
        Log.d(CLASS_NAME, "Open");
        //Create and/or open a database that will be used for reading and writing.
        return getWritableDatabase();
    }

    public void execAsyncSQL(SQLiteDatabase database, String sql){
        Log.d(CLASS_NAME, "execAsyncSQL");
        numAsyncCalls++;
        new AsyncDatabase(database).execute(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(CLASS_NAME, "onCreate");

        db.beginTransaction();
        try{
            String createTable = "create table if not exists users ("
                    + "_id integer primary key autoincrement, "
                    + "name text not null, "
                    + "userName text not null)";
            String createIndex = "create unique index if not exists "
                    + "pk_trips on trips(_id)";

            db.execSQL(createTable);
            db.execSQL(createIndex);

            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            Log.d(CLASS_NAME, "SQL error:" + e.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(CLASS_NAME, "onUpgrade");

        db.beginTransaction();
        try{
            String dropTable = "drop table if exists users";
            String dropIndex = "drop index if exists pk_routes";

            db.execSQL(dropIndex);
            db.execSQL(dropTable);

            db.setTransactionSuccessful();
        }catch (SQLiteException e) {
            Log.d(CLASS_NAME, "SLQ error: " + e.getMessage());
        }finally {
            db.endTransaction();
        }

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase database)
    {
        Log.d(CLASS_NAME, "onConfigure");
        execAsyncSQL(database, "pragma foreign_key=ON;");
    }

    private class AsyncDatabase extends AsyncTask<String, Void, Void>{
        private final SQLiteDatabase database;
        public AsyncDatabase(SQLiteDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(String[] params) {
            Log.d(CLASS_NAME, "doInBackground" );

            int paramsLength = params.length;

            if(paramsLength == 1 ) {
                database.execSQL(params[0]);
            } else {
                database.beginTransaction();
                try {
                    for (int i = 0; i < paramsLength; i++) {
                        database.execSQL(params[i]);
                    }
                    database.setTransactionSuccessful();
                }
                catch (SQLiteException e) {
                    Log.e(CLASS_NAME, e.getMessage());
                }
                finally {
                    database.endTransaction();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.d(CLASS_NAME, "onPostExecute");
            numAsyncCalls--;
            if(numAsyncCalls <= 0)
            {
                database.close();
            }
        }
    }


}
