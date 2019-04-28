package com.example.KitchenApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BlockedActivity extends AppCompatActivity {
    private static DatabaseReference dbRobotStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRobotStatus = database.getReference("robot_status");

        Button buttonBlocked = findViewById(R.id.buttonBlocked);
        buttonBlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRobotStatus.setValue(getString(R.string.robot_status_working));
                Intent intent = new Intent(BlockedActivity.this, MainActivity.class);
                    startActivity(intent);
                }
        });
    }
}
