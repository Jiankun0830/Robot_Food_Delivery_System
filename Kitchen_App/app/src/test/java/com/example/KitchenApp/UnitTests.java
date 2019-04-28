package com.example.KitchenApp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void checkLoginTest() {
        String correctUsername = "username";
        String correctPassword = "password";
        String wrongUsername = "wrong_username";
        String wrongPassword = "wrong_password";
        String trueUsername = "username";
        String truePassword = "password";

        LoginActivity.LoginChecker loginChecker = new LoginActivity.LoginChecker(trueUsername, truePassword);

        loginChecker.updateStatus(correctUsername, correctPassword);
        assertTrue(loginChecker.checkSuccess());

        loginChecker.updateStatus(wrongUsername, correctPassword);
        assertFalse(loginChecker.checkSuccess());

        loginChecker.updateStatus(correctUsername, wrongPassword);
        assertFalse(loginChecker.checkSuccess());

        loginChecker.updateStatus(wrongUsername, wrongPassword);
        assertFalse(loginChecker.checkSuccess());
    }
    @Test
    public void failCounterTest() {
        int limit = 5;
        LoginActivity.FailCounter failCounter = new LoginActivity.FailCounter(limit);
        for (int i = 0; i < limit; i++) {
            assertFalse(failCounter.checkExceededLimit());
            failCounter.increment();
        }
        assertTrue(failCounter.checkExceededLimit());
        failCounter.reset();
        assertFalse(failCounter.checkExceededLimit());
    }
}
