package com.example.nitsarut.mymap.prefs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nitsarut.mymap.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by Nitsarut on 2/24/2018.
 */

public class ViewPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.backhome1,R.drawable.accurate};
    private PhotoView photoView;




    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        photoView = (PhotoView)view.findViewById(R.id.photo_view1);
        photoView.setImageResource(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0){
                    Toast.makeText(context, "รูปที่ 1 ก่อนแก้ไข" , Toast.LENGTH_SHORT).show();
                } else if (position == 1){
                    Toast.makeText(context,  "รูปที่ 2 หลังแก้ไข" , Toast.LENGTH_SHORT).show();
                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view , 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
