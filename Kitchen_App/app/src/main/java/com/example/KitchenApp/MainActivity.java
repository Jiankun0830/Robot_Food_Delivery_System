package com.example.KitchenApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // gifs from james curran
    public static ArrayList<DataSnapshot> localFoodOrders = new ArrayList<>();
    public static DatabaseReference dbDeliveryList;
    public static DatabaseReference dbRobotStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbDeliveryList = database.getReference("delivery_list");
        dbRobotStatus = database.getReference("robot_status");

        final CardView cardView = findViewById(R.id.activityMainCardView);
        final TextView textView = findViewById(R.id.activityMainTextView);
        final ListView listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty));

        // for interactive testing purposes only
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createExampleOrders(dbDeliveryList);
                return false;
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbRobotStatus.setValue(getString(R.string.robot_status_blocked));
                return false;
            }
        });
        // monitor firebase food orders and update order list accordingly
        dbDeliveryList.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<DataSnapshot> newFoodOrders = new ArrayList<>();
                // If status is "Ready" or "Rejected", we no longer want to display them
                // Also subtly prioritise accepted items on top
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    if (getField(item_snapshot, "status").equals(getString(R.string.food_status_accept))) {
                        newFoodOrders.add(item_snapshot);
                    }
                }
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    if (getField(item_snapshot, "status").equals(getString(R.string.food_status_active))) {
                        newFoodOrders.add(item_snapshot);
                    }
                }
                listView.setAdapter(new CustomAdapter(getApplicationContext(), newFoodOrders));
                localFoodOrders = new ArrayList<>(newFoodOrders);

                if (localFoodOrders.isEmpty()) {
                    cardView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    cardView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        dbRobotStatus.addValueEventListener(new ValueEventListener() {
            // if blocked, go to BlockedActivity
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String robotStatus = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                if (robotStatus.equals(getString(R.string.robot_status_blocked))) {
                    Intent intent = new Intent(MainActivity.this, BlockedActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public static void createExampleOrders(DatabaseReference dbDeliveryList) {
        // Create example order in firebase for demo purposes
        for (int i = 1; i <= 3; i++) {
            dbDeliveryList.child("example" + i).child("status").setValue("ACTIVE");
            dbDeliveryList.child("example" + i).child("food").setValue("Example Order " + i);
            dbDeliveryList.child("example" + i).child("table").setValue(i);
            dbDeliveryList.child("example" + i).child("timestamp").setValue("0000000000000");
        }
    }

    public static void clearOrders(DatabaseReference dbDeliveryList) {
        dbDeliveryList.removeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static String getField(DataSnapshot dataSnapshot, String field) {
        // For convenience
        try {
            return Objects.requireNonNull(dataSnapshot.child(field).getValue()).toString();
        } catch (java.lang.NullPointerException e) {
            return "";
        }
    }

    class CustomAdapter extends BaseAdapter {
        // Based on http://www.prandroid.com/2016/03/dynamic-add-and-remove-item-on-listview.html
        final Context context;
        ArrayList<DataSnapshot> items;
        CustomAdapter(Context context, ArrayList<DataSnapshot> items) {
            this.context = context;
            updateOrders(items);
        }
        void updateOrders(ArrayList<DataSnapshot> items) {
            this.items = items;
            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null && localFoodOrders.size() > 0) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                TextView tvName = convertView.findViewById(R.id.tvName);
                tvName.setText(getField(items.get(position), "food"));

                viewHolder.imageReject = convertView.findViewById(R.id.imageReject);
                viewHolder.imageAccept = convertView.findViewById(R.id.imageAccept);
                viewHolder.imageReady = convertView.findViewById(R.id.imageReady);

                // In the beginning, only REJECT and ACCEPT buttons available
                // If reject, entry is gone
                // If accept, entry is refreshed with only READY button
                if (getField(items.get(position), "status").equals("ACCEPT")) {
                    viewHolder.imageReject.setVisibility(View.GONE);
                    viewHolder.imageAccept.setVisibility(View.GONE);
                    viewHolder.imageReady.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }
                // click listener for remove button
                viewHolder.imageReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrder(getString(R.string.food_status_reject), items, position);
                    }
                });
                viewHolder.imageAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrder(getString(R.string.food_status_accept), items, position);
                    }
                });
                viewHolder.imageReady.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrder(getString(R.string.food_status_ready), items, position);
                    }
                });
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        private ImageView imageReject;
        private ImageView imageAccept;
        private ImageView imageReady;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateOrder(
            String updateType,
            ArrayList<DataSnapshot> foodOrders,
            Integer position
    ){
        final DataSnapshot foodOrder = foodOrders.get(position);
        dbDeliveryList.child(Objects.requireNonNull(foodOrder.getKey())).child("status").setValue(updateType);

        ConstraintLayout rootLayout = findViewById(R.id.mainActivityRootLayout);
        String message = getField(foodOrder, "food") + " status set to " + updateType;
        Snackbar snackbar = Snackbar
                .make(rootLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateStatus(dbDeliveryList, foodOrder);
                    }
                });
        snackbar.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateStatus(DatabaseReference dbDeliveryList, DataSnapshot foodOrder) {
        String key = Objects.requireNonNull(foodOrder.getKey());
        dbDeliveryList.child(key).child("status").setValue(getField(foodOrder, "status"));
    }
}