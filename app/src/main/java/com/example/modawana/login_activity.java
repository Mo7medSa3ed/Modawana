package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class login_activity extends AppCompatActivity {
    Button btn;
    EditText e1;
    EditText e2;
    api_requests requests;
    String[] word;
    ArrayList<items> dbAllwords;

    database db = new database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        btn = (Button) findViewById(R.id.button);
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);


        Gson gson=new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/").
                addConverterFactory(GsonConverterFactory.create(gson)).build();
        requests =retrofit.create(api_requests.class);


        TextView txt = (TextView) findViewById(R.id.textView6);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),signup_activty.class);
                startActivity(intent);
                finish();
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_defult));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_press));
                }
                Post_user();

                return false;
            }
        });

    }

    public void Post_user() {

        String email = e1.getText().toString().trim().replace(" ","");
        String pass = e2.getText().toString().trim().replace(" ","");

        if (email.isEmpty()) {
            if (pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter Email And Password", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
        }else if (pass.isEmpty()){
            if (email.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Email And Password", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ApiClass apiClass = new ApiClass(email, pass);
            Call<ApiClass> call = requests.login_user(apiClass);
            call.enqueue(new Callback<ApiClass>() {
                @Override
                public void onResponse(Call<ApiClass> call, Response<ApiClass> response) {
                    if (!response.isSuccessful()) {
                       if (response.code()!=200){
                           Toast.makeText(getApplicationContext(),"Login Error", Toast.LENGTH_SHORT).show();
                       }
                        return;
                    }



                    db.Delete("Words_Favourite");
                    ApiClass d = response.body();
                    db.insert_users(d.getName(),d.getEmail(),d.getPassword());


                    word = d.getBookmark().words;
                    dbAllwords =db.getAllwords();


                   if (word.length>0 && dbAllwords.size()>0) {
                       for (int i=0; i<word.length;i++){
                           for (int x=0;x<dbAllwords.size();x++){
                               if (word[i].equals(dbAllwords.get(x).get_id())){
                                   String a = dbAllwords.get(x).getAr_word();
                                   String b = dbAllwords.get(x).getEn_word();
                                   String c = dbAllwords.get(x).getAr_mean();
                                   String e = dbAllwords.get(x).getEn_mean();
                                   String f = dbAllwords.get(x).get_id();
                                   items g = new items(a,b,c,e,f);
                                   db.insert_words_favourite(g);
                               }
                           }
                       }
                   }
                    Intent i = new Intent(getApplicationContext(),start_grammer.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<ApiClass> call, Throwable t) {
                   Toast.makeText(getApplicationContext(),"Login Error", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}



