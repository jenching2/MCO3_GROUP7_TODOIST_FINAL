package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mobdeve.s13.ching.jennilyn.mco3mobdeve.custom.CustomNumberPicker;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private CustomNumberPicker npDay, npMonth, npHour, npMinute, npAmPm;
    private EditText etTaskTitle, etTaskDescription;
    private RadioGroup rgColor, rgStatus;
    private Button btnAddTask;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        npDay = findViewById(R.id.npDay);
        npMonth = findViewById(R.id.npMonth);
        npHour = findViewById(R.id.npHour);
        npMinute = findViewById(R.id.npMinute);
        npAmPm = findViewById(R.id.npAmPm);
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        rgColor = findViewById(R.id.rgColor);
        rgStatus = findViewById(R.id.rgStatus);
        btnAddTask = findViewById(R.id.btnAddTask);
        dbHelper = new DatabaseHelper(this);

        // Initialize CustomNumberPicker values
        Calendar calendar = Calendar.getInstance();
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npDay.setValue(calendar.get(Calendar.DAY_OF_MONTH));

        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(calendar.get(Calendar.MONTH) + 1);

        npHour.setMinValue(1);
        npHour.setMaxValue(12);
        npHour.setValue(calendar.get(Calendar.HOUR) == 0 ? 12 : calendar.get(Calendar.HOUR));

        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);
        npMinute.setValue(calendar.get(Calendar.MINUTE));

        npAmPm.setMinValue(0);
        npAmPm.setMaxValue(1);
        npAmPm.setDisplayedValues(new String[]{"AM", "PM"});
        npAmPm.setValue(calendar.get(Calendar.AM_PM));

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etTaskDescription.getText().toString().trim();
        String color = getSelectedColor();
        String status = getSelectedStatus();

        int day = npDay.getValue();
        int month = npMonth.getValue();
        int hour = npHour.getValue();
        int minute = npMinute.getValue();
        String amPm = npAmPm.getValue() == 0 ? "AM" : "PM";

        if (amPm.equals("PM") && hour != 12) {
            hour += 12;
        } else if (amPm.equals("AM") && hour == 12) {
            hour = 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1); // Month is 0-based in Calendar
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        String date = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), month, day);
        String time = String.format("%02d:%02d", hour, minute);

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addTask(title, description, date, time, color, status);

        if (isInserted) {
            Log.d("AddTaskActivity", "Task inserted: " + title + " on " + date + " at " + time);
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d("AddTaskActivity", "Task insertion failed");
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedColor() {
        int selectedId = rgColor.getCheckedRadioButtonId();
        if (selectedId == R.id.rbGreen) {
            return "Green";
        } else if (selectedId == R.id.rbPurple) {
            return "Purple";
        } else if (selectedId == R.id.rbRed) {
            return "Red";
        }
        return "Default";
    }

    private String getSelectedStatus() {
        int selectedId = rgStatus.getCheckedRadioButtonId();
        if (selectedId == R.id.rbNotStarted) {
            return "Not yet started";
        } else if (selectedId == R.id.rbInProgress) {
            return "In progress";
        } else if (selectedId == R.id.rbDone) {
            return "Done";
        }
        return "Not yet started";
    }
}
