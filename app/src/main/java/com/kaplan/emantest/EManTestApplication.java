package com.kaplan.emantest;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by kaplanfatt on 10/08/15.
 */
public class EManTestApplication extends Application {

    public static final String TAG = EManTestApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    private static String FLICKR_API_BASE = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=";
    private static String FLICKR_API_KEY = "e2b28abe57f4953a4e0aa7e69fba766b";
    private static String FLICKR_API_SUFFIX = "&format=json&nojsoncallback=1";
    private static String FLICKR_API_CALL = FLICKR_API_BASE + FLICKR_API_KEY + FLICKR_API_SUFFIX;

    private static EManTestApplication sharedInstance = new EManTestApplication();

    public static EManTestApplication getInstance() {
        return sharedInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;
    }


    public String getFlickrCall() {
        return FLICKR_API_CALL;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
