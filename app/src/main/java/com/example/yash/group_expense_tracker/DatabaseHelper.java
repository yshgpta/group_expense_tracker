package com.example.yash.group_expense_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GrpExp.db";
    public static final String TABLE1_NAME = "Members";
    public static final String COL1_1 = "MemberID";
    public static final String COL1_2 = "MemberName";
    public static final String COL1_3 = "TripName";
    public static final String TABLE2_NAME = "Expenses";
    public static final String COL2_1 = "ExpenseID";
    public static final String COL2_2 = "ExpenseType";
    public static final String COL2_3 = "Value";
    public static final String COL2_4 = "MemberID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE1_NAME + " (MemberID INTEGER PRIMARY KEY AUTOINCREMENT, MemberName TEXT NOT NULL, TripName TEXT )");
        db.execSQL("create table "+ TABLE2_NAME + "( ExpenseID INTEGER PRIMARY KEY AUTOINCREMENT, ExpenseType TEXT NOT NULL , Value INTEGER NOT NULL , MemberID INTEGER NOT NULL, FOREIGN KEY(MemberID) REFERENCES Members(MemberID) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE2_NAME);
        onCreate(db);
    }

    public boolean insertDataTb1(String MemberName,String TripName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1_2,MemberName);
        contentValues.put(COL1_3,TripName);
        long res = db.insert(TABLE1_NAME,null,contentValues);
        if(res==-1)
            return false;
        else
            return true;
    }
    public boolean insertDataTb2(String ExpenseType,String Value,String MemberID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_2,ExpenseType);
        contentValues.put(COL2_3,Value);
        contentValues.put(COL2_4,MemberID);
        long res = db.insert(TABLE2_NAME,null,contentValues);
        if(res==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE1_NAME,null);
        return res;
    }
    public Cursor getAllExpense() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE2_NAME, null);
        return res;
    }
    public Cursor getExpensebyId(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE2_NAME+" where MemberID = "+Id,null);
        return res;
    }
    public Cursor getExpenseType() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ExpenseType from "+TABLE2_NAME,null);
        return res;
    }
    public Cursor getExpenseTypeByName(String Type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE2_NAME+" where ExpenseType="+"'"+Type+"'",null);
        return res;
    }
    public Cursor getValueByExpenseNameAndId(String ExpName,String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE2_NAME+" where ExpenseType="+"'"+ExpName+"' and MemberID="+"'"+Id+"'",null);
        return res;
    }
    public boolean updateByIdAndExpense(String ExpName,String Id,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_3,value);
        long res = db.update(TABLE2_NAME,contentValues,"ExpenseType='"+ExpName+"' and MemberID='"+Id+"'",null);
        if(res==-1)
            return false;
        else
            return true;
    }

    public boolean dropDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        File file = new File(db.getPath());
        if(SQLiteDatabase.deleteDatabase(file))
            return true;
        else
            return false;
    }
}

