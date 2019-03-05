package com.omralcorut.orderfood;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by omral on 24.12.2016.
 */

//Food adapter for custom display food items.
public class FoodStatusAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<DataSnapshot> foodOrders;

    public FoodStatusAdapter(Context context, ArrayList<DataSnapshot> foodOrders) {
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
            convertView = inflater.inflate(R.layout.custom_bill, parent, false);


            viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.name.setText(getField(foodOrders.get(position), "food"));
            viewHolder.image.setImageResource(R.drawable.normal);
            if (getField(foodOrders.get(position), "status").equals("ACCEPT")) {
                viewHolder.status.setText("Order Accepted");
            } else if (getField(foodOrders.get(position), "status").equals("REJECT")) {
                viewHolder.status.setText("Order Rejected");
            } else if(getField(foodOrders.get(position), "status").equals("ACTIVE")) {
                viewHolder.status.setText("Order Sent");
            } else if (getField(foodOrders.get(position), "status").equals("READY")) {
                viewHolder.status.setText("Food being Delivered");
            }
        }
        return convertView;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getField(DataSnapshot dataSnapshot, String field) {
        // For convenience
        return Objects.requireNonNull(dataSnapshot.child(field).getValue()).toString();
    }

    private static class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView status;
    }
}




