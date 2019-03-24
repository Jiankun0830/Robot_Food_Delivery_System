package com.example.KitchenApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editUsername = findViewById(R.id.editUsername);
                EditText editPassword = findViewById(R.id.editPassword);
                loginSuccess = checkLogin(
                        editUsername.getText().toString(),
                        editPassword.getText().toString(),
                        getString(R.string.username),
                        getString(R.string.password)
                );
                Toast.makeText(LoginActivity.this, "Login success " + loginSuccess, Toast.LENGTH_SHORT).show();
                if (loginSuccess) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        buttonLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }
    static boolean checkLogin(
            String enteredUsername,
            String enteredPassword,
            String trueUsername,
            String truePassword
    ) {
        return enteredUsername.equals(trueUsername) && enteredPassword.equals(truePassword);
    }
}
