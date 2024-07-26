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
import java.util.List;

public class OverdueTasksActivity extends AppCompatActivity {

    private RecyclerView rvOverdueTasks;
    private DatabaseHelper dbHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private Button btnUpcomingTasks, btnDueTodayTasks, btnOverdueTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_today); // Ensure this layout file matches your XML

        rvOverdueTasks = findViewById(R.id.rvTasks);
        dbHelper = new DatabaseHelper(this);
        taskList = new ArrayList<>();

        rvOverdueTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, taskList);
        rvOverdueTasks.setAdapter(taskAdapter);

        btnUpcomingTasks = findViewById(R.id.btnUpcomingTasks);
        btnDueTodayTasks = findViewById(R.id.btnDueTodayTasks);
        btnOverdueTasks = findViewById(R.id.btnOverdueTasks);

        btnUpcomingTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverdueTasksActivity.this, UpcomingTasksActivity.class);
                startActivity(intent);
            }
        });

        btnDueTodayTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverdueTasksActivity.this, DueTodayActivity.class);
                startActivity(intent);
            }
        });

        btnOverdueTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOverdueTasks(); // Reload current activity's tasks
            }
        });

        loadOverdueTasks();
    }

    private void loadOverdueTasks() {
        taskList.clear();
        Cursor cursor = dbHelper.getOverdueTasks();

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
