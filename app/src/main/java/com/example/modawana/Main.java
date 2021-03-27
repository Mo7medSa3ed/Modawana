package com.example.modawana;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class Main extends Fragment {

    int photos[]={R.drawable.p1,R.drawable.p4,R.drawable.p2,R.drawable.p3};
    String title[]={"تكوين قاموسك الخاص ","قراءة الدروس","حل تمارين","مشاهدة الشرح"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CarouselView carouselView = (CarouselView)view.findViewById(R.id.car);
        final TextView txt = (TextView)view.findViewById(R.id.tex);
        ImageView img = (ImageView)view.findViewById(R.id.img_go);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(photos[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                txt.setText(title[position]);
            }
        });
        carouselView.setPageCount(title.length);


        carouselView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              txt.setText(title[position]);
          }

          @Override
          public void onPageSelected(int position) {
              txt.setText(title[position]);
          }

          @Override
          public void onPageScrollStateChanged(int state) {

          }
      });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW,Uri.parse("https://peaceful-lowlands-63106.herokuapp.com"));
                startActivity(browser);
            }
        });


    }
}