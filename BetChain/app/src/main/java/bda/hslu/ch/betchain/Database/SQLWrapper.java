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
 * Created by Bruno Fischlin on 21/03/2018.
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
                +"'"+ APP_USERS_ADDRESS +"' varchar(250),"
                +"'"+ APP_USERS_P_KEY +"' varchar(250)"
                +")";

        sqLiteDatabase.execSQL(CREATE_TABLE_APP_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /***
     * This method is called when the user logins into the app. The user information will be stored in a local Database on the Device.
     * The Method either creates a user entry or updates the one of an existing user, so the app can log in a user automatically upon start.
     * @param username  : The username of the user
     * @param pwd       : The users hashed Password
     * @param address   : the ethereum address of the user.
     * @throws LocalDBException     : If something goes wrong on the DB an exception is thrown.
     */
    public void addOrUpdateAppUser(String username, String pwd, String address) throws LocalDBException{
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            if(!checkIfUserExists(username)) {
                ContentValues values = new ContentValues();
                values.put(APP_USERS_USERNAME, username);
                values.put(APP_USERS_PWD, pwd);
                values.put(APP_USERS_ADDRESS, address);
                values.put(APP_USERS_STAY_LOGGED_IN, 1);
                values.put(APP_USERS_P_KEY, "");
                int id = (int) db.insert(TABLE_APP_USERS, null, values);
                db.setTransactionSuccessful();
            }else{
                ContentValues values = new ContentValues();
                values.put(APP_USERS_PWD, pwd);
                values.put(APP_USERS_STAY_LOGGED_IN, 1);
                values.put(APP_USERS_ADDRESS, address);
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

    /***
     * Checks if a user entry already exists on the local database
     * @param username  : Username of the user
     * @return          : True if a user exists, false if not
     */
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

    /***
     * Sets the "stayLoggedIn" of all users who are not the submitted user in the local DB to FALSE.
     * @param username      : Name of the user which stays logged in
     * @throws LocalDBException
     */
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

    /***
     * Sets the Stay Logged In Flag in the local DB of the current user which is automatically logged in to false
     * @throws LocalDBException
     */
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

    /***
     * Returns the userinformation of the logged in User as an Array
     * @return      : Returns an Array with the information
     *              : [0] The Username
     *              : [1] The Users hashed password
     *              : [2] The Users saved private Key
     *              : [3] The users saved Ethereum address
     */
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

    /***
     * Updates the PRivate key of the submitted user in the local DB
     * @param username      ; The username of the user
     * @param p_key         : The  private key to be updated.
     * @throws LocalDBException : If something goes wrong a LocalDbException is thrown
     */
    public void changeUserPrivateKey(String username, String p_key) throws LocalDBException{
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(APP_USERS_P_KEY, p_key);
            int i = db.update(TABLE_APP_USERS, values, APP_USERS_USERNAME + "='" + username + "'", null);

            if (i == 1) {
                db.setTransactionSuccessful();
            }
        }catch (SQLException e){
            throw new LocalDBException("Could not write to Database");
        }finally{
            db.endTransaction();
        }

    }

    /***
     * Updates the Public key of the submitted user in the local DB
     * @param username          : The username of the user
     * @param address           : The  public key to be updated.
     * @throws LocalDBException : If something goes wrong a LocalDbException is thrown
     */
    public void changeUserPublicKey(String username, String address) throws LocalDBException{
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(APP_USERS_ADDRESS, address);
            int i = db.update(TABLE_APP_USERS, values, APP_USERS_USERNAME + "='" + username + "'", null);

            if (i == 1) {
                db.setTransactionSuccessful();
            }
        }catch (SQLException e){
            throw new LocalDBException("Could not write to Database");
        }finally{
            db.endTransaction();
        }

    }

    /***
     * Checks if the user needs to be automatically logged in
     * @return  : True if yes, False if no
     */
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
