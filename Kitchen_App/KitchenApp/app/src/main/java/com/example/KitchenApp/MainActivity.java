package com.example.KitchenApp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = findViewById(R.id.listView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("delivery_list");
        final CardView cardView = findViewById(R.id.activityMainCardView);
        final TextView textView = findViewById(R.id.activityMainTextView);

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createExampleOrders(myRef);
                return false;
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<DataSnapshot> foodOrders = new ArrayList<>();
                CustomAdapter foodOrdersAdapter = new CustomAdapter(getApplicationContext(), foodOrders);
                listView.setEmptyView(findViewById(R.id.empty));
                listView.setAdapter(foodOrdersAdapter);

                // If status is "Ready" or "Rejected", we no longer want to display them
                // Also subtly prioritise accepted items on top
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    if (getField(item_snapshot, "status").equals("ACCEPT")) {
                        foodOrders.add(item_snapshot);
                    }
                }
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    if (getField(item_snapshot, "status").equals("ACTIVE")) {
                        foodOrders.add(item_snapshot);
                    }
                }
                if (foodOrders.isEmpty()) {
                    cardView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    cardView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
                foodOrdersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void createExampleOrders(DatabaseReference myRef) {
        // Create example order in firebase for demo purposes
        for (int i = 1; i <= 3; i++) {
            myRef.child("example" + i).child("status").setValue("ACTIVE");
            myRef.child("example" + i).child("food").setValue("Example Order " + i);
            myRef.child("example" + i).child("table").setValue(i);
            myRef.child("example" + i).child("timestamp").setValue("0000000000000");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getField(DataSnapshot dataSnapshot, String field) {
        // For convenience
        return Objects.requireNonNull(dataSnapshot.child(field).getValue()).toString();
    }

    class CustomAdapter extends BaseAdapter {
        // Based on http://www.prandroid.com/2016/03/dynamic-add-and-remove-item-on-listview.html
        final Context context;
        final ArrayList<DataSnapshot> foodOrders;
        CustomAdapter(Context context, ArrayList<DataSnapshot> foodOrders) {
            this.context = context;
            this.foodOrders = foodOrders;
        }
        @Override
        public int getCount() {
            return foodOrders.size();
        }
        @Override
        public Object getItem(int position) {
            return foodOrders.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                TextView tvName = convertView.findViewById(R.id.tvName);
                tvName.setText(getField(foodOrders.get(position), "food"));

                viewHolder.imageReject = convertView.findViewById(R.id.imageReject);
                viewHolder.imageAccept = convertView.findViewById(R.id.imageAccept);
                viewHolder.imageReady = convertView.findViewById(R.id.imageReady);

                // In the beginning, only REJECT and ACCEPT buttons available
                // If reject, entry is gone
                // If accept, entry is refreshed with only READY button
                if (getField(foodOrders.get(position), "status").equals("ACCEPT")) {
                    viewHolder.imageReject.setVisibility(View.GONE);
                    viewHolder.imageAccept.setVisibility(View.GONE);
                    viewHolder.imageReady.setVisibility(View.VISIBLE);
                }

                // click listener for remove button
                viewHolder.imageReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrder("REJECT", foodOrders, position);
                    }
                });
                viewHolder.imageAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrder("ACCEPT", foodOrders, position);
                    }
                });
                viewHolder.imageReady.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrder("READY", foodOrders, position);
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("delivery_list");
        final DataSnapshot foodOrder = foodOrders.get(position);
        myRef.child(Objects.requireNonNull(foodOrder.getKey())).child("status").setValue(updateType);

        ConstraintLayout rootLayout = findViewById(R.id.mainActivityRootLayout);
        String message = getField(foodOrder, "food") + " status set to " + updateType;
        Snackbar snackbar = Snackbar
                .make(rootLayout, message, Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateStatus(myRef, foodOrder);
                    }
                });
        snackbar.show();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateStatus(DatabaseReference myRef, DataSnapshot foodOrder) {
        String key = Objects.requireNonNull(foodOrder.getKey());
        myRef.child(key).child("status").setValue(getField(foodOrder, "status"));
    }
}