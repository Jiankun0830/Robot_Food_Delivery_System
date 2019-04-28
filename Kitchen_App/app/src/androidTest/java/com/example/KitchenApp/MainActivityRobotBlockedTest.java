package com.example.KitchenApp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
public class MainActivityRobotBlockedTest {
    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Before
    public void init(){
        activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    @Test
    public void robotBlockedTest(){
        Intents.init();
        MainActivity.dbRobotStatus.setValue(getResourceString(R.string.robot_status_blocked));
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(BlockedActivity.class.getName()), times(10));
        MainActivity.dbRobotStatus.setValue(getResourceString(R.string.robot_status_working));
        Intents.release();
    }
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}