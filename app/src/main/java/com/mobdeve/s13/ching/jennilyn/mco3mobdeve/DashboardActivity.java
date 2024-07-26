package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvDueTodayCount, tvUpcomingCount, tvOverdueCount, tvWelcome;
    private ImageView ivMenuIcon;
    private DrawerLayout drawerLayout;
    private TextView navHeaderUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvDueTodayCount = findViewById(R.id.tvDueTodayCount);
        tvUpcomingCount = findViewById(R.id.tvUpcomingCount);
        tvOverdueCount = findViewById(R.id.tvOverdueCount);
        ivMenuIcon = findViewById(R.id.ivMenuIcon);
        drawerLayout = findViewById(R.id.drawer_layout);
        tvWelcome = findViewById(R.id.tvWelcome);

        // Retrieve the user's name from the intent extras
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");

        // Set the welcome message with the user's name
        if (userName != null && !userName.isEmpty()) {
            tvWelcome.setText("Welcome Back, " + userName + "!");
        }

        // Find the TextView in the navigation drawer header and set the user's name
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navHeaderUserName = headerView.findViewById(R.id.tvUserName);

        if (userName != null && !userName.isEmpty()) {
            navHeaderUserName.setText(userName);
        }

        tvDueTodayCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DueTodayActivity.class);
                startActivity(intent);
            }
        });

        tvUpcomingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, UpcomingTasksActivity.class);
                startActivity(intent);
            }
        });

        tvOverdueCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, OverdueTasksActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.addTaskContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        ivMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }
}
