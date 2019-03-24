package com.example.KitchenApp;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.android.dx.command.findusages.Main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import com.example.KitchenApp.R;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    @Before
    public void init(){
        activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        Intents.init();
    }
    @Test
    public void testLoginSuccess(){
//        Resources resources = InstrumentationRegistry.getContext().getResources();
//        String trueUsername = resources.getString(R.string.username);
//        String truePassword = resources.getString(R.string.password);
        onView(withId(R.id.editUsername)).perform(typeText("username"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        assertTrue(LoginActivity.loginSuccess);
    }
    @After
    public void release() {
        Intents.release();
    }
    @Test
    public void testLoginFail(){
        onView(withId(R.id.editUsername)).perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        assertFalse(LoginActivity.loginSuccess);
    }
}
