package com.omralcorut.jiankun;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


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
        onView(withId(R.id.menuButton)).check(matches(isDisplayed()));
        onView(withId(R.id.menuButton)).perform(click());
        intended(hasComponent(MenuActivity.class.getName()));
    }

    @Test
    public void TestFoodStatus(){
        onView(withId(R.id.foodStatusButton)).check(matches(isDisplayed()));
        onView(withId(R.id.foodStatusButton)).perform(click());
        intended(hasComponent(FoodStatusActivity.class.getName()));
    }

    @Test
    public void TestHistory(){
        onView(withId(R.id.historyButton)).check(matches(isDisplayed()));
        onView(withId(R.id.historyButton)).perform(click());
        intended(hasComponent(MyOrdersActivity.class.getName()));
    }



}
