package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;

public class SliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderData> sliderList;
    private TextView indicatorSlideOneTV;
    private TextView indicatorSlideTwoTV;
    private TextView indicatorSlideThreeTV;
    private TextView indicatorSlideFourTV;
    private Button btnSkip;
    private TextView slideRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        viewPager = findViewById(R.id.idViewPager);
        indicatorSlideOneTV = findViewById(R.id.idTVSlideOne);
        indicatorSlideTwoTV = findViewById(R.id.idTVSlideTwo);
        indicatorSlideThreeTV = findViewById(R.id.idTVSlideThree);
        indicatorSlideFourTV = findViewById(R.id.idTVSlideFour);
        btnSkip = findViewById(R.id.btnSkip);

        slideRight = findViewById(R.id.idTVSliderDescription);

        sliderList = new ArrayList<>();
        sliderList.add(new SliderData(R.drawable.first_slider));
        sliderList.add(new SliderData(R.drawable.second_slider));
        sliderList.add(new SliderData(R.drawable.third_slider));
        sliderList.add(new SliderData(R.drawable.fourth));

        sliderAdapter = new SliderAdapter(this, sliderList, new SliderAdapter.OnSlideChangeListener() {
            @Override
            public void onSlideChanged(int position) {
                if (position == sliderList.size()) {
                    btnSkip.setVisibility(View.VISIBLE);
                } else {
                    btnSkip.setVisibility(View.GONE);
                }
            }
        });
        viewPager.setAdapter(sliderAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        btnSkip.setVisibility(View.GONE);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                indicatorSlideTwoTV.setTextColor(getResources().getColor(R.color.sliderTextGrey));
                indicatorSlideThreeTV.setTextColor(getResources().getColor(R.color.sliderTextGrey));
                indicatorSlideFourTV.setTextColor(getResources().getColor(R.color.sliderTextGrey));
                indicatorSlideOneTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                btnSkip.setVisibility(View.GONE);
            } else if (position == 1) {
                indicatorSlideOneTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                indicatorSlideThreeTV.setTextColor(getResources().getColor(R.color.sliderTextGrey));
                indicatorSlideFourTV.setTextColor(getResources().getColor(R.color.sliderTextGrey));
                indicatorSlideTwoTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                btnSkip.setVisibility(View.GONE);
            } else if (position == 2) {
                indicatorSlideOneTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                indicatorSlideTwoTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                indicatorSlideFourTV.setTextColor(getResources().getColor(R.color.sliderTextGrey));
                indicatorSlideThreeTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                btnSkip.setVisibility(View.GONE);
            } else if (position == 3) {
                indicatorSlideOneTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                indicatorSlideTwoTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                indicatorSlideThreeTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                indicatorSlideFourTV.setTextColor(getResources().getColor(R.color.sliderBlue));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}
