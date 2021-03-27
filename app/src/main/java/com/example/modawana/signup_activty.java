package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
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

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signup_activty extends AppCompatActivity {
    EditText e1;
    EditText e2;
    EditText e3;
    TextView txt;
    Button btn;
    database db = new database(this);
    api_requests requests;

    String name="";
    String email="";
    String pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activty);

        Gson gson=new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/").
                addConverterFactory(GsonConverterFactory.create(gson)).build();
        requests =retrofit.create(api_requests.class);

        e1 = (EditText)findViewById(R.id.editText3);
        e2 = (EditText)findViewById(R.id.editText);
        e3 = (EditText)findViewById(R.id.editText2);
        txt=(TextView)findViewById(R.id.textView6);
        btn= (Button)findViewById(R.id.button);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login_activity.class);
                finish();
                startActivity(intent);
            }
        });
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_defult));
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(getResources().getColor(R.color.btn_press));
                }
                post_user();
                return false;
            }
        });



    }


public  void  post_user (){

     name = e1.getText().toString().trim();
     email = e2.getText().toString().trim().replace(" ","");
     pass = e3.getText().toString().trim().replace(" ","");

    Check_and_Sign(name,email,pass);

}

public void Check_and_Sign(String name ,String email , String pass){
    if (name.isEmpty()) {
        if (email.isEmpty()){
            if (pass.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Fill Name And Email And Password", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please Fill Name And Email", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Please Fill Name", Toast.LENGTH_SHORT).show();
        }

    }else if (pass.isEmpty()){
        if (name.isEmpty()){
            if (email.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Fill Name And Email And Password", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please Fill Name And Password", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Please Fill Password", Toast.LENGTH_SHORT).show();
        }
    }else if (email.isEmpty()) {
        if (pass.isEmpty()) {
            if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Fill Name And Email And Password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please Fill Email And Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Fill Email", Toast.LENGTH_SHORT).show();
        }

    }else {
        Sign_up(name,email,pass);
    }
}

public void Sign_up(final String name , final String email , final String pass){
    ApiClass apiClass = new ApiClass(name,email,pass);
    Call<ApiClass> call = requests.postusers(apiClass);
    call.enqueue(new Callback<ApiClass>() {
        @Override
        public void onResponse(Call<ApiClass> call, Response<ApiClass> response) {
            if (!response.isSuccessful()){
               // Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_SHORT).show();
                return;
            }
            db.insert_users(name,email,pass);
            Intent intent = new Intent(getApplicationContext(),start_grammer.class);
            startActivity(intent);
            finish();
        }
        @Override
        public void onFailure(Call<ApiClass> call, Throwable t) {
           // Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
        }
    });
    }

}