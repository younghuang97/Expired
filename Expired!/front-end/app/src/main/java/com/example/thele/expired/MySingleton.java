package com.example.thele.expired;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by thele on 4/24/2017.
 * Enables RequestQueue object to last lifetime of the application
 */

public class MySingleton {

    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context mCtx)
    {
        context = mCtx;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context mCtx)
    {
        if (instance == null)
        {
            instance = new MySingleton(mCtx);
        }
        return instance;
    }

    public<T> void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }
}
