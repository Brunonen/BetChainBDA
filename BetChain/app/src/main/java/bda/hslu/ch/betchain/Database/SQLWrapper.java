package bda.hslu.ch.betchain.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.acl.LastOwnerException;

import bda.hslu.ch.betchain.LocalDBException;

/**
 * Created by ssj10 on 21/03/2018.
 */

public class SQLWrapper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "betChainApp";

    // Table Names
    private static final String TABLE_APP_USERS = "tbAppUsers";

    // App Users Table Columns
    private static final String APP_USERS_ID = "userID";
    private static final String APP_USERS_USERNAME = "username";
    private static final String APP_USERS_PWD = "pwd";
    private static final String APP_USERS_STAY_LOGGED_IN = "stayLoggedIn";
    private static final String APP_USERS_ADDRESS = "address";
    private static final String APP_USERS_P_KEY = "p_key";

    public SQLWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_APP_USERS = "CREATE TABLE IF NOT EXISTS '" + TABLE_APP_USERS +"' ("
                +"'"+ APP_USERS_ID +"' INTEGER PRIMARY KEY,"
                +"'"+ APP_USERS_USERNAME +"' varchar(45) NOT NULL UNIQUE,"
                +"'"+ APP_USERS_PWD +"' varchar(250) NOT NULL,"
                +"'"+ APP_USERS_STAY_LOGGED_IN +"' tinyint(4) NOT NULL DEFAULT '1',"
                +"'"+ APP_USERS_ADDRESS +"' varchar(250) NOT NULL,"
                +"'"+ APP_USERS_P_KEY +"' varchar(250)"
                +")";

        sqLiteDatabase.execSQL(CREATE_TABLE_APP_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addOrUpdateAppUser(String username, String pwd) throws LocalDBException{
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            if(!checkIfUserExists(username)) {
                ContentValues values = new ContentValues();
                values.put(APP_USERS_USERNAME, username);
                values.put(APP_USERS_PWD, pwd);
                values.put(APP_USERS_ADDRESS, "");
                values.put(APP_USERS_STAY_LOGGED_IN, 1);
                values.put(APP_USERS_P_KEY, "");
                int id = (int) db.insert(TABLE_APP_USERS, null, values);
                db.setTransactionSuccessful();
            }else{
                ContentValues values = new ContentValues();
                values.put(APP_USERS_PWD, pwd);
                values.put(APP_USERS_STAY_LOGGED_IN, 1);
                int id = db.update(TABLE_APP_USERS, values, APP_USERS_USERNAME + "='"+username+"'", null);

                logoutAllOtherUsers(username);
                db.setTransactionSuccessful();
            }
        }catch(SQLException e){
            throw new LocalDBException("Could not write to Database");
        }finally{
            db.endTransaction();
        }

    }

    private boolean checkIfUserExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_APP_USERS + " WHERE " + APP_USERS_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(sqlSelect, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private void logoutAllOtherUsers(String username) throws LocalDBException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(APP_USERS_STAY_LOGGED_IN, 0);
            db.update(TABLE_APP_USERS, values, APP_USERS_USERNAME + "!='" + username + "'", null);
            db.setTransactionSuccessful();
        }catch(SQLException e){
            throw new LocalDBException("Could not write to Database");
        }finally{
            db.endTransaction();
        }
    }

    public void logoutUser() throws LocalDBException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(APP_USERS_STAY_LOGGED_IN, 0);
            db.update(TABLE_APP_USERS, values, APP_USERS_STAY_LOGGED_IN + "='" + 1 + "'", null);
            db.setTransactionSuccessful();
        }catch(SQLException e){
            throw new LocalDBException("Could not write to Database");
        }finally{
            db.endTransaction();
        }
    }

    public String[] getLoggedInUserInfo(){
        String[] returnString = new String[4];
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_APP_USERS + " WHERE " + APP_USERS_STAY_LOGGED_IN + " = '1'";
        Cursor cursor = db.rawQuery(sqlSelect, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            returnString[0] = cursor.getString(cursor.getColumnIndex(APP_USERS_USERNAME));
            returnString[1] = cursor.getString(cursor.getColumnIndex(APP_USERS_PWD));
            returnString[2] = cursor.getString(cursor.getColumnIndex(APP_USERS_P_KEY));
            returnString[3] = cursor.getString(cursor.getColumnIndex(APP_USERS_ADDRESS));
         }
        cursor.close();

        return returnString;
    }

    public void changeUserPrivateKey(String username, String p_key) throws LocalDBException{
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(APP_USERS_P_KEY, p_key);
            int i = db.update(TABLE_APP_USERS, values, APP_USERS_USERNAME + "=='" + username + "'", null);

            if (i == 1) {
                db.setTransactionSuccessful();
            }
        }catch (SQLException e){
            throw new LocalDBException("Could not write to Database");
        }finally{
            db.endTransaction();
        }

    }

    public boolean checkIfUserNeedsToBeLoggedIn(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_APP_USERS + " WHERE " + APP_USERS_STAY_LOGGED_IN + " = '1'";
        Cursor cursor = db.rawQuery(sqlSelect, null);
        if(cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }



}
