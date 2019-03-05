package com.omralcorut.orderfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    MenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<Menu> menuArrayList = new ArrayList<Menu>();

        //add menus
        menuArrayList.add(new Menu("Hot Drinks",R.drawable.hot_drinks,R.raw.hot_drinks));
        menuArrayList.add(new Menu("Cold Drinks",R.drawable.cold_drinks,R.raw.cold_drinks));
        menuArrayList.add(new Menu("Breakfast",R.drawable.breakfast,R.raw.breakfast));
        menuArrayList.add(new Menu("Desserts",R.drawable.snacks,R.raw.snacks));
        menuArrayList.add(new Menu("Main Menu",R.drawable.main_menu,R.raw.main_menu));

        //Create list view with custom adapter
        ListView menuListView = (ListView) findViewById(R.id.list);
        mAdapter = new MenuAdapter(this, menuArrayList);
        menuListView.setAdapter(mAdapter);

        //When click list item, show foods
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toFood = new Intent(MenuActivity.this, FoodActivity.class);
                toFood.putExtra("currentMenu", menuArrayList.get(i).getContent());
                toFood.putExtra("nameOfMenu", menuArrayList.get(i).getBaslik());
                startActivity(toFood);
            }
        });

    }

    //this is back button at actionbar
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
}
