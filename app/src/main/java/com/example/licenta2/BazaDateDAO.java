package com.example.licenta2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BazaDateDAO {
    private SQLiteDatabase database;
    private Context context;

    public BazaDateDAO(Context context){
        this.context = context;
    }

    public void open() throws SQLException {
        BazaDate bazaDate = new BazaDate(context);
        database = bazaDate.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public Cursor getAllUsers() {
        return database.rawQuery("SELECT * FROM users", null);
    }

    public long addUser(String userEmail, int points, int totalQuestions, int wrong){
        ContentValues values = new ContentValues();
        values.put("userEmail", userEmail);
        values.put("points", points);
        values.put("totalQuestions", totalQuestions);
        values.put("wrong", wrong);
        return database.insert("users", null, values);
    }

    public int updateUserPoints( String userEmail, int points) {
        ContentValues values = new ContentValues();
        values.put("points", points);
        String whereClause = "userEmail=?";
        String[] whereArgs = new String[] { String.valueOf(userEmail) };
        return database.update("users", values, whereClause, whereArgs);
    }


    @SuppressLint("Range")
    public int fetchUserPoints(String userEmail) {
        String[] columns = {"points"};
        String selection = "userEmail=?";
        String[] selectionArgs = {userEmail};
        Cursor cursor = database.query("users", columns, selection, selectionArgs, null, null, null);
        int points = 0;
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("points");
            if (!cursor.isNull(columnIndex)) {
                points = cursor.getInt(columnIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return points;
    }

    public int incrementUserTotalAnswers(String userEmail, int incrementValue) {
        int newTotalAnswers = fetchUserTotalAnswers(userEmail) + incrementValue;

        ContentValues values = new ContentValues();
        values.put("totalQuestions", newTotalAnswers);

        String whereClause = "userEmail=?";
        String[] whereArgs = new String[]{userEmail};

        return database.update("users", values, whereClause, whereArgs);
    }
    public int fetchUserTotalAnswers(String userEmail) {
        String[] columns = {"totalQuestions"};
        String selection = "userEmail=?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = database.query("users", columns, selection, selectionArgs, null, null, null);
        int totalAnswers = 0;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("totalQuestions");
            if (!cursor.isNull(columnIndex)) {
                totalAnswers = cursor.getInt(columnIndex);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return totalAnswers;
    }

    public int incrementUserCorrectAnswers(String userEmail, int incrementValue) {
        int newTotalCorrect = fetchUserTotalCorrectAnswers(userEmail) + 1;

        ContentValues values = new ContentValues();
        values.put("wrong", newTotalCorrect);

        String whereClause = "userEmail=?";
        String[] whereArgs = new String[]{userEmail};

        return database.update("users", values, whereClause, whereArgs);
    }

    public int fetchUserTotalCorrectAnswers(String userEmail) {
        String[] columns = {"wrong"};
        String selection = "userEmail=?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = database.query("users", columns, selection, selectionArgs, null, null, null);
        int totalCorrectAnwers = 0;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("wrong");
            if (!cursor.isNull(columnIndex)) {
                totalCorrectAnwers = cursor.getInt(columnIndex);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return totalCorrectAnwers;
    }

}
