package com.example.thele.expired;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains miscellaneous static helper methods
 */

public class Helper {

    // sets a custom font for elements given an id and a path to the font file
    protected static void setCustomFont(Context context, int id, String path) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), path);
        TextView itemName = (TextView)((Activity)context).findViewById(id);
        itemName.setTypeface(custom_font);
    }

    protected static List<Item> filterItems(List<Item> itemList, String query) {
        List<Item> filteredList = new ArrayList<>();
        if (query.isEmpty() || query == null) {
            filteredList.addAll(itemList);
        } else {
            for (Item item : itemList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        return filteredList;
    }

    protected static List<PairOfDates> filterPoDs(List<PairOfDates> itemList, String query) {
        List<PairOfDates> filteredList = new ArrayList<>();
        if (query.isEmpty() || query == null) {
            filteredList.addAll(itemList);
        } else {
            for (PairOfDates item : itemList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        return filteredList;
    }
}
