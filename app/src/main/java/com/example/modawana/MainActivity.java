package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity {
    int []photos={R.drawable.p1,R.drawable.p4,R.drawable.p2};
    String title[]={"تكوين قاموسك الخاص","قراءة الدروس","حل تمارين"};
    Button btn;
    Button img1;
    Button img2;
    Button img3;
    ImageView imageView;
    TextView txt;
    TextView text;
    int i=0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView4);
        txt =(TextView)findViewById(R.id.textView_car);
        text =(TextView)findViewById(R.id.text);
        btn = (Button)findViewById(R.id.button2);
        img1 = (Button)findViewById(R.id.button8);
        img2 = (Button)findViewById(R.id.button7);
        img3 = (Button)findViewById(R.id.button6);
        imageView.setImageResource(photos[i]);
        txt.setText(title[i]);
        img1.setBackground(getResources().getDrawable(R.drawable.dots_selected));
        i=1;
        img1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                i=0;
                imageView.setImageResource(photos[i]);
                txt.setText(title[i]);
                btn.setText("التالى");
                img1.setBackground(getResources().getDrawable(R.drawable.dots_selected));
                img2.setBackground(getResources().getDrawable(R.drawable.dots));
                img3.setBackground(getResources().getDrawable(R.drawable.dots));
                text.setVisibility(View.VISIBLE);
                i++;
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                i=1;
                imageView.setImageResource(photos[i]);
                txt.setText(title[i]);
                btn.setText("التالى");
                img2.setBackground(getResources().getDrawable(R.drawable.dots_selected));
                img1.setBackground(getResources().getDrawable(R.drawable.dots));
                img3.setBackground(getResources().getDrawable(R.drawable.dots));
                text.setVisibility(View.VISIBLE);
                i++;
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                    i=2;
                    imageView.setImageResource(photos[i]);
                    txt.setText(title[i]);
                    btn.setText("ابدأ الآن");
                    img3.setBackground(getResources().getDrawable(R.drawable.dots_selected));
                    img1.setBackground(getResources().getDrawable(R.drawable.dots));
                    img2.setBackground(getResources().getDrawable(R.drawable.dots));
                    text.setVisibility(View.INVISIBLE);
                    i++;
            }
        });


        btn.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_defult));
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_press));
                }

                return false;
            }
        });

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (i==0){
            imageView.setImageResource(photos[i]);
            txt.setText(title[i]);
            btn.setText("التالى");
            text.setVisibility(View.VISIBLE);
            img2.setBackground(getResources().getDrawable(R.drawable.dots_selected));
            img1.setBackground(getResources().getDrawable(R.drawable.dots));
            img3.setBackground(getResources().getDrawable(R.drawable.dots));
        }else if (i==1){
            imageView.setImageResource(photos[i]);
            txt.setText(title[i]);
            btn.setText("التالى");
            text.setVisibility(View.VISIBLE);
            img2.setBackground(getResources().getDrawable(R.drawable.dots_selected));
            img1.setBackground(getResources().getDrawable(R.drawable.dots));
            img3.setBackground(getResources().getDrawable(R.drawable.dots));
        } else if (i==2){
            text.setVisibility(View.INVISIBLE);
            imageView.setImageResource(photos[i]);
            txt.setText(title[i]);
            btn.setText("ابدأ الآن");
            img3.setBackground(getResources().getDrawable(R.drawable.dots_selected));
            img1.setBackground(getResources().getDrawable(R.drawable.dots));
            img2.setBackground(getResources().getDrawable(R.drawable.dots));
        }else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }

        i++;
    }
});

    text.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
    });

    }

    }