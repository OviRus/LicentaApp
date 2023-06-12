package com.example.licenta2;

import android.annotation.SuppressLint;
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
    public BazaDate(Context context) {
        super(context, "DBLicenta.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userEmail TEXT, " +
                "points INTEGER, " +
                "totalQuestions INTEGER, " +
                "wrong INTEGER" +
                ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveUserEmail(String userEmail) {
        SQLiteDatabase db = getWritableDatabase();

        // Check if the user already exists in the database
        String[] projection = { "userEmail" };
        String selection = "userEmail = ?";
        String[] selectionArgs = { userEmail };
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            // User already exists, do nothing
            cursor.close();
            db.close();
            return;
        }

        // User does not exist, insert into the database
        ContentValues values = new ContentValues();
        values.put("userEmail", userEmail);
        long newRowId = db.insert("users", null, values);
        db.close();
    }

    public void saveUserPoints(String userEmail, int newPoints) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("points", newPoints);

        String selection = "userEmail = ?";
        String[] selectionArgs = { userEmail };

        int rowsAffected = db.update("users", values, selection, selectionArgs);
        db.close();
    }

    public boolean verifyUser(String userEmail) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { "userEmail" };
        String selection = "userEmail = ?";
        String[] selectionArgs = { userEmail };
        String limit = "1";

        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null, limit);

        boolean userExists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return userExists;
    }


    @SuppressLint("Range")
    public int withdrawPoints(String userEmail) {
        SQLiteDatabase db = getWritableDatabase();

        // Retrieve the current points value for the user
        String[] projection = { "points" };
        String selection = "userEmail = ?";
        String[] selectionArgs = { userEmail };
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);

        int currentPoints = 0;

        if (cursor != null && cursor.moveToFirst()) {
            currentPoints = cursor.getInt(cursor.getColumnIndex("points"));
            cursor.close();
        }

        // Update the table to set the points value to 0 for the specified user
        ContentValues values = new ContentValues();
        values.put("points", 0);

        String updateSelection = "userEmail = ?";
        String[] updateSelectionArgs = { userEmail };

        int rowsAffected = db.update("users", values, updateSelection, updateSelectionArgs);
        db.close();

        // Return the current points value after withdrawing
        return currentPoints;
    }

    @SuppressLint("Range")
    public void  saveNrQuestion(String userEmail, int newQuestions) {
        SQLiteDatabase db = getWritableDatabase();

        // Retrieve the current totalQuestions value from the table
        String[] projection = {"totalQuestions"};
        String selection = "userEmail = ?";
        String[] selectionArgs = {userEmail};
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);

        int currentQuestions = 0;

        if (cursor != null && cursor.moveToFirst()) {
            currentQuestions = cursor.getInt(cursor.getColumnIndex("totalQuestions"));
            cursor.close();
        }

        // Calculate the new totalQuestions value
        int updatedQuestions = currentQuestions + newQuestions;

        // Update the table with the new totalQuestions value
        ContentValues values = new ContentValues();
        values.put("totalQuestions", updatedQuestions);

        String updateSelection = "userEmail = ?";
        String[] updateSelectionArgs = {userEmail};

        int rowsAffected = db.update("users", values, updateSelection, updateSelectionArgs);
        db.close();
        }
    }
