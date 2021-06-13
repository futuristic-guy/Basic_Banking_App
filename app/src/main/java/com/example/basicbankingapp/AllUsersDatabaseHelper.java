package com.example.basicbankingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AllUsersDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Storage.db";
    private static final String TABLE_NAME1 = "userInfo_table";
    private static final String TABLE_NAME2 = "transacInfo_table";

    private static final String TABLE1_COL_1 = "ID";
    private static final String TABLE1_COL_2 = "NAME";
    private static final String TABLE1_COL_3 = "EMAIL";
    private static final String TABLE1_COL_4 = "GENDER";
    private static final String TABLE1_COL_5 = "CURRENT_BALANCE";

    private static final String TABLE2_COL_1 = "ID";
    private static final String TABLE2_COL_2 = "FROM_NAME";
    private static final String TABLE2_COL_3 = "TO_NAME";
    private static final String TABLE2_COL_4 = "AMOUNT";


    public AllUsersDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT,GENDER TEXT, CURRENT_BALANCE TEXT)");
         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FROM_NAME TEXT, TO_NAME TEXT, AMOUNT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
           onCreate(db);
    }


    public boolean insertToUserInfo(String name,String email,String gender, String currentBalance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COL_2,name);
        contentValues.put(TABLE1_COL_3,email);
        contentValues.put(TABLE1_COL_4,gender);
        contentValues.put(TABLE1_COL_5,currentBalance);


        long result = db.insert(TABLE_NAME1,null,contentValues);

        if(result == -1){
            return false;
        }else{
          return true;
        }

    }



    public boolean insertToTransacInfo(String fromName,String toName,String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE2_COL_2,fromName);
        contentValues.put(TABLE2_COL_3,toName);
        contentValues.put(TABLE2_COL_4,amount);


        long result = db.insert(TABLE_NAME2,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }


    public Cursor fetchAllFromUserInfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME1;
        Cursor cursor = db.rawQuery(query,null);
         return cursor;
    }

   public Cursor fetchAllFromTransacInfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME2;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
   }


   public Cursor fetchSingleDataFromUserInfo(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1 + " WHERE ID='" + id + "'";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
   }


    public Cursor fetchSingleDataFromTransacInfo(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE ID='" + id + "'";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }


   public boolean updateUserInfo(String id,String balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COL_5,balance);
         long result = db.update(TABLE_NAME1,contentValues,TABLE1_COL_1 + "=?",new String[]{id});
         if(result == -1){
            return false;
         }else{
             return true;
         }

   }







}
