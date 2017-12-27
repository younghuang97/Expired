package com.example.thele.expired;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

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
}
