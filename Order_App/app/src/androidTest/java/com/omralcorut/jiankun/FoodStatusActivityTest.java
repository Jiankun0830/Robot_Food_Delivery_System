package com.omralcorut.jiankun;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class FoodStatusActivityTest {
    @Rule
    public ActivityTestRule<FoodStatusActivity> activityActivityTestRule =
            new ActivityTestRule<FoodStatusActivity>(FoodStatusActivity.class);


    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        Intents.init();
    }

    @After
    public void release(){
        Intents.release();
    }




}
