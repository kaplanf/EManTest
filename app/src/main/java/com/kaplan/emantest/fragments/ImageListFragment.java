package com.kaplan.emantest.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaplan.emantest.EManTestApplication;
import com.kaplan.emantest.MainActivity;
import com.kaplan.emantest.R;
import com.kaplan.emantest.model.Image;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ImageListFragment extends Fragment {

    private ListView imageList;
    private String TAG = "ImageListFragment";
    private ProgressDialog pDialog;
    private ArrayList<Image> imageArrayList;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageDetailFragment imageDetailFragment;

    private MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_image_list, container, false);
        imageList = (ListView) v.findViewById(R.id.imageList);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setTitle("Images");


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

                        initializeList();
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


    public void initializeList() {

        ImageListAdapter adapter = new ImageListAdapter(getActivity(), imageArrayList);
        imageList.setAdapter(adapter);

    }


    private class ImageListAdapter extends ArrayAdapter<Image> {
        // View lookup cache
        private class ViewHolder {
            TextView title;
            ImageView image;
            RelativeLayout layout;
        }

        public ImageListAdapter(Context context, ArrayList<Image> imageArrayList) {
            super(context, R.layout.item_image, imageArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Image image = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.imageLayout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object
            viewHolder.title.setText(image.title);
            Picasso.with(getContext()).load(createFlickrImageURL(image)).into(viewHolder.image);
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageDetailFragment = new ImageDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image_url", createFlickrImageURL(image));
                    bundle.putString("title", image.title);
                    imageDetailFragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null).replace(R.id.content_frame, imageDetailFragment, null).commit();
                }
            });
            return convertView;
        }
    }

    public String createFlickrImageURL(Image image) {
        String url = "";
        url = "https://farm" + image.farm + "." + "staticflickr.com/" + image.server + "/" + image.id + "_" + image.secret + ".jpg";

        return url;
    }
}
