package com.example.calmcheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.calmcheck.model.ChatMessage;
import com.example.calmcheck.model.Symptom;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "CalmCheck.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE symptoms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "description TEXT, " +
                "response TEXT, " +
                "severity TEXT, " +
                "summary TEXT, " +
                "date TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS symptoms");
        onCreate(db);
    }

    public void insertSymptom(String userId, String description, String response, String severity, String summary, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("description", description);
        values.put("response", response);
        values.put("severity", severity);
        values.put("summary", summary);
        values.put("date", date);
        db.insert("symptoms", null, values);
        db.close();
    }

    public List<Symptom> getRecentSymptoms(String userId, int limit) {
        List<Symptom> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM symptoms WHERE user_id = ? ORDER BY id DESC LIMIT ?", new String[]{userId, String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new Symptom(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<ChatMessage> getMessagesForUser(String userId) {
        List<ChatMessage> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sender, message FROM conversations WHERE user_id = ? ORDER BY timestamp ASC", new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(0);
                String message = cursor.getString(1);
                messages.add(new ChatMessage(sender, message));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return messages;
    }


    public List<Symptom> searchByKeyword(String userId, String keyword) {
        List<Symptom> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM symptoms WHERE user_id = ? AND description LIKE ? ORDER BY id DESC LIMIT 5", new String[]{userId, "%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                list.add(new Symptom(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
