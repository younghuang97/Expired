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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by younghuang97 on 12/27/17.
 */

public class DBActivity extends AppCompatActivity {
    private static final String TAG = "DBActivity";
    private Toolbar myToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<PairOfDates> myDataset;
    String fontPath = "fonts/indie_flower.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        // makes it so button doesn't get pushed up when focused in on searching items
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // sets up custom fonts for textviews

        Helper.setCustomFont(this, R.id.itemName, fontPath);
        Helper.setCustomFont(this, R.id.fridgeDate, fontPath);
        Helper.setCustomFont(this, R.id.freezerDate, fontPath);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myDataset = Fridge.getFridge().returnDatabase();

        mAdapter = new DBAdapter(myDataset, DBActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        // lines between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    /*
    On add button press, goes to AddItemActivity
     */
    public void goToAddDateActivity(View view) {
        Intent intent = new Intent(this, AddDateActivity.class);
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
                mAdapter = new DBAdapter(Helper.filterPoDs(myDataset, newText), DBActivity.this);
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
                return true;
            case R.id.item_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    Returning to DBActivity from another Activity refreshes the screen to reflect new data
     */
    @Override
    public void onResume()
    {
        super.onResume();
        List<PairOfDates> myDataset = Fridge.getFridge().returnDatabase();
        mAdapter = new DBAdapter(myDataset, DBActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }
}