package com.omralcorut.jiankun;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.app.PendingIntent.getActivity;
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
import static org.hamcrest.Matchers.not;

public class CartActivityTest{
        @Rule
        public ActivityTestRule<MenuActivity> activityTestRule =
                new ActivityTestRule<MenuActivity>(MenuActivity.class);

        @Before
        public void init(){
            activityTestRule.getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Intents.init();
        }

        @After
        public void release(){
            Intents.release();
        }


        @Test
        public void TestOver21(){

            onData(anything()).inAdapterView(withId(R.id.list)).atPosition(1).perform(click());
            intended(hasComponent(FoodActivity.class.getName()));

            for(int i = 0;i < 21;i++){
                onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
            }

            onView(withId(R.id.fab)).perform(click());
            intended(hasComponent(CartActivity.class.getName()));
            onView(withId(R.id.btn_order)).perform(click());
            onView(withId(R.id.total_cost)).check(matches(not(withText("0.0 SGD"))));
            onView(withId(R.id.empty_cart)).perform(click());
            onView(withText("Yes")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());



        }

        @Test
        public void TestBelow21(){

            onData(anything()).inAdapterView(withId(R.id.list)).atPosition(1).perform(click());
            intended(hasComponent(FoodActivity.class.getName()));

            for(int i = 0;i < 10;i++){
                onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
            }

            onView(withId(R.id.fab)).perform(click());
            intended(hasComponent(CartActivity.class.getName()));
            onView(withId(R.id.btn_order)).perform(click());
            onView(withText("Yes")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.total_cost)).check(matches(withText("0.0 SGD")));



        }

}
