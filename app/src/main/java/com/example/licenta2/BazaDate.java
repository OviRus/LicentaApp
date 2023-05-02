package com.example.licenta2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BazaDate extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";



    public BazaDate(Context context) {

        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        sqLiteDatabase.execSQL("create table utilizatori(email_user TEXT primary key, parola Text)");
    }

    private SQLiteDatabase getDatabasePath(String s) {
        return null;
    }

    private IntDef getAssets() {
        return null;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS utilizatori");
        onCreate(sqLiteDatabase);
    }

    public Boolean insertData(String email_user, String parola) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email_user", email_user);
        contentValues.put("parola", parola);
        long result = sqLiteDatabase.insert("utilizatori", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Boolean verificareEmailUser(String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from utilizatori where email_user=?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean verificaUserEmailParola(String emailUser, String parola) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from utilizatori where email_user = ? and parola = ?", new String[]{emailUser, parola});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
