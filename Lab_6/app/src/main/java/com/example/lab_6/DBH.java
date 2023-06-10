package com.example.lab_6;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBH extends SQLiteOpenHelper {


    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_USER_PASS = "USER_PASS";
    public static final String COLUMN_USER_SALT = "USER_SALT";
    public static final String COLUMN_ID = "ID";

    public DBH(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_PASS + " TEXT, " + COLUMN_USER_SALT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean addUser(userModel ustmdl){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_NAME,ustmdl.getName());
        cv.put(COLUMN_USER_PASS,ustmdl.getPass());
        cv.put(COLUMN_USER_SALT,ustmdl.getSalt());

        long insert = db.insert(USER_TABLE, null, cv);

        return insert != -1;
    }

    public List<userModel> getUser(){
        List<userModel> retList = new ArrayList<>();

        String qStr = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(qStr,null);

        if(cursor.moveToFirst()){

            do{
                int userID = cursor.getInt(0);
                String username = cursor.getString(1);
                String userpass = cursor.getString(2);
                String usersalt = cursor.getString(3);

                userModel new_user = new userModel(userID,username,userpass,usersalt);
                retList.add(new_user);


            }while(cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return retList;

    }

    public userModel getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        userModel user = null;

        String[] columns = {COLUMN_ID, COLUMN_USER_NAME, COLUMN_USER_PASS, COLUMN_USER_SALT};
        String selection = COLUMN_USER_NAME + "=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int userID = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            @SuppressLint("Range") String userPass = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASS));
            @SuppressLint("Range") String userSalt = cursor.getString(cursor.getColumnIndex(COLUMN_USER_SALT));

            user = new userModel(userID, userName, userPass, userSalt);
        }

        cursor.close();
        db.close();

        return user;
    }


    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASS, newPassword);

        String selection = COLUMN_USER_NAME + "=?";
        String[] selectionArgs = {username};

        int rowsAffected = db.update(USER_TABLE, values, selection, selectionArgs);

        db.close();

        return rowsAffected > 0;
    }

}
