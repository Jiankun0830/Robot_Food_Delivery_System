package com.omralcorut.jiankun;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

public class MenuActivityTest {
    @Rule
    public ActivityTestRule<MenuActivity> activityActivityTestRule =
            new ActivityTestRule<MenuActivity>(MenuActivity.class);


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

    @Test
    public void TestMenu(){
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
        intended(hasComponent(FoodActivity.class.getName()));

    }

}
