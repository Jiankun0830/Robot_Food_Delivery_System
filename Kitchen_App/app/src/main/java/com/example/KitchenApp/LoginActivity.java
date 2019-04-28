package com.example.KitchenApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    static final int failCountLimit = 3;
    static LoginChecker loginChecker;
    static FailCounter failCounter;
    static int timerCoolDown = 10 * 1000;
    static int timerTick = 1000;
    TextView failCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        failCountTextView = findViewById(R.id.activityLoginFailCountTextView);
        final CardView cardView = findViewById(R.id.activityLoginCardView);
        final EditText editUsername = findViewById(R.id.editUsername);
        final EditText editPassword = findViewById(R.id.editPassword);
        loginChecker = new LoginChecker(getString(R.string.username), getString(R.string.password));
        failCounter = new FailCounter(failCountLimit);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!failCounter.checkExceededLimit()) {
                    loginChecker.updateStatus(editUsername.getText().toString(), editPassword.getText().toString());
                    if (loginChecker.checkSuccess()) {
                        failCounter.reset();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        failCounter.increment();
                        if (failCounter.checkExceededLimit()) {
                            startCoolDownTimer();
                        }
                    }
                }
                failCountTextView.setText(failCounter.getMessage());
            }
        });
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // reset count and text view to default state
                failCounter.reset();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }
    static class LoginChecker {
        final String trueUsername;
        final String truePassword;
        boolean status;
        LoginChecker(String trueUsername, String truePassword) {
            this.trueUsername = trueUsername;
            this.truePassword = truePassword;
        }
        void updateStatus(String username, String password) {
            status = username.equals(trueUsername) && password.equals(truePassword);
        }
        boolean checkSuccess() {
            return status;
        }
    }
    static class FailCounter {
        final int countLimit;
        int count;

        FailCounter(int countLimit) {
            this.countLimit = countLimit;
            reset();
        }
        void increment() {
            count++;
            if (checkExceededLimit()) {
                count = countLimit;
            }
        }
        void reset() {
            count = 0;
        }
        boolean checkExceededLimit() {
            return count >= countLimit;
        }
        String getMessage() {
            return (countLimit - count) + " of " + countLimit + " attempts remaining";
        }
    }
    void startCoolDownTimer() {
        new CountDownTimer(timerCoolDown, timerTick) {
            public void onTick(long millisRemaining) {
                String message = "Locked! Try again in " + (millisRemaining / 1000) + " seconds.";
                failCountTextView.setText(message);
            }
            public void onFinish() {
                failCounter.reset();
                failCountTextView.setText("");
            }
        }.start();
    }
}
