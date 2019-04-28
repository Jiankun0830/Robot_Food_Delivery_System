package com.omralcorut.jiankun;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;

@RunWith(AndroidJUnit4.class)
public class IntroActivityTest {
    @Rule
    public ActivityTestRule<IntroActivity> activityActivityTestRule = new ActivityTestRule<IntroActivity>(IntroActivity.class);

    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void TestScanButton(){
        onView(withId(R.id.scanbutton)).check(matches(isDisplayed()));
//        onView(withId(R.id.scanbutton)).perform(click());
//        intended(hasComponent(MainActivity.class.getName()));

    }



}
