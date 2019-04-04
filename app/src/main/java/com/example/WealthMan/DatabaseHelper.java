package com.example.WealthMan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.example.WealthMan.detail.adapter.SharesListAdapter;
import com.example.WealthMan.detail.bean.SharesStockBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.valueOf;


public class DatabaseHelper extends SQLiteOpenHelper {
    // for our logs
    public static final String TAG = "DatabaseHandler.java";

    static final String DATABASE_NAME = "register.db";
    static final String TABLE_NAME = "registeruser";
    static final String COL_1 = "ID";         // AUTOINCREMENT number.  not used yet.... KEY for transactions?
    static final String COL_2 = "username";   // TEXT
    static final String COL_3 = "password";   // WARNING!!! CLEARTEXT PASSWORD!!!
    static final String COL_4 = "email";      // TEXT
    static final String COL_5 = "pin";        // Stored as TEXT to preserve any leading Zeros


    static final String SYM_TBL = "symbols";
    static final String SYM_COL_1 = "id";
    static final String SYM_COL_2 = "name";
    static final String SYM_COL_3 = "symbol";

    static final String WL_TBL = "watchlist";
    static final String WL_COL_USER = "userid";
    static final String WL_COL_SYMBOL = "symbol";


    public static final String SHARES_LIST_NAME = "shareslist";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    /*public DatabaseHelper(SharesListAdapter context) {
        super(context, DATABASE_NAME, null, 1);
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY  KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, pin TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS symbols (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, symbol TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS watchlist (userid TEXT, symbol TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS shareslist (ID INTEGER PRIMARY  KEY AUTOINCREMENT,stock TEXT, shares TEXT,price TEXT,date TEXT,bought Text)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS translog(ID INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, symbol TEXT, price REAL, shares REAL, date TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long createWatchlist() {
        String[] wlArray = {"FB", "AAL", "BAC", "WFC", "WMT", "COF", "AMZN", "VMW", "IBM", "DELL", "HPQ", "MSFT", "JNPR", "ORCL", "AAPL"};

        SQLiteDatabase db = this.getReadableDatabase();
        long res = 0;
        for (int i = 0; i < wlArray.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WL_COL_USER, 1);

            contentValues.put(WL_COL_SYMBOL, wlArray[i]);
            res = db.insert(WL_TBL, null, contentValues);
        }
        db.close();
        return res;
    }

    /**
     * insert date
     *
     * @param tableName -- need table name
     * @param params
     * @return
     */
    public long insertDateToTable(String tableName, ContentValues params) {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        long insert = readableDatabase.insert(tableName, null, params);
        //long temp = readableDatabase.execSQL("SELECT last_insert_rowid()");
        readableDatabase.close();
        //return temp;
        return insert;
    }
    public void DeleteTable(int ID) {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        Log.e("大都带上!!!!", "的撒记得");
        //long insert = readableDatabase.insert(tableName, null, params);
        readableDatabase.delete("shareslist", "ID = ?", new String[] { valueOf(ID) });
        //long temp = readableDatabase.execSQL("SELECT last_insert_rowid()");
        readableDatabase.close();
        //return temp;
        //return insert;
    }

    /**
     * query stock
     *
     * @param selectionArgs
     * @return
     */
    public boolean queryStockDate(String[] selectionArgs) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = "symbol=?";
        Cursor cursor = db.query(SYM_TBL, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count > 0)
            return true;
        else
            return false;
    }

    /**
     * query all SharesStockBean, return list
     *
     * @return List<SharesStockBean>
     */
    public List<SharesStockBean> queryAllSharesList(String StockID) {
        ArrayList<SharesStockBean> persons = new ArrayList<SharesStockBean>();
        Cursor c = queryTheCursor(StockID);
        while (c.moveToNext()) {
            SharesStockBean person = new SharesStockBean();
            person.stock = c.getString(c.getColumnIndex("stock"));
            person.shares = c.getString(c.getColumnIndex("shares"));
            person.price = c.getString(c.getColumnIndex("price"));

            person.date = c.getString(c.getColumnIndex("date")).substring(0,c.getString(c.getColumnIndex("date")).length()-6);
         //   person.date = c.getString(c.getColumnIndex("date"));
            person.buy_type = c.getString(c.getColumnIndex("bought"));
            person.ID = c.getInt((c.getColumnIndex("ID")));
            persons.add(person);
        }
        c.close();
        return persons;
    }
    public int querylastSharesList() {
       // ArrayList<SharesStockBean> persons = new ArrayList<SharesStockBean>();
        Cursor c =  queryTheCursorLast();

        c.moveToLast();
        int ID =c.getInt((c.getColumnIndex("ID")));
        Log.e("啊啊啊啊啊啊WHATever!!!!", valueOf(ID));
        c.close();
        return ID;
        /*while (c.moveToNext()) {
            SharesStockBean person = new SharesStockBean();
            person.stock = c.getString(c.getColumnIndex("stock"));
            person.shares = c.getString(c.getColumnIndex("shares"));
            person.price = c.getString(c.getColumnIndex("price"));

            person.date = c.getString(c.getColumnIndex("date")).substring(0,c.getString(c.getColumnIndex("date")).length()-6);
            //   person.date = c.getString(c.getColumnIndex("date"));
            person.buy_type = c.getString(c.getColumnIndex("bought"));
            person.ID = c.getInt((c.getColumnIndex("ID")));
            persons.add(person);
        }
        c.close();
        return persons;*/
    }
    public Cursor queryTheCursorLast() {
        SQLiteDatabase db = getReadableDatabase();
       // String ID =  "SELECT MAX(ID) FROM shareslist" ;
            /*String[] columns = {COL_1};
            SQLiteDatabase db = getReadableDatabase();
            String selection = COL_4 + "=?";
            String[] selectionArgs = {Email};
            Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            cursor.moveToFirst();//***!!!very important
            int count = cursor.getCount();*/
        Log.e("前面啊啊啊啊啊啊WHATever!!!!", "dsads!!!!");
        Cursor c = db.rawQuery("SELECT * FROM shareslist ORDER BY ID", null);
        return c;
        //Cursor c = db.rawQuery("SELECT * FROM shareslist", null);
        //return c;
    }




    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor(String stockID) {
        SQLiteDatabase db = getReadableDatabase();
        String ID = stockID;
            /*String[] columns = {COL_1};
            SQLiteDatabase db = getReadableDatabase();
            String selection = COL_4 + "=?";
            String[] selectionArgs = {Email};
            Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            cursor.moveToFirst();//***!!!very important
            int count = cursor.getCount();*/

        Cursor c = db.rawQuery("SELECT * FROM shareslist where stock= ? ", new String[]{ID});
        return c;
        //Cursor c = db.rawQuery("SELECT * FROM shareslist", null);
        //return c;
    }


    public long addWatch(int userid, String symbol) {
        //symbol = symbol.toLowerCase();
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WL_COL_USER, userid);
        contentValues.put(WL_COL_SYMBOL, symbol);
        long res = db.insert(WL_TBL, null, contentValues);
        db.close();
        return res;
    }

    public void remWatch(int userid, String symbol) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM watchlist WHERE symbol = " + "\'" + symbol + "\'" + " and userid = \"1\";");
        db.close();
    }

    public int getUserId(String Email) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_4 + "=?";
        String[] selectionArgs = {Email};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();//***!!!very important
        int count = cursor.getCount();
        int userid = 0;
        if (count > 0) {
            userid = cursor.getInt(cursor.getColumnIndex("ID"));
            cursor.close();
            db.close();
        }
        return userid;
    }

    public String getWatchList(Integer id) {
        String result;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selection = { Integer.toString(id) };
        Cursor cursor = db.rawQuery("SELECT GROUP_CONCAT(symbol) as symbol from watchlist where userid = ?", selection);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("symbol"));
        db.close();
        if (result == null) {
            result = "0";
        }
        return result;
    }

    public long addUser(String user, String password, String email, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("pin", pin);

        long res = db.insert("registeruser", null, contentValues);
        db.close();
        return res;
    }

    public long addSymbol(String name, String sym) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("symbol", '"' + sym + '"');
//        contentValues.put("name", '"' + name + '"');
        contentValues.put("name", name);
        contentValues.put("symbol", sym);
        long res = db.insert(SYM_TBL, null, contentValues);
        db.close();
        return res;
    }

    public boolean checkUser(String email, String password) {
        System.out.println("checkuser:::" + email);
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_4 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public String checkpin(String Email, String Pin) {
        String[] columns = {COL_3};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_4 + "=?" + " and " + COL_5 + "=?";
        String[] selectionArgs = {Email, Pin};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();//***!!!very important
        int count = cursor.getCount();
        if (count > 0) {
            Log.d("ForgotActivity", "If onCreate execute");
            String password = "Your password is " + cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            db.close();
            return password;
        } else {
            Log.d("ForgotActivity", "else onCreate execute");
            String warn = "Email or PIN code is wrong";
            cursor.close();
            db.close();
            return warn;
        }
    }

    public int checkSymbol(int ID, String symbol) {
        String[] column_id = {WL_COL_USER};
        SQLiteDatabase db = getReadableDatabase();
        String selection = WL_COL_USER + "=?" + " and " + WL_COL_SYMBOL + "=?";
        String[] selectionArgs = {Integer.toString(ID), symbol};
        Cursor cursor = db.query(WL_TBL, column_id, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public boolean checkUserExist(String email) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL_4 + " = " + email;
        String selection = COL_4 + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public void populateSymbols(List<Pair> SymbolList) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE " + SYM_TBL);   // Remove any existing Symbol Table & Names to remove old and not have conficts if exists
        db.execSQL("CREATE TABLE IF NOT EXISTS symbols (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, symbol TEXT)");
        db.beginTransaction();
        Iterator<Pair> symIterator = SymbolList.iterator();
        int count = 0;
        for (int i = 0; i < SymbolList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", (String) SymbolList.get(i).first);
            contentValues.put("symbol", (String) SymbolList.get(i).second);
            db.insert(SYM_TBL, null, contentValues);
            count++;
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        Log.d("DB", "Added " + count + " symbols to database");
        db.close();

    }

    public String searchNameSymbol(String searchKey) {
//        select symbol as t1 from symbols as a where symbol like 'ap%' union all select name as t1 from symbols as b where name like '%ap%' order by t1 COLLATE NOCASE ASC;

        return "not set";
    }

    // Read records related to the search term
    public NameSymbol[] read(String searchTerm) {
//        public static final String SYM_TBL = "symbols";
//        public static final String SYM_COL_1 = "id";
//        public static final String SYM_COL_2 = "name";
//        public static final String SYM_COL_3 = "symbol";

        // select query
        String sql = "";
        sql += "SELECT " + SYM_COL_3 + " as column_1, " + SYM_COL_3 + ", LENGTH(" + SYM_COL_3 + ") from  " + SYM_TBL + " AS table_a WHERE " + SYM_COL_3 + " like '" + searchTerm + "%' ";
        sql += "UNION ALL SELECT " + SYM_COL_2 + " as column_1," + SYM_COL_3 + ", LENGTH(" + SYM_COL_3 + ") from " + SYM_TBL + " AS table_b WHERE " + SYM_COL_2;
        sql += " like '%" + searchTerm + "%' ORDER BY LENGTH(" + SYM_COL_3 + ") COLLATE NOCASE ASC ";
//        sql += "SELECT * FROM " + tableName;
//        sql += " WHERE " + fieldObjectName + " LIKE '%" + searchTerm + "%'";
//        sql += " ORDER BY " + fieldObjectId + " DESC";
        sql += "LIMIT 0,50";

        SQLiteDatabase db = this.getReadableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        int recCount = cursor.getCount();

        NameSymbol[] ObjectItemData = new NameSymbol[recCount];
        int x = 0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//              Column 0 = Combined Names and Symbols, Column 1 = symbols
                String objectName = cursor.getString(0);
                String objectSymbol = cursor.getString(1);
//                String objectSymbol = "sym";
//                Log.e(TAG, "coName: " + objectName);
//                Log.e(TAG, "coSymbol: " + objectSymbol);

                NameSymbol nameSymbol = new NameSymbol(objectName, objectSymbol);

                ObjectItemData[x] = nameSymbol;

                x++;

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ObjectItemData;

    }
    public ArrayList<?> getShareData(Integer id, String sym){
// Select * from shareslist where userid = id and symbol = sym;
//        translog(ID INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, symbol TEXT, price REAL, shares REAL, date TEXT)"
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "translog";
        String[] selection = {"ID", "userid", "symbol", "price", "shares", "date"};
        String whereClause = "userid = ? AND symbol = ?";
        String[] selctionArgs = {id.toString(), sym};
        String orderBy = "date,ID";
        ArrayList<Transaction> tList = new ArrayList<>();
        Cursor c = db.query(table, selection, whereClause, selctionArgs, null, null,orderBy);
        if (c.moveToFirst()) {
            do {
//              Column 0 = Combined Names and Symbols, Column 1 = symbols
                Integer logId = c.getInt(c.getColumnIndex("ID"));
                String logSymbol = c.getString(c.getColumnIndex("symbol"));
                Double logShares = c.getDouble(c.getColumnIndex("shares"));
                Double logPrice = c.getDouble(c.getColumnIndex("price"));
                String logDate = c.getString(c.getColumnIndex("date"));
                Transaction temp = new Transaction(logId, logSymbol, logShares,logPrice,logDate);
                tList.add(temp);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tList;
    }
    public void enterTx(Integer userid, String symbol, Double qty, Double price, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("symbol",symbol);
        contentValues.put("shares", qty);
        contentValues.put("price", price);
        contentValues.put("date", date);
        db.insert("translog",null,contentValues);
        db.close();
    }

    public void uptateTx(int txID, int userid, String symbolName, Double s_qty, Double s_price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shares",s_qty);
        contentValues.put("price", s_price);
        contentValues.put("date", date);
        String whereClause = "ID = ?";
        String [] selectionArgs = { valueOf(txID) };
        db.update("translog", contentValues, whereClause, selectionArgs);
        db.close();
    }
    public void deleteTx(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = { id.toString()};
        db.delete("translog", "ID = ?",  whereArgs );
        db.close();
    }
}

