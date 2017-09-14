package com.example.thele.expired;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Item> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // initialize variables for testing
        Fridge.getFridge().addExpDate(new PairOfDates(5, 16), "carrot");
        Fridge.getFridge().addExpDate(new PairOfDates(2, 7), "beef");
        Fridge.getFridge().addExpDate(new PairOfDates(2, 20), "cabbage");
        Fridge.getFridge().addExpDate(new PairOfDates(3, 30), "bread");
        Item item = new Item("Carrot", "Fridge");
        Item item2 = new Item("beef", "Freezer");
        Item item3 = new Item("LotIon", "Misc.");
        Fridge.getFridge().addItem(item);
        Fridge.getFridge().addItem(item2);
        Fridge.getFridge().printDatePurchased(3);

        myDataset = Fridge.getFridge().returnDateExpired();

        mAdapter = new MyAdapter(myDataset, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        // lines between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    /*
    On button press, goes to AddActivity
     */
    public void goToGetActivity(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
return false;
    }

    /*
    Returning to MainActivity from another Activity refreshes the screen to reflect new data
     */
    @Override
    public void onResume()
    {
        super.onResume();
        List<Item> myDataset = Fridge.getFridge().returnDateExpired();
        mAdapter = new MyAdapter(myDataset, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
