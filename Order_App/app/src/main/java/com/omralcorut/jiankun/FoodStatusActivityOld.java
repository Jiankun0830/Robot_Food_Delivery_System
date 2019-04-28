package com.omralcorut.jiankun;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FoodStatusActivityOld extends AppCompatActivity {

    FoodAdapter mAdapter;
    DatabaseHandler db;
    TextView totalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_status);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //DB object
        db = new DatabaseHandler(this);

        totalCost = (TextView) findViewById(R.id.total_cost);
        totalCost.setText(db.billCost()+" SGD");

        final List<Food> bills = db.getAllBill();

        //Create list view with custom adapter
        final ListView billListView = (ListView) findViewById(R.id.list);
        mAdapter = new FoodAdapter(this, bills,2);
        billListView.setAdapter(mAdapter);
    }

    //Update list view
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    //pressed back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void askForCheck(View v) {
        if (db.getAllBill().isEmpty()) {
            Toast.makeText(FoodStatusActivityOld.this,"There is no bill.",Toast.LENGTH_LONG).show();
        }
        else {
            AlertDialog.Builder adb = new AlertDialog.Builder(FoodStatusActivityOld.this);
            adb.setTitle("Ask For Check?");
            adb.setMessage("Are you sure to ask for check?");
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    db.addHistory(new History("Asked for check for "+db.billCost()+" SGD",MainActivity.tableNumber+""));
                    db.deleteBills();
                    mAdapter.clear();
                    totalCost.setText(db.billCost()+" SGD");
                    Toast.makeText(FoodStatusActivityOld.this,"The request is sent!\nThank you for coming :)",Toast.LENGTH_LONG).show();
                }});
            adb.show();
        }
    }
}
