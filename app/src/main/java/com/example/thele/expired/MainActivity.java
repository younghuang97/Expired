package com.example.thele.expired;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Add all polls in ref as rows
    /*myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            for (DataSnapshot child : snapshot.getChildren()) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    });
*/
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

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("expirationDates");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String expirationDate = itemSnapshot.getValue(String.class);
                    Log.d(TAG, itemSnapshot.getKey() + " = " + expirationDate);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

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

        // Initialize expiration dates from local db
        Fridge.getFridge().addExpDatesFromJSON(this);
        Fridge.getFridge().printDatabase();

        /*Item item = new Item("Carrot", "Fridge");
        Item item2 = new Item("beef", "Freezer");

        Fridge.getFridge().addItem(item);
        Fridge.getFridge().addItem(item2);
        */
        Fridge.getFridge().printDatePurchased(3);

        //Fridge.getFridge().writeDatabase(this);
        Fridge.getFridge().updateFridge(this);

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
