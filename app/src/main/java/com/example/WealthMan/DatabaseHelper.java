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
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS translog(ID INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, symbol TEXT, price REAL, shares REAL, date TEXT , sample BOOLEAN)");

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

        String whereClause = "ID = ?";        contentValues.put("shares",s_qty);
        contentValues.put("price", s_price);
        contentValues.put("date", date);
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
    public void removeSample(Integer userid){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = {Integer.toString(userid) } ;
            db.delete("translog", "sample=1 and userid =?", whereArgs);
    }
    public void addSampleData(Integer userid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Transaction> temp = new ArrayList<>();
        Transaction newTx = null;
        newTx = new Transaction("WMT"  , 750.0   , 68.1924  , "2014-04-09"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 480.0   , 23.4725  , "2014-04-10"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -70.0   , 163.731  , "2014-04-16"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 370.0   , 14.9523  , "2014-04-21"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 660.0   , 67.8688  , "2014-04-21"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 820.0   , 42.4979  , "2014-04-21"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 780.0   , 35.4351  , "2014-04-23"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 440.0   , 13.6978  , "2014-05-09"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 140.0   , 304.91   , "2014-05-22"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 780.0   , 40.9094  , "2014-06-04"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 160.0   , 23.5103  , "2014-06-06"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 890.0   , 78.6945  , "2014-06-17"); temp.add(newTx);
        newTx = new Transaction("COF"  ,  70.0   , 76.1705  , "2014-06-20"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 690.0   , 45.8007  , "2014-06-30"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 920.0   , 358.66   , "2014-07-18"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 1010.0  , 67.5586  , "2014-07-21"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 360.0   , 86.4289  , "2014-07-21"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -50.0   , 162.7474 , "2014-07-22"); temp.add(newTx);
        newTx = new Transaction("COF"  , 910.0   , 74.8558  , "2014-07-23"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 820.0   , 66.6258  , "2014-07-28"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 530.0   , 37.7661  , "2014-08-01"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 460.0   , 307.06   , "2014-08-01"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 760.0   , 316.8    , "2014-08-08"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 280.0   , 43.6816  , "2014-08-12"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 110.0   , 334.53   , "2014-08-18"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 150.0   , 14.8875  , "2014-08-28"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 890.0   , 45.2347  , "2014-09-03"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 990.0   , 79.2511  , "2014-09-05"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 390.0   , 93.3875  , "2014-09-10"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 120.0   , 38.8111  , "2014-09-18"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 550.0   , 21.0221  , "2014-09-25"); temp.add(newTx);
        newTx = new Transaction("COF"  , 730.0   , 74.9954  , "2014-09-29"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 500.0   , 92.3704  , "2014-10-02"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 870.0   , 45.6559  , "2014-10-06"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 230.0   , 68.9692  , "2014-10-09"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 200.0   , 15.2972  , "2014-10-13"); temp.add(newTx);
        newTx = new Transaction("FB"   , 440.0   , 80.04    , "2014-10-23"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 250.0   , 295.59   , "2014-10-28"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 610.0   , 36.2141  , "2014-10-28"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 510.0   , 19.9881  , "2014-10-31"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 690.0   , 70.055   , "2014-11-11"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 700.0   , 19.9122  , "2014-11-14"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -40.0   , 42.3451  , "2014-11-19"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 370.0   , 73.3363  , "2014-11-26"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 860.0   , 39.4091  , "2014-12-03"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -30.0   , 46.3203  , "2014-12-03"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 730.0   , 47.9276  , "2014-12-10"); temp.add(newTx);
        newTx = new Transaction("WFC"  ,  20.0   , 47.433   , "2014-12-12"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 620.0   , 129.0261 , "2014-12-17"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 440.0   , 43.1081  , "2014-12-18"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 220.0   , 104.4903 , "2014-12-30"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 570.0   , 41.6884  , "2015-01-14"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 800.0   , 38.5088  , "2015-02-05"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -40.0   , 45.7888  , "2015-02-11"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 320.0   , 22.8707  , "2015-02-19"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 500.0   , 41.1321  , "2015-02-23"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 810.0   , 137.5767 , "2015-02-26"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 830.0   , 382.72   , "2015-03-04"); temp.add(newTx);
        newTx = new Transaction("FB"   , 400.0   , 82.92    , "2015-03-25"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 140.0   , 381.2    , "2015-04-08"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 760.0   , 138.4148 , "2015-04-08"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 820.0   , 117.6287 , "2015-04-16"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 130.0   , 23.1286  , "2015-04-21"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 960.0   , 75.571   , "2015-04-22"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 470.0   , 14.9488  , "2015-04-30"); temp.add(newTx);
        newTx = new Transaction("COF"  , 800.0   , 74.6681  , "2015-04-30"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 290.0   , 70.4343  , "2015-05-01"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -90.0   , 47.1939  , "2015-05-04"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -40.0   , 45.3572  , "2015-05-06"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 990.0   , 433.69   , "2015-05-08"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 220.0   , 70.8157  , "2015-05-08"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 260.0   , 121.8835 , "2015-05-18"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -30.0   , 26.0745  , "2015-06-04"); temp.add(newTx);
        newTx = new Transaction("ORCL" ,  80.0   , 41.2668  , "2015-06-04"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 260.0   , 16.1796  , "2015-06-05"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -10.0   , 145.4337 , "2015-06-11"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 630.0   , 38.4493  , "2015-06-17"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 300.0   , 74.2169  , "2015-06-25"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -20.0   , 50.3428  , "2015-06-30"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 570.0   , 536.15   , "2015-07-31"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 640.0   , 537.01   , "2015-08-05"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 840.0   , 66.7415  , "2015-08-05"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 720.0   , 41.0902  , "2015-08-12"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -30.0   , 51.8427  , "2015-08-18"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 500.0   , 35.4495  , "2015-08-28"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 870.0   , 40.2642  , "2015-09-03"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 290.0   , 14.7775  , "2015-09-04"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 600.0   , 24.8737  , "2015-09-15"); temp.add(newTx);
        newTx = new Transaction("COF"  , 680.0   , 70.774   , "2015-09-17"); temp.add(newTx);
        newTx = new Transaction("COF"  , 760.0   , 70.774   , "2015-09-17"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 440.0   , 40.2457  , "2015-09-18"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 140.0   , 58.3523  , "2015-10-01"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 230.0   , 104.1814 , "2015-10-07"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 430.0   , 35.5049  , "2015-10-16"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -40.0   , 44.8977  , "2015-10-27"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 870.0   , 119.7768 , "2015-10-27"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 340.0   , 49.5296  , "2015-11-10"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 980.0   , 111.2102 , "2015-11-23"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 830.0   , 110.823  , "2015-12-01"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 800.0   , 657.91   , "2015-12-14"); temp.add(newTx);
        newTx = new Transaction("FB"   , 650.0   , 104.04   , "2015-12-18"); temp.add(newTx);
        newTx = new Transaction("VMW"  ,  30.0   , 46.6202  , "2015-12-21"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 140.0   , 34.6023  , "2015-12-21"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 860.0   , 14.393   , "2016-01-08"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 590.0   , 39.7843  , "2016-01-11"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 290.0   , 9.6432   , "2016-01-11"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 210.0   , 9.5798   , "2016-01-13"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 770.0   , 9.6161   , "2016-01-14"); temp.add(newTx);
        newTx = new Transaction("FB"   , 130.0   , 94.35    , "2016-01-20"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 190.0   , 8.8736   , "2016-01-22"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 540.0   , 95.7872  , "2016-01-22"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 570.0   , 37.3494  , "2016-02-03"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 530.0   , 36.727   , "2016-02-12"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 100.0   , 117.9005 , "2016-02-19"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 780.0   , 575.14   , "2016-03-04"); temp.add(newTx);
        newTx = new Transaction("FB"   , 140.0   , 111.45   , "2016-03-18"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 320.0   , 100.5706 , "2016-03-21"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 350.0   , 131.2073 , "2016-03-22"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 270.0   , 11.0058  , "2016-03-23"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 520.0   , 24.7162  , "2016-03-28"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 980.0   , 24.7745  , "2016-03-31"); temp.add(newTx);
        newTx = new Transaction("FB"   , 330.0   , 113.71   , "2016-04-06"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 600.0   , 24.1723  , "2016-04-11"); temp.add(newTx);
        newTx = new Transaction("COF"  , 410.0   , 66.8237  , "2016-04-13"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 910.0   , 13.4402  , "2016-04-14"); temp.add(newTx);
        newTx = new Transaction("AAPL" , 300.0   , 100.6275 , "2016-04-21"); temp.add(newTx);
        newTx = new Transaction("FB"   , 430.0   , 108.76   , "2016-04-26"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 650.0   , 11.6559  , "2016-04-26"); temp.add(newTx);
        newTx = new Transaction("FB"   , 190.0   , 119.49   , "2016-05-06"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 730.0   , 47.2933  , "2016-05-06"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 830.0   , 10.7036  , "2016-05-11"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 270.0   , 62.111   , "2016-05-12"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 990.0   , 37.795   , "2016-05-18"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 640.0   , 37.1918  , "2016-05-19"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 360.0   , 12.3975  , "2016-06-02"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 730.0   , 12.6467  , "2016-06-14"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 850.0   , 50.6576  , "2016-06-21"); temp.add(newTx);
        newTx = new Transaction("WFC"  ,  30.0   , 43.6004  , "2016-06-30"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 410.0   , 27.6103  , "2016-07-06"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 190.0   , 136.4986 , "2016-07-07"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 280.0   , 745.81   , "2016-07-08"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 730.0   , 44.5401  , "2016-07-12"); temp.add(newTx);
        newTx = new Transaction("AAPL" ,  80.0   , 94.3807  , "2016-07-14"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 470.0   , 44.5585  , "2016-07-19"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 200.0   , 23.4688  , "2016-07-25"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 410.0   , 53.6444  , "2016-07-26"); temp.add(newTx);
        newTx = new Transaction("FB"   , 280.0   , 121.22   , "2016-07-26"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 710.0   , 12.8832  , "2016-07-28"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -10.0   , 143.6366 , "2016-08-02"); temp.add(newTx);
        newTx = new Transaction("VMW"  ,  90.0   , 59.4217  , "2016-08-09"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 900.0   , 44.7399  , "2016-08-10"); temp.add(newTx);
        newTx = new Transaction("BAC"  , 740.0   , 14.4684  , "2016-08-16"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 440.0   , 35.8201  , "2016-08-18"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -90.0   , 144.5885 , "2016-08-23"); temp.add(newTx);
        newTx = new Transaction("COF"  , 540.0   , 66.4595  , "2016-08-29"); temp.add(newTx);
        newTx = new Transaction("ORCL" ,  50.0   , 39.703   , "2016-08-30"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 430.0   , 35.4587  , "2016-08-31"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 320.0   , 769.16   , "2016-08-31"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 920.0   , 42.1862  , "2016-09-16"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 180.0   , 14.07    , "2016-09-23"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 490.0   , 23.4436  , "2016-09-28"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 820.0   , 841.66   , "2016-10-06"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 980.0   , 841.66   , "2016-10-06"); temp.add(newTx);
        newTx = new Transaction("FB"   , -50.0   , 128.88   , "2016-10-11"); temp.add(newTx);
        newTx = new Transaction("COF"  , 230.0   , 68.7788  , "2016-10-12"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 410.0   , 64.4445  , "2016-10-19"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 560.0   , 13.1469  , "2016-10-20"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 360.0   , 12.8672  , "2016-10-21"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 950.0   , 66.433   , "2016-11-17"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 150.0   , 14.7973  , "2016-11-18"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 430.0   , 66.2918  , "2016-11-25"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 350.0   , 57.9529  , "2016-11-25"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 470.0   , 57.6944  , "2016-11-30"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 850.0   , 59.5231  , "2016-12-12"); temp.add(newTx);
        newTx = new Transaction("VMW"  ,  90.0   , 67.2222  , "2016-12-14"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -10.0   , 52.388   , "2016-12-27"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 890.0   , 65.7186  , "2016-12-29"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 720.0   , 37.8045  , "2017-01-09"); temp.add(newTx);
        newTx = new Transaction("COF"  , -90.0   , 85.3973  , "2017-01-10"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -10.0   , 47.1038  , "2017-01-12"); temp.add(newTx);
        newTx = new Transaction("MSFT" , 170.0   , 60.0305  , "2017-01-13"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -70.0   , 21.7743  , "2017-01-20"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -30.0   , 162.3146 , "2017-01-25"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 220.0   , 69.9636  , "2017-01-26"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -30.0   , 161.4133 , "2017-01-27"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -110.0  , 52.5097  , "2017-01-30"); temp.add(newTx);
        newTx = new Transaction("ORCL" , 700.0   , 38.589   , "2017-02-01"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -50.0   , 22.1975  , "2017-02-10"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -90.0   , 27.6233  , "2017-02-16"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 620.0   , 65.358   , "2017-02-17"); temp.add(newTx);
        newTx = new Transaction("MSFT" ,  70.0   , 62.2439  , "2017-02-24"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 380.0   , 16.2862  , "2017-03-02"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 750.0   , 852.46   , "2017-03-10"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -10.0   , 27.6025  , "2017-03-13"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -110.0  , 24.426   , "2017-03-14"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -100.0  , 136.4215 , "2017-03-16"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , 320.0   , 16.6392  , "2017-03-17"); temp.add(newTx);
        newTx = new Transaction("AMZN" , 580.0   , 856.97   , "2017-03-20"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -20.0   , 160.3773 , "2017-03-22"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -60.0   , 27.336   , "2017-03-30"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -50.0   , 41.7516  , "2017-03-30"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -70.0   , 27.336   , "2017-03-30"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -10.0   , 156.5234 , "2017-04-11"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 470.0   , 69.6918  , "2017-04-11"); temp.add(newTx);
        newTx = new Transaction("FB"   , -40.0   , 139.39   , "2017-04-13"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -110.0  , 17.4161  , "2017-04-19"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 940.0   , 70.9921  , "2017-04-20"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -60.0   , 27.8985  , "2017-04-24"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -20.0   , 43.4732  , "2017-04-26"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 40.0    , 71.5995  , "2017-04-27"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -40.0   , 51.3391  , "2017-05-01"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -20.0   , 66.7518  , "2017-05-02"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 460.0   , 139.8455 , "2017-05-17"); temp.add(newTx);
        newTx = new Transaction("AMZN" , -80.0   , 970.67   , "2017-05-22"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -60.0   , 43.3468  , "2017-05-23"); temp.add(newTx);
        newTx = new Transaction("COF"  , 160.0   , 77.0362  , "2017-05-25"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -100.0  , 21.5156  , "2017-06-06"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -100.0  , 28.3307  , "2017-06-09"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -40.0   , 50.75    , "2017-06-20"); temp.add(newTx);
        newTx = new Transaction("WMT"  , 510.0   , 72.843   , "2017-06-21"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -50.0   , 141.7945 , "2017-06-22"); temp.add(newTx);
        newTx = new Transaction("VMW"  , 230.0   , 74.1421  , "2017-06-27"); temp.add(newTx);
        newTx = new Transaction("FB"   , -90.0   , 150.34   , "2017-07-05"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -110.0  , 24.032   , "2017-07-07"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -50.0   , 52.9145  , "2017-07-07"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -60.0   , 28.806   , "2017-07-13"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -30.0   , 52.0221  , "2017-07-18"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -10.0   , 146.3805 , "2017-07-20"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 730.0   , 135.4536 , "2017-07-25"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -110.0  , 23.4319  , "2017-07-26"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -60.0   , 47.9433  , "2017-08-08"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -20.0   , 18.0434  , "2017-08-10"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -100.0  , 156.2513 , "2017-08-14"); temp.add(newTx);
        newTx = new Transaction("COF"  , -80.0   , 81.5929  , "2017-08-16"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -30.0   , 70.8229  , "2017-08-23"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -110.0  , 70.7936  , "2017-08-24"); temp.add(newTx);
        newTx = new Transaction("COF"  , 970.0   , 77.4947  , "2017-08-31"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -20.0   , 72.011   , "2017-09-01"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -10.0   , 27.3312  , "2017-09-06"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -50.0   , 71.4851  , "2017-09-06"); temp.add(newTx);
        newTx = new Transaction("FB"   , -110.0  , 173.51   , "2017-09-11"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -20.0   , 47.5918  , "2017-09-15"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -70.0   , 91.363   , "2017-09-18"); temp.add(newTx);
        newTx = new Transaction("FB"   , -20.0   , 170.01   , "2017-09-18"); temp.add(newTx);
        newTx = new Transaction("AMZN" , -40.0   , 950.87   , "2017-09-27"); temp.add(newTx);
        newTx = new Transaction("AMZN" , -20.0   , 956.4    , "2017-09-28"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -80.0   , 46.805   , "2017-09-29"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -20.0   , 72.7415  , "2017-10-04"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -90.0   , 73.9881  , "2017-10-05"); temp.add(newTx);
        newTx = new Transaction("COF"  , -20.0   , 84.7273  , "2017-10-06"); temp.add(newTx);
        newTx = new Transaction("AMZN" , -10.0   , 990.99   , "2017-10-09"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -40.0   , 51.3386  , "2017-10-13"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -110.0  , 25.6019  , "2017-10-23"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -70.0   , 142.1397 , "2017-11-09"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -70.0   , 102.395  , "2017-11-17"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -20.0   , 21.5904  , "2017-11-21"); temp.add(newTx);
        newTx = new Transaction("FB"   , -30.0   , 183.03   , "2017-11-27"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -60.0   , 27.4525  , "2017-12-01"); temp.add(newTx);
        newTx = new Transaction("COF"  , -20.0   , 92.79    , "2017-12-04"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -40.0   , 47.0443  , "2017-12-05"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -60.0   , 20.1746  , "2017-12-13"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -80.0   , 171.2239 , "2017-12-19"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -100.0  , 83.7072  , "2017-12-20"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -10.0   , 95.2738  , "2017-12-21"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -10.0   , 46.4463  , "2017-12-27"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -20.0   , 46.7698  , "2018-01-03"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -100.0  , 52.0042  , "2018-01-05"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -60.0   , 107.3462 , "2018-01-10"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -10.0   , 113.0117 , "2018-01-19"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -70.0   , 49.9043  , "2018-01-22"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -50.0   , 113.8673 , "2018-01-24"); temp.add(newTx);
        newTx = new Transaction("AMZN" , -90.0   , 1357.51  , "2018-01-24"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -50.0   , 163.7977 , "2018-01-30"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -10.0   , 63.3386  , "2018-01-31"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -60.0   , 144.2487 , "2018-02-05"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -70.0   , 144.2487 , "2018-02-05"); temp.add(newTx);
        newTx = new Transaction("FB"   , -80.0   , 176.11   , "2018-02-09"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -10.0   , 51.0452  , "2018-02-16"); temp.add(newTx);
        newTx = new Transaction("COF"  , -30.0   , 96.1523  , "2018-03-05"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -30.0   , 54.222   , "2018-03-08"); temp.add(newTx);
        newTx = new Transaction("JNPR" , 600.0   , 23.9719  , "2018-03-23"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -70.0   , 104.6961 , "2018-03-26"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -80.0   , 21.3347  , "2018-03-29"); temp.add(newTx);
        newTx = new Transaction("FB"   , -80.0   , 156.11   , "2018-04-03"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -90.0   , 29.0167  , "2018-04-03"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -10.0   , 170.6514 , "2018-04-10"); temp.add(newTx);
        newTx = new Transaction("AAL"  , -100.0  , 46.0872  , "2018-04-12"); temp.add(newTx);
        newTx = new Transaction("COF"  , -110.0  , 94.6693  , "2018-04-13"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -60.0   , 85.5891  , "2018-04-18"); temp.add(newTx);
        newTx = new Transaction("COF"  , -60.0   , 96.2309  , "2018-04-20"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -90.0   , 112.1644 , "2018-04-23"); temp.add(newTx);
        newTx = new Transaction("COF"  , -40.0   , 91.1041  , "2018-04-25"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 260.0   , 139.3754 , "2018-04-25"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -40.0   , 21.9771  , "2018-05-09"); temp.add(newTx);
        newTx = new Transaction("COF"  , -80.0   , 95.2276  , "2018-05-21"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -70.0   , 97.0972  , "2018-05-24"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -80.0   , 51.675   , "2018-05-29"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -50.0   , 81.1812  , "2018-05-31"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -80.0   , 47.1859  , "2018-06-06"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -80.0   , 29.5488  , "2018-06-08"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -30.0   , 29.598   , "2018-06-11"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -50.0   , 100.1688 , "2018-06-14"); temp.add(newTx);
        newTx = new Transaction("WFC"  , -90.0   , 53.7537  , "2018-06-19"); temp.add(newTx);
        newTx = new Transaction("COF"  , -110.0  , 91.0749  , "2018-06-27"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 80.0    , 37.8924  , "2018-06-28"); temp.add(newTx);
        newTx = new Transaction("FB"   , -60.0   , 197.36   , "2018-07-02"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -80.0   , 131.2959 , "2018-07-05"); temp.add(newTx);
        newTx = new Transaction("IBM"  , -60.0   , 141.415  , "2018-07-12"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -110.0  , 103.112  , "2018-07-19"); temp.add(newTx);
        newTx = new Transaction("VMW"  , -90.0   , 125.3895 , "2018-07-23"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -60.0   , 190.8382 , "2018-07-24"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -70.0   , 87.4168  , "2018-07-30"); temp.add(newTx);
        newTx = new Transaction("FB"   , -50.0   , 176.37   , "2018-08-02"); temp.add(newTx);
        newTx = new Transaction("COF"  , -20.0   , 97.9845  , "2018-08-16"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -10.0   , 96.7981  , "2018-08-17"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -90.0   , 23.9752  , "2018-08-21"); temp.add(newTx);
        newTx = new Transaction("FB"   , -40.0   , 176.26   , "2018-08-28"); temp.add(newTx);
        newTx = new Transaction("JNPR" , -10.0   , 27.4604  , "2018-09-10"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -30.0   , 29.8202  , "2018-09-13"); temp.add(newTx);
        newTx = new Transaction("AMZN" , -50.0   , 1908.03  , "2018-09-17"); temp.add(newTx);
        newTx = new Transaction("FB"   , -20.0   , 162.44   , "2018-10-01"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -40.0   , 49.0773  , "2018-10-04"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 790.0   , 139.2728 , "2018-10-10"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -30.0   , 46.9342  , "2018-10-15"); temp.add(newTx);
        newTx = new Transaction("FB"   , -20.0   , 153.52   , "2018-10-15"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -10.0   , 28.2273  , "2018-10-16"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -30.0   , 219.4795 , "2018-10-17"); temp.add(newTx);
        newTx = new Transaction("FB"   , -30.0   , 159.42   , "2018-10-17"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -40.0   , 210.5987 , "2018-10-29"); temp.add(newTx);
        newTx = new Transaction("COF"  , -20.0   , 85.6424  , "2018-10-29"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -60.0   , 26.4959  , "2018-10-30"); temp.add(newTx);
        newTx = new Transaction("IBM"  , 890.0   , 121.959  , "2018-11-08"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -50.0   , 93.1577  , "2018-11-21"); temp.add(newTx);
        newTx = new Transaction("COF"  , -10.0   , 87.5248  , "2018-11-26"); temp.add(newTx);
        newTx = new Transaction("ORCL" , -80.0   , 47.7111  , "2018-11-29"); temp.add(newTx);
        newTx = new Transaction("FB"   , -70.0   , 141.09   , "2018-12-03"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -10.0   , 156.1602 , "2018-12-20"); temp.add(newTx);
        newTx = new Transaction("COF"  , 40.0    , 72.0236  , "2018-12-21"); temp.add(newTx);
        newTx = new Transaction("DELL" , 970.0   , 44.6     , "2018-12-27"); temp.add(newTx);
        newTx = new Transaction("DELL" , 30.0    , 44.6     , "2018-12-27"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -30.0   , 20.5457  , "2019-01-04"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -90.0   , 20.7737  , "2019-01-07"); temp.add(newTx);
        newTx = new Transaction("DELL" , 730.0   , 45.19    , "2019-01-10"); temp.add(newTx);
        newTx = new Transaction("DELL" , 590.0   , 44.47    , "2019-01-11"); temp.add(newTx);
        newTx = new Transaction("WFC"  , 920.0   , 47.985   , "2019-01-14"); temp.add(newTx);
        newTx = new Transaction("DELL" , 860.0   , 42.8     , "2019-01-14"); temp.add(newTx);
        newTx = new Transaction("DELL" , 280.0   , 42.8     , "2019-01-14"); temp.add(newTx);
        newTx = new Transaction("BAC"  , -90.0   , 26.236   , "2019-01-14"); temp.add(newTx);
        newTx = new Transaction("AAPL" , -100.0  , 152.4162 , "2019-01-15"); temp.add(newTx);
        newTx = new Transaction("DELL" , 150.0   , 42.62    , "2019-01-16"); temp.add(newTx);
        newTx = new Transaction("DELL" , 800.0   , 44.06    , "2019-01-22"); temp.add(newTx);
        newTx = new Transaction("DELL" , 380.0   , 45.91    , "2019-01-25"); temp.add(newTx);
        newTx = new Transaction("HPQ"  , -60.0   , 21.7951  , "2019-01-28"); temp.add(newTx);
        newTx = new Transaction("DELL" , 540.0   , 48.59    , "2019-01-31"); temp.add(newTx);
        newTx = new Transaction("FB"   , -70.0   , 165.71   , "2019-02-01"); temp.add(newTx);
        newTx = new Transaction("DELL" , 190.0   , 52.02    , "2019-02-11"); temp.add(newTx);
        newTx = new Transaction("DELL" , -30.0   , 53.95    , "2019-02-13"); temp.add(newTx);
        newTx = new Transaction("MSFT" , -20.0   , 106.3557 , "2019-02-13"); temp.add(newTx);
        newTx = new Transaction("DELL" , -110.0  , 55.0     , "2019-02-15"); temp.add(newTx);
        newTx = new Transaction("DELL" , -110.0  , 55.54    , "2019-02-20"); temp.add(newTx);
        newTx = new Transaction("DELL" , -90.0   , 56.73    , "2019-02-26"); temp.add(newTx);
        newTx = new Transaction("DELL" , -20.0   , 56.25    , "2019-02-27"); temp.add(newTx);
        newTx = new Transaction("DELL" , -70.0   , 55.34    , "2019-03-04"); temp.add(newTx);
        newTx = new Transaction("DELL" , -60.0   , 55.34    , "2019-03-04"); temp.add(newTx);
        newTx = new Transaction("DELL" , -10.0   , 53.73    , "2019-03-06"); temp.add(newTx);
        newTx = new Transaction("DELL" , -60.0   , 53.13    , "2019-03-07"); temp.add(newTx);
        newTx = new Transaction("DELL" , -30.0   , 52.35    , "2019-03-08"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -60.0   , 98.64    , "2019-03-20"); temp.add(newTx);
        newTx = new Transaction("DELL" , -50.0   , 60.71    , "2019-03-21"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -10.0   , 98.17    , "2019-03-25"); temp.add(newTx);
        newTx = new Transaction("AAL"  , 860.0   , 30.21    , "2019-03-25"); temp.add(newTx);
        newTx = new Transaction("WMT"  , -70.0   , 98.32    , "2019-03-26"); temp.add(newTx);
        newTx = new Transaction("DELL" , -50.0   , 58.89    , "2019-03-26"); temp.add(newTx);
        newTx = new Transaction("DELL" , -70.0   , 61.27    , "2019-04-02"); temp.add(newTx);
        newTx = new Transaction("DELL" , -20.0   , 62.2     , "2019-04-05"); temp.add(newTx);
//        Iterator<Transaction> iter = temp.iterator();
        db.beginTransaction();
        for(int i = 0; i < temp.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("userid", userid);
            contentValues.put("symbol", temp.get(i).getSymbol());
            contentValues.put("shares", temp.get(i).getShares());
            contentValues.put("price", temp.get(i).getPrice());
            contentValues.put("date", temp.get(i).getDate());
            contentValues.put("sample", true);
            db.insert("translog", null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}

