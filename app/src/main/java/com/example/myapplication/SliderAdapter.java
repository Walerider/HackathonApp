package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;
public class SliderAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<SliderData> sliderList;
    private Button btnSkip;

    public SliderAdapter(Context context, ArrayList<SliderData> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
        this.btnSkip = btnSkip;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_item, container, false);

        ImageView imageView = view.findViewById(R.id.idIVSlider);

        SliderData sliderData = sliderList.get(position);

        imageView.setImageResource(sliderData.slideImage);

        container.addView(view);

        if (position == getCount() - 1) {
            btnSkip.setVisibility(View.VISIBLE);
        } else {
            btnSkip.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}