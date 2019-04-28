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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private final String correctUsername = getResourceString(R.string.username);
    private final String correctPassword = getResourceString(R.string.password);
    private final String wrongUserame = "wrong" + correctUsername;
    private final String wrongPassword  = "wrong" + correctPassword;
    @Rule
    public final ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);
    @Before
    public void init(){
        activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    @Test
    public void testLoginSuccess(){
        Intents.init();
        performLogin(correctUsername, correctPassword);
        intended(hasComponent(MainActivity.class.getName()));
        assertTrue(LoginActivity.loginChecker.checkSuccess());
        assertFalse(LoginActivity.failCounter.checkExceededLimit());
        Intents.release();
    }
    @Test
    public void testLoginFail(){
        onView(withId(R.id.editUsername)).perform(typeText(wrongUserame), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText(wrongPassword), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        assertFalse(LoginActivity.loginChecker.checkSuccess());
        assertFalse(LoginActivity.failCounter.checkExceededLimit());
    }
    @Test
    public void testLoginLocked() {
        for (int i = 0; i < LoginActivity.failCountLimit; i++) {
            assertFalse(LoginActivity.failCounter.checkExceededLimit());
            performLogin(wrongUserame, wrongPassword);
            assertFalse(LoginActivity.loginChecker.checkSuccess());
        }
        performLogin(correctUsername, correctPassword);
        assertTrue(LoginActivity.failCounter.checkExceededLimit());
        assertFalse(LoginActivity.loginChecker.checkSuccess());
    }

    @Test
    public void testLoginCoolDown() {
        Intents.init();
        for (int i = 0; i < LoginActivity.failCountLimit; i++) {
            assertFalse(LoginActivity.failCounter.checkExceededLimit());
            performLogin(wrongUserame, wrongPassword);
            assertFalse(LoginActivity.loginChecker.checkSuccess());
        }
        performLogin(correctUsername, correctPassword);
        assertTrue(LoginActivity.failCounter.checkExceededLimit());
        assertFalse(LoginActivity.loginChecker.checkSuccess());
        try {
            sleep(LoginActivity.timerCoolDown);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performLogin(correctUsername, correctPassword);
        intended(hasComponent(MainActivity.class.getName()));
        assertTrue(LoginActivity.loginChecker.checkSuccess());
        assertFalse(LoginActivity.failCounter.checkExceededLimit());
        Intents.release();
    }
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
    private void performLogin(String username, String password) {
        onView(withId(R.id.editUsername)).perform(clearText());
        onView(withId(R.id.editPassword)).perform(clearText());
        onView(withId(R.id.editUsername)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
    }
}
