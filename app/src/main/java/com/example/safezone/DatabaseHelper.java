package com.example.safezone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

     public static final String DATABASE_NAME = "safezone.db";
     public static final String TABLE_NAME = "Phone_number_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Phone_number";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME + "(ID INTEGER Primary Key AUTOINCREMENT , NAME TEXT , PHONE_NUMBER INTEGER  )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+ TABLE_NAME);
        onCreate(db);

    }
    public boolean insertdata(String name,String phonenumber)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,phonenumber);
       long success= db.insert(TABLE_NAME,null,contentValues);
       if(success==-1)
       {
           return false;
       }
       else
       {
           return  true;
       }

    }
    public Cursor getAllData()
    {
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cur= db.rawQuery("select * from "+TABLE_NAME,null);
        return  cur;

    }
}
