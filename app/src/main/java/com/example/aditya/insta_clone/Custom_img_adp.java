package com.example.aditya.insta_clone;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 2017-12-30.
 */

public class Custom_img_adp extends ArrayAdapter<ImgPOJO> {

    ArrayList<ImgPOJO> lst1=new ArrayList<>();
    public Custom_img_adp(@NonNull Context context, int resource, @NonNull ArrayList<ImgPOJO> objects) {
        super(context, resource, objects);
        lst1=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.layout,null);

        ImageView img=(ImageView)v.findViewById(R.id.ref_imgvw);
        Bitmap ImageId=lst1.get(position).getImage();
        img.setImageBitmap(ImageId);

        return v;
    }
}
