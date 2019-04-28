package com.omralcorut.jiankun;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

public class FoodStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_status);
        final ListView listView = findViewById(R.id.listView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("delivery_list");
        final CardView cardView = findViewById(R.id.activityMainCardView);
        final TextView textView = findViewById(R.id.activityMainTextView);


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
                    if(getField(item_snapshot, "table").equals(String.valueOf(MainActivity.tableNumber))){
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
                viewHolder.status = convertView.findViewById(R.id.status);
                viewHolder.status.setText(getField(foodOrders.get(position), "status"));




            }
            return convertView;
        }
    }

    private static class ViewHolder {
        private TextView status;
    }

}