package com.omralcorut.jiankun;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;

public class FoodActivityTest {

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
    public void TestPrepare(){

        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(1).perform(click());
        intended(hasComponent(FoodActivity.class.getName()));

        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(CartActivity.class.getName()));
        onView(withId(R.id.empty_cart)).perform(click());
        onView(withText("Yes")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());


    }

    @Test
    public void TestMenu(){

        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(1).perform(click());
        intended(hasComponent(FoodActivity.class.getName()));
        onView(withId(R.id.cart_count)).check(matches(withText("0")));
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(2).perform(click());
        onView(withId(R.id.cart_count)).check(matches(withText("2")));


    }


}
