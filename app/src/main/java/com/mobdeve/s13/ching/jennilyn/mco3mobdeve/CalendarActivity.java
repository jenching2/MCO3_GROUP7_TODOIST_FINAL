package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private Button btnAddTask;
    private CalendarView calendarView;
    private DatabaseHelper dbHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private static final String TAG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        rvTasks = findViewById(R.id.rvTasks);
        btnAddTask = findViewById(R.id.btnAddTask);
        calendarView = findViewById(R.id.calendarView);

        dbHelper = new DatabaseHelper(this);
        taskList = new ArrayList<>();

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, taskList);
        rvTasks.setAdapter(taskAdapter);

        btnAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Load tasks for today's date when the activity is first created
        loadTasks(getCurrentDate());

        // Set listener for date change
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            loadTasks(formatDate(calendar));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload tasks whenever the activity is resumed
        loadTasks(getCurrentDate());
    }

    private void loadTasks(String date) {
        Log.d(TAG, "Loading tasks for date: " + date);
        taskList.clear();
        Cursor cursor = dbHelper.getTasks(date);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DESCRIPTION));
                String taskDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIME));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_COLOR));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_STATUS));

                Log.d(TAG, "Task loaded: " + title + " at " + time + " with status " + status);

                taskList.add(new Task(id, title, description, taskDate, time, color, status));
            } while (cursor.moveToNext());

            cursor.close();
        }

        taskAdapter.notifyDataSetChanged();
        Log.d(TAG, "Tasks loaded for date " + date + ": " + taskList.size());
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar);
    }

    private String formatDate(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based in Calendar
        int year = calendar.get(Calendar.YEAR);
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
