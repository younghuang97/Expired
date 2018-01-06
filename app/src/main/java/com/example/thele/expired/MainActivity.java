package com.example.thele.expired;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Toolbar myToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Item> myDataset;
    String fontPath = "fonts/indie_flower.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // makes it so button doesn't get pushed up when focused in on searching items
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // sets up custom fonts for textviews
        Helper.setCustomFont(this, R.id.itemName, fontPath);
        Helper.setCustomFont(this, R.id.purchaseDate, fontPath);
        Helper.setCustomFont(this, R.id.expirationDate, fontPath);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Fridge.getFridge().readDatabase(this);
        Fridge.getFridge().readList(this);

        //Fridge.getFridge().printDatabase();

        // initialize items for testing
        /*Item item = new Item("Carrot", "Fridge");
        Item item2 = new Item("beef", "Freezer");

        Fridge.getFridge().addItem(item);
        Fridge.getFridge().addItem(item2);
        */
        //Fridge.getFridge().writeDatabase(this);
        //Fridge.getFridge().printDatePurchased(3);

        myDataset = Fridge.getFridge().returnDateExpired();

        mAdapter = new MainAdapter(myDataset, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        // lines between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    /*
    On add button press, goes to AddItemActivity
     */
    public void goToAddItemActivity(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter = new MainAdapter(Helper.filterItems(myDataset, newText), MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent1 = new Intent(this, DBActivity.class);
                startActivity(intent1);
                return true;
            case R.id.item_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    Returning to MainActivity from another Activity refreshes the screen to reflect new data
    TODO: Make it more scalable
     */
    @Override
    public void onResume()
    {
        super.onResume();
        List<Item> myDataset = Fridge.getFridge().returnDateExpired();
        mAdapter = new MainAdapter(myDataset, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
