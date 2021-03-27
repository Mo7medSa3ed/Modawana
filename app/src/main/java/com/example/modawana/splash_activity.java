package com.example.modawana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class splash_activity extends AppCompatActivity {

    database db = new database(this);
    int user_test=0;
    api_requests requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
         requests = retrofit.create(api_requests.class);

        ImageView img = (ImageView) findViewById(R.id.imageView);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);
        img.setAnimation(anim);


        user_test = db.getAllData().size();


        if (user_test==0){

            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){

                get_grammer_list();
                get_words();
                thread_splash2();
            }else{
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }

        }else {

            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {



                get_grammer_list();
                get_words();
                thread_splash();
            }else {
                thread_splash();
            }
        }

    }




    public void get_words(){
        Call<List<items>> call = requests.getwords();
        call.enqueue(new Callback<List<items>>() {
            @Override
            public void onResponse(Call<List<items>> call, Response<List<items>> response) {
                if (!response.isSuccessful()){
                   // Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_SHORT).show();
                    return;}
                db.Delete("Words");
                List<items> words = response.body();
                for (items i : words){
                    items word = new items(i.getAr_word(),i.getEn_word(),i.getAr_mean(),i.getEn_mean(),i.get_id());
                    db.insert_words(word);
                }
            }
            @Override
            public void onFailure(Call<List<items>> call, Throwable t) {
               // Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void get_grammer_list(){
        Call<List<Get_Grammer>> call = requests.get_grammer();
        call.enqueue(new Callback<List<Get_Grammer>>() {
            @Override
            public void onResponse(Call<List<Get_Grammer>> call, Response<List<Get_Grammer>> response) {
                if (!response.isSuccessful()){
                  //  Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_SHORT).show();
                    return;}

                db.Delete("Grammers");
                db.Delete("comments");
                db.Delete("replies");

                List<Get_Grammer> grammers = response.body();

                for (Get_Grammer i : grammers){
                    Get_Grammer grammer = new Get_Grammer(i.get_id(),i.getText(),i.getTitle(),i.getAuthor(),i.getCreated_at(),i.getUpdatedAt());
                    db.insert_grammer(grammer);

                    if (i.getComments().size() > 0 ){
                    for (int x=0; x< i.getComments().size();x++) {
                        comments_items comments_items = new comments_items(i.getComments().get(x).getName(), i.getComments().get(x).getDate(), i.getComments().get(x).getContent(), i.getComments().get(x).getId());
                        db.insert_comment(comments_items,i.get_id());
                       if (i.getComments().get(x).getReplies().size() > 0) {
                           for (int s = 0; s < i.getComments().get(x).getReplies().size(); s++) {
                               reply_item reply_item = new reply_item(i.getComments().get(x).getReplies().get(s).getName(), i.getComments().get(x).getReplies().get(s).getContent(), i.getComments().get(x).getReplies().get(s).getDate());
                               db.insert_reply(reply_item,i.getComments().get(x).getId());
                           }

                       }
                    }
                    }
                    }
            }
            @Override
            public void onFailure(Call<List<Get_Grammer>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){

               // Toast.makeText(getApplicationContext(), "Socket Time out. Please try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void thread_splash (){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();

    }
    public void thread_splash2 (){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),login_activity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }


}
