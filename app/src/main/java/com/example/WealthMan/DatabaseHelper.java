package com.example.WealthMan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME ="register.db";
    public static final String TABLE_NAME ="registeruser";
    public static final String COL_1 ="ID";         // AUTOINCREMENT number.  not used yet.... KEY for transactions?
    public static final String COL_2 ="username";   // TEXT
    public static final String COL_3 ="password";   // WARNING!!! CLEARTEXT PASSWORD!!!
    public static final String COL_4 ="email";      // TEXT
    public static final String COL_5 ="pin";        // Stored as TEXT to preserve any leading Zeros

    public static final String SYM_TBL = "symbols";
    public static final String SYM_COL_1 = "id";
    public static final String SYM_COL_2 = "name";
    public static final String SYM_COL_3 = "symbol";

    public static final String WL_TBL = "watchlist";
    public static final String WL_COL_USER = "userid";
    public static final String WL_COL_SYMBOL = "symbol";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY  KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, pin TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS symbols (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, symbol TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS watchlist (userid TEXT, symbol TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public long createWatchlist(){
        String[] wlArray = {"fb","aal","bac","wfc","wmt","cof","amzn","vmw","ibm","dell","hpq","msft","jnpr","orcl","aapl"};
        SQLiteDatabase db = this.getReadableDatabase();
        long res = 0;
        for (int i = 0 ; i < wlArray.length; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(WL_COL_USER, 1);

            contentValues.put(WL_COL_SYMBOL, wlArray[i]);
            res = db.insert(WL_TBL,null,contentValues);
        }
        db.close();
        return res;
    }
    public long addWatch(int userid, String symbol){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WL_COL_USER, userid);
        contentValues.put(WL_COL_SYMBOL, symbol);
        long res = db.insert(WL_TBL,null,contentValues);
        db.close();
        return res;
    }
    public void remWatch(int userid, String symbol){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM watchlist WHERE symbol = "+"\'"+symbol+"\'"+" and userid = \"1\";");
        db.close();
    }
    public int getUserId(String Email){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_4 + "=?";
        String[] selectionArgs = {Email};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();//***!!!very important
        int count = cursor.getCount();
        int userid = 0;
        if(count>0){
            userid = cursor.getInt(cursor.getColumnIndex("ID"));
            cursor.close();
            db.close();
            }
        return  userid;
    }
    public String getWatchList(){
        String result;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT GROUP_CONCAT(symbol) as symbol from watchlist",null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("symbol"));
        db.close();
        if(result == null){
            result = "0";
        }
        return result;
    }

    public long addUser(String user, String password, String email, String pin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",user);
        contentValues.put("password",password);
        contentValues.put("email",email);
        contentValues.put("pin",pin);

        long res = db.insert("registeruser",null,contentValues);
        db.close();
        return  res;
    }
    public long addSymbol(String name, String sym){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("symbol", '"' + sym + '"');
//        contentValues.put("name", '"' + name + '"');
        contentValues.put("name", name);
        contentValues.put("symbol", sym);
        long res = db.insert(SYM_TBL,null, contentValues);
        db.close();
        return res;
    }
    public boolean checkUser(String email, String password){
        System.out.println("checkuser:::"+email);
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_4 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = { email, password };
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0)
            return  true;
        else
            return  false;
    }
    public String checkpin(String Email, String Pin){
        String[] columns = { COL_3 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_4 + "=?" + " and " + COL_5 + "=?";
        String[] selectionArgs = {Email,Pin};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();//***!!!very important
        int count = cursor.getCount();
        if(count>0){
            Log.d("ForgotActivity","If onCreate execute");
            String password = "Your password is "+cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            db.close();
            return  password;}
        else {
            Log.d("ForgotActivity", "else onCreate execute");
            String warn = "Email or PIN code is wrong";
            cursor.close();
            db.close();
            return warn;
        }
    }

    public  int checkSymbol(int ID, String symbol){
        String[] column_id = {WL_COL_USER};
        SQLiteDatabase db = getReadableDatabase();
        String selection = WL_COL_USER + "=?" + " and " + WL_COL_SYMBOL + "=?";
        String[] selectionArgs = {Integer.toString(ID),symbol};
        Cursor cursor = db.query(WL_TBL,column_id,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public boolean checkUserExist(String email){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL_4 + " = " + email;
        String selection = COL_4 + "=?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0)
            return  true;
        else
            return  false;
    }
}
