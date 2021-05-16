package com.example.yatra.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.yatra.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    SharedPreferences sharedPref;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Our images are in 2:1 format so we will be getting device width
        sharedPref = this.getActivity().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        int height = sharedPref.getInt("device_width", 600);

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height / 2;
        Log.d("Slider height: ", String.valueOf(height));
        imageSlider.setLayoutParams(layoutParams);

//        ScrollView scrollView = view.findViewById(R.id.homescrollView);
//        FrameLayout.LayoutParams layoutParams2 =  (FrameLayout.LayoutParams) scrollView.getLayoutParams();
//        layoutParams2.setMargins(0, 540, 0, 0);
//        scrollView.setLayoutParams(layoutParams2);


        List<SlideModel> slideModelList = new ArrayList<>();
        slideModelList.add(new SlideModel(R.drawable.bus0, ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.bus1, ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.bus2, ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.bus3, ScaleTypes.CENTER_CROP));
        slideModelList.add(new SlideModel(R.drawable.bus4, "Yatra  -by Amgai Software's", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModelList);
        // Inflate the layout for this fragment
        return view;
    }


}