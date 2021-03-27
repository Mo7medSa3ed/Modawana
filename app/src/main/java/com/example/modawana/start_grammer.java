package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class start_grammer extends AppCompatActivity {
    Button btn;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_grammer);


        btn = (Button)findViewById(R.id.button3);


        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_defult));
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_press));
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

}