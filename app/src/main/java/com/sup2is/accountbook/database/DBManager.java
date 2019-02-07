package com.sup2is.accountbook.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sup2is.accountbook.model.Account;
import com.sup2is.accountbook.model.DateBundle;

import java.util.ArrayList;

public class DBManager {

    private final DBHelper dbHelper;

    private final SQLiteDatabase db;


    public DBManager(Context context, int version) {
        this(context,null,version);
    }

    public DBManager(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        this.dbHelper = new DBHelper(context,DBHelper.DB_NAME,factory,version);
        this.db = dbHelper.getWritableDatabase();
    }

    public void insertItem(Account item) {
        String sql = "INSERT INTO " + DBHelper.TBL_NAME + " VALUES ("
                + item.getDateBundle().getYear() + ","
                + item.getDateBundle().getMonth() + ","
                + item.getDateBundle().getDay() + ","
                + item.getDateBundle().getDayOfWeek() + ","
                + item.getDateBundle().getHour() + ","
                + item.getDateBundle().getMinute() + ","
                + item.getDateBundle().getSeconds() + ","
                + item.getMoney() + ","
                + item.getMethod() + ","
                + item.getSpending()+ ","
                + item.getContent()+ ",";
        db.execSQL(sql);
    }

    public ArrayList<Account> selectByDate(DateBundle bundle) {

        String sql = "SELECT * FROM " + DBHelper.TBL_NAME + " WHERE "
                + "year =" + bundle.getYear()
                + "month =" + bundle.getYear()
                + "day =" + bundle.getYear()
                + "ORDER BY day DESC";

        Cursor results = db.rawQuery(sql,null);
        results.moveToFirst();
        
        ArrayList<Account> accounts = new ArrayList<>();
        DateBundle temp;

        while (!results.isAfterLast()) {
            temp = new DateBundle(
                    results.getString(results.getColumnIndex("year")),
                    results.getString(results.getColumnIndex("month")),
                    results.getString(results.getColumnIndex("day")),
                    results.getString(results.getColumnIndex("day_of_week")),
                    results.getString(results.getColumnIndex("hour")),
                    results.getString(results.getColumnIndex("minute")),
                    results.getString(results.getColumnIndex("seconds"))
                    );

            accounts.add(new Account(temp,
                    results.getString(results.getColumnIndex("money")),
                    results.getString(results.getColumnIndex("method")),
                    results.getString(results.getColumnIndex("class")),
                    results.getString(results.getColumnIndex("spending")),
                    results.getString(results.getColumnIndex("content"))));
        }
        results.close();
        return accounts;
    }

}