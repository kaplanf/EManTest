package com.kaplan.emantest.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaplan.emantest.EManTestApplication;
import com.kaplan.emantest.R;
import com.kaplan.emantest.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ImageListFragment extends Fragment {

    private ListView imageList;
    private String TAG = "ImageListFragment";
    private ProgressDialog pDialog;
    private ArrayList<Image> imageArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_image_list, container, false);
        imageList = (ListView) v.findViewById(R.id.imageList);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Images...");
        pDialog.show();


        JsonObjectRequest flickrRequest = new JsonObjectRequest(Request.Method.GET,
                EManTestApplication.getInstance().getFlickrCall(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        JSONObject jsonObjectPhoto = new JSONObject();
                        try {
                            jsonObjectPhoto = response.getJSONObject("photos");
                            JSONArray jsonArrayPhotos = jsonObjectPhoto.getJSONArray("photo");
                            imageArrayList = new ArrayList<Image>();
                            int length = jsonArrayPhotos.length();
                            for (int a = 0; a < length; a++) {

                                JSONObject jsonObject = jsonArrayPhotos.getJSONObject(a);
                                Image image = new Image();
                                image.id = jsonObject.getString("id");
                                image.owner = jsonObject.getString("owner");
                                image.farm = jsonObject.getInt("farm");
                                image.secret = jsonObject.getString("secret");
                                image.title = jsonObject.getString("title");
                                image.server = jsonObject.getString("server");
                                imageArrayList.add(image);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

        EManTestApplication.getInstance().addToRequestQueue(flickrRequest, TAG);

        return v;
    }


}
