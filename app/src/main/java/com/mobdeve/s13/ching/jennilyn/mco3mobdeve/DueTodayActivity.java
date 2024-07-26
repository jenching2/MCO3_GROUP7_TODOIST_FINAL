package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DueTodayActivity extends AppCompatActivity {

    private RecyclerView rvDueTodayTasks;
    private DatabaseHelper dbHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private Button btnUpcomingTasks, btnDueTodayTasks, btnOverdueTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_today); // Ensure this layout file matches your XML

        rvDueTodayTasks = findViewById(R.id.rvTasks);
        dbHelper = new DatabaseHelper(this);
        taskList = new ArrayList<>();

        rvDueTodayTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, taskList);
        rvDueTodayTasks.setAdapter(taskAdapter);

        btnUpcomingTasks = findViewById(R.id.btnUpcomingTasks);
        btnDueTodayTasks = findViewById(R.id.btnDueTodayTasks);
        btnOverdueTasks = findViewById(R.id.btnOverdueTasks);

        btnUpcomingTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DueTodayActivity.this, UpcomingTasksActivity.class);
                startActivity(intent);
            }
        });

        btnDueTodayTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDueTodayTasks(); // Reload current activity's tasks
            }
        });

        btnOverdueTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DueTodayActivity.this, OverdueTasksActivity.class);
                startActivity(intent);
            }
        });

        loadDueTodayTasks();
    }

    private void loadDueTodayTasks() {
        taskList.clear();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based in Calendar
        int year = calendar.get(Calendar.YEAR);
        String todayDate = String.format("%04d-%02d-%02d", year, month, day);

        Cursor cursor = dbHelper.getTasks(todayDate);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIME));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_COLOR));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_STATUS));

                taskList.add(new Task(id, title, description, date, time, color, status));
            } while (cursor.moveToNext());

            cursor.close();
        }

        taskAdapter.notifyDataSetChanged();
    }
}
