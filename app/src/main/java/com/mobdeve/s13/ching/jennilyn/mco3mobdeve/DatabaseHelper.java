package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_DATE = "date";
    public static final String COLUMN_TASK_TIME = "time";
    public static final String COLUMN_TASK_COLOR = "color";
    public static final String COLUMN_TASK_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FULLNAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_TITLE + " TEXT,"
                + COLUMN_TASK_DESCRIPTION + " TEXT,"
                + COLUMN_TASK_DATE + " TEXT,"
                + COLUMN_TASK_TIME + " TEXT,"
                + COLUMN_TASK_COLOR + " TEXT,"
                + COLUMN_TASK_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);

        Log.d("DatabaseHelper", "Database created with tables: " + CREATE_USERS_TABLE + " and " + CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public boolean addUser(String fullName, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        Log.d("DatabaseHelper", "addUser result: " + result);

        return result != -1;
    }

    public boolean checkUser(String fullName, String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_FULLNAME + " = ? AND "
                + COLUMN_PHONE + " = ? AND "
                + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{fullName, phone, password});

        boolean exists = (cursor.getCount() > 0);
        Log.d("DatabaseHelper", "checkUser exists: " + exists);

        cursor.close();
        db.close();

        return exists;
    }

    public boolean addTask(String title, String description, String date, String time, String color, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_DATE, date);
        values.put(COLUMN_TASK_TIME, time);
        values.put(COLUMN_TASK_COLOR, color);
        values.put(COLUMN_TASK_STATUS, status);

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();

        Log.d("DatabaseHelper", "addTask result: " + result + " for date: " + date);

        return result != -1;
    }

    public boolean updateTaskStatus(int taskId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_STATUS, status);

        int result = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();

        return result > 0;
    }

    public Cursor getTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_DATE + " = ?";
        Log.d("DatabaseHelper", "Querying tasks for date: " + date);
        return db.rawQuery(query, new String[]{date});
    }

    public Cursor getTasksDueToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        String todayDate = getCurrentDate(); // Assume getCurrentDate() method returns today's date in the same format as stored in DB
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_DATE + " = ?";
        return db.rawQuery(query, new String[]{todayDate});
    }

    public Cursor getUpcomingTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String todayDate = getCurrentDate(); // Assume getCurrentDate() method returns today's date in the same format as stored in DB
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_DATE + " > ?";
        return db.rawQuery(query, new String[]{todayDate});
    }

    public Cursor getOverdueTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String todayDate = getCurrentDate(); // Assume getCurrentDate() method returns today's date in the same format as stored in DB
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_DATE + " < ?";
        return db.rawQuery(query, new String[]{todayDate});
    }

    private String getCurrentDate() {
        // Method to get current date in the required format
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based in Calendar
        int year = calendar.get(Calendar.YEAR);
        return String.format("%04d-%02d-%02d", year, month, day);
    }

}