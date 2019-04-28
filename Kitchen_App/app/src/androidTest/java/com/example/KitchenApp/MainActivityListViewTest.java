package com.example.KitchenApp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityListViewTest {
    private int millis = 300;
    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        MainActivity.dbRobotStatus.setValue(getResourceString(R.string.robot_status_working));
        wait(millis);
        MainActivity.clearOrders(MainActivity.dbDeliveryList);
        wait(millis);
        MainActivity.createExampleOrders(MainActivity.dbDeliveryList);
        wait(millis);
    }
    @Test
    public void createOrdersTest() {
        assertEquals(MainActivity.localFoodOrders.size(), 3);
    }
    @Test
    public void listViewInitTest() {
        Activity activity = activityTestRule.getActivity();
        ListView listView = activity.findViewById(R.id.listView);

        MainActivity.dbDeliveryList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String status = MainActivity.getField(item_snapshot, "status");
                    assertEquals(status, getResourceString(R.string.food_status_active));
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        assertEquals(3, MainActivity.localFoodOrders.size());
    }
    @Test
    public void listViewAcceptTest() {
        Activity activity = activityTestRule.getActivity();
        ListView listView = activity.findViewById(R.id.listView);

        listView.getAdapter().getView(0, null, null).findViewById(R.id.imageAccept).performClick();
        wait(millis);

        MainActivity.dbDeliveryList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String status = MainActivity.getField(item_snapshot, "status");
                    assertEquals(status, getResourceString(R.string.food_status_accept));
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        assertEquals(3, MainActivity.localFoodOrders.size());
    }
    @Test
    public void listViewReadyTest() {
        Activity activity = activityTestRule.getActivity();
        ListView listView = activity.findViewById(R.id.listView);

        listView.getAdapter().getView(0, null, null).findViewById(R.id.imageReady).performClick();
        wait(millis);

        MainActivity.dbDeliveryList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String status = MainActivity.getField(item_snapshot, "status");
                    assertEquals(status, getResourceString(R.string.food_status_ready));
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        assertEquals(2, MainActivity.localFoodOrders.size());
    }

    @Test
    public void listViewRejectTest() {
        Activity activity = activityTestRule.getActivity();
        ListView listView = activity.findViewById(R.id.listView);

        listView.getAdapter().getView(0, null, null).findViewById(R.id.imageReject).performClick();
        wait(millis);

        MainActivity.dbDeliveryList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String status = MainActivity.getField(item_snapshot, "status");
                    assertEquals(status, getResourceString(R.string.food_status_reject));
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        assertEquals(2, MainActivity.localFoodOrders.size());
    }
    @Test
    public void listViewUndoTest() {
        Activity activity = activityTestRule.getActivity();
        ListView listView = activity.findViewById(R.id.listView);

        listView.getAdapter().getView(0, null, null).findViewById(R.id.imageReject).performClick();
        wait(millis);

        MainActivity.dbDeliveryList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String status = MainActivity.getField(item_snapshot, "status");
                    assertEquals(status, getResourceString(R.string.food_status_reject));
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        assertEquals(2, MainActivity.localFoodOrders.size());

        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());
        wait(millis);
        assertEquals(3, MainActivity.localFoodOrders.size());
    }
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
    private void wait(int millis) {
        try {
            sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
