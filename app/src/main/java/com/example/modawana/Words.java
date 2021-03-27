package com.example.modawana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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


public class Words extends Fragment {
    ArrayList<items> items2 = new ArrayList<items>();
    ArrayList<items> add = new ArrayList<items>();
    ArrayList<items> add2 = new ArrayList<items>();
    ArrayList<items> add3 = new ArrayList<items>();
    ArrayList<items> word_fav = new ArrayList<items>();
    ArrayList<items> db_word =new ArrayList<>();
    ListView list;
    Handler mHandler;
    Boolean isloading=false;
    adapter ad;
    ConnectivityManager connectivityManager;
    int pos=0;
    api_requests requests;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final database db = new database(getActivity());
        ArrayList<items> words =db.getAllwords();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        requests = retrofit.create(api_requests.class);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        mHandler = new myhandler();

        list = (ListView) view.findViewById(R.id.grid);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, list, false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer, list, false);
        list.addHeaderView(header,null,false);
        list.addFooterView(footer,null,false);
        ImageView img =  (ImageView) footer.findViewById(R.id.imageView12);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://peaceful-lowlands-63106.herokuapp.com"));
                startActivity(browser);
            }
        });

        for (items i : words) {
            items2.add(new items(i.getAr_word(),i.getEn_word(),i.getAr_mean(),i.getEn_mean(),i.get_id()));
        }
        ad = new adapter(getContext(), items2);
        list.setAdapter(ad);


        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount-1&&list.getCount()>=db.getAllwords().size() && isloading ==false) {
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                        isloading = true;
                        Thread thread = new threadtogetmoredata();
                        thread.start();
                    }
                }
            }
        });


        }

    class adapter extends BaseAdapter {

        Context context;
        ArrayList<items> list2 = new ArrayList<items>();

        public adapter(Context context, ArrayList<items> list2) {
            this.context = context;
            this.list2 = list2;
        }

        public void addlistitemtoadapter( ArrayList<items> new_list) {

            if (new_list.size()>0) {
                for (int x = 0; x < new_list.size(); x++) {
                    this.list2.add(new_list.get(x));
                }
                this.notifyDataSetChanged();
            }


        }

        @Override
        public int getCount() {
            return list2.size();
        }

        @Override
        public Object getItem(int position) {
            return list2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final database db =new database(getActivity());
            LayoutInflater layoutInflater = getLayoutInflater();
             View v = convertView;
            if (v==null){
                v = layoutInflater.inflate(R.layout.shap_words, null,false);
            }

             final TextView ar_word = (TextView) v.findViewById(R.id.textView24);
             final TextView en_word = (TextView) v.findViewById(R.id.textView22);
             final TextView ar_mean = (TextView) v.findViewById(R.id.textView25);
             final TextView en_mean = (TextView) v.findViewById(R.id.textView23);
             final CheckBox ck =(CheckBox) v.findViewById(R.id.checkBox);

             word_fav = db.getAllfavouritewords();
             db_word = db.getAllwords();
            ar_word.setText(list2.get(position).ar_word);
            en_word.setText(list2.get(position).en_word);
            ar_mean.setText(list2.get(position).ar_mean);
            en_mean.setText(list2.get(position).en_mean);

            if (word_fav.size()>0){
                for (int x=0; x<word_fav.size(); x++){
                        if (db_word.get(position).get_id().equals(word_fav.get(x).get_id())){
                            ck.setChecked(true);
                        }
                }
            } else {
                ck.setChecked(false);
            }


            ck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;
                    if (!(ck.isChecked())){
                        ck.setChecked(false);
                        post();
                        db.Delete_favorite_word(db.getAllwords().get(position).get_id());
                    }else {
                        ck.setChecked(true);
                        post();
                        items item = new items(db_word.get(position).getAr_word(),db_word.get(position).getEn_word(),db_word.get(position).getAr_mean(),db_word.get(position).getEn_mean(),db_word.get(position).get_id());
                        db.insert_words_favourite(item);

                    }
                }
            });


            return v;
        }
    }


    public void insert_words(){
        final database db = new database(getActivity());
        Call<List<items>> call = requests.getwords();
        call.enqueue(new Callback<List<items>>() {
            @Override
            public void onResponse(Call<List<items>> call, Response<List<items>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(),response.code(),Toast.LENGTH_SHORT).show();
                    return;}

                add.clear();
                add2.clear();

                List<items> words = response.body();

                for (items i : words){
                    items word = new items(i.getAr_word(),i.getEn_word(),i.getAr_mean(),i.getEn_mean(),i._id);
                    add.add(word);
                }

                int dbsize = db.getAllwords().size();
                int getsize = add.size();


                /*if (getsize == dbsize){

                    for (int x = 0; x < dbsize; x++){
                        if (!(add.get(x).ar_word .equals(db.getAllwords().get(x).ar_word))){

                            db.Delete("Words");
                            String ar_word = add.get(x).ar_word;
                            String ar_mean = add.get(x).ar_mean;
                            String en_word = add.get(x).en_word;
                            String en_mean = add.get(x).en_mean;
                            String _No = add.get(x)._id;
                            items word = new items(ar_word,en_word,ar_mean,en_mean,_No);
                            db.insert_words(word);
                        }
                    }

                }*/
                if (getsize > dbsize){
                    for (int x = dbsize; x < getsize; x++){

                        String ar_word = add.get(x).ar_word;
                        String ar_mean = add.get(x).ar_mean;
                        String en_word = add.get(x).en_word;
                        String en_mean = add.get(x).en_mean;
                        String _No = add.get(x)._id;
                        items word = new items(ar_word,en_word,ar_mean,en_mean,_No);
                        db.insert_words(word);
                        add2.add(word);
                    }

                }else if (getsize<dbsize){
                    add3=db.getAllwords();
                    add3.removeAll(add);
                    for (int x=0; x<add3.size(); x++){
                        String _No = add3.get(x)._id;
                        db.Delete_word(_No);
                    }

                }





            }
            @Override
            public void onFailure(Call<List<items>> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class myhandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    ad.addlistitemtoadapter((ArrayList<items>)msg.obj);
                    isloading=false;
                    break;
                  default:
                      break;
            }
        }
    }

    public class threadtogetmoredata extends Thread{
        @Override
        public void run() {
            final database db = new database(getActivity());
            mHandler.sendEmptyMessage(0);
            insert_words();
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            Message msg = mHandler.obtainMessage(1,add2);
            mHandler.sendMessage(msg);

        }
    }

    public void post(){
        final database db= new database(getActivity());
        String id =db_word.get(pos).get_id();
        post_fav post =new post_fav(db.getuseremail(),id);
        Call<post_fav> call = requests.post_fav(post);
        call.enqueue(new Callback<post_fav>() {
            @Override
            public void onResponse(Call<post_fav> call, Response<post_fav> response) {
                if (!(response.isSuccessful())) {
                   // Toast.makeText(getActivity(),response.code(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<post_fav> call, Throwable t) {
              //  Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


}