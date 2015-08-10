package com.kaplan.emantest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaplan.emantest.MainActivity;
import com.kaplan.emantest.R;
import com.squareup.picasso.Picasso;


public class ImageDetailFragment extends Fragment {

    private TextView title;
    private String image_url;
    private String image_title;
    private ImageView image;
    private Bundle bundle;
    private ActionBar actionBar;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_detail, container, false);

        title = (TextView) v.findViewById(R.id.imageDetailTitle);
        image = (ImageView) v.findViewById(R.id.imageDetail);
        activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setTitle("Image Detail");


        bundle = getArguments();
        if (bundle != null) {
            image_title = bundle.getString("title");
            image_url = bundle.getString("image_url");

            title.setText(image_title);
            Picasso.with(getActivity()).load(image_url).into(image);

        }

        return v;


    }


}
