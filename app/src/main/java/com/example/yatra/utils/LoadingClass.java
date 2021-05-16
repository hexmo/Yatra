package com.example.yatra.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.yatra.R;


//https://www.youtube.com/watch?v=tccoRIrMyhU
public class LoadingClass {
    Activity activity;
    AlertDialog dialog;

    public LoadingClass(Activity activity) {
        this.activity = activity;

    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loader, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void stopLoadingDialog() {

        dialog.dismiss();
    }


}


