package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class    grammer_list extends Fragment {
    ArrayList<Get_Grammer> items2 = new ArrayList<Get_Grammer>();
    ArrayList<Get_Grammer> add =new ArrayList<>();
    ArrayList<Get_Grammer> add2 =new ArrayList<>();
    ArrayList<Get_Grammer> add3 =new ArrayList<>();
    FragmentManager fragmentManager;
    ListView list;
    Handler mHandler;
    Boolean isloading=false;
    adapter ad;
    ConnectivityManager connectivityManager;
    api_requests requests;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_grammer_list,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final database db = new database(getActivity());
        ArrayList<Get_Grammer> grammers =db.getgrammer();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        requests = retrofit.create(api_requests.class);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        mHandler = new myhandler();

        list=(ListView)view.findViewById(R.id.list);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup =(ViewGroup)inflater.inflate(R.layout.header2,list,false);
        ViewGroup viewGroup1=(ViewGroup)inflater.inflate(R.layout.footer,list,false);
        list.addHeaderView(viewGroup,null,false);
        list.addFooterView(viewGroup1,null,false);
        ImageView img = (ImageView)viewGroup1.findViewById(R.id.imageView12);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://peaceful-lowlands-63106.herokuapp.com"));
                startActivity(browser);
            }
        });

        int x=0;
        for (Get_Grammer i : grammers) {
            if (x>9){
                items2.add(new Get_Grammer(String.valueOf(x+1),i.getTitle()));
            }else {
                items2.add(new Get_Grammer("0"+String.valueOf(x+1),i.getTitle()));
            }
            x++;
        }
        ad= new adapter(getContext(), items2);
        list.setAdapter(ad);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("position",position);
                fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction ft=fragmentManager.beginTransaction();
                Article article = new Article();
                article.setArguments(b);
                ft.replace(R.id.fragment,article);
                ft.commit();
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount-1 && isloading ==false){
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED){
                    isloading =true;
                    Thread thread = new threadtogetmoredata();
                    thread.start();
                    }
                }
            }
        });




    }



    class adapter extends BaseAdapter {

        Context context;
        ArrayList<Get_Grammer> list2 = new ArrayList<Get_Grammer>();

        public adapter(Context context, ArrayList<Get_Grammer> list2) {
            this.context = context;
            this.list2 = list2;
        }

        public void addlistitemtoadapter( ArrayList<Get_Grammer> new_list) {
            if (new_list.size()>0){
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

            LayoutInflater layoutInflater = getLayoutInflater();
            View v = convertView;
            if (v==null){
                v = layoutInflater.inflate(R.layout.shape, null,false);
            }

            TextView index = (TextView) v.findViewById(R.id.textView20);
            TextView title = (TextView) v.findViewById(R.id.textView21);

            if (position+1>9){
                index.setText(String.valueOf(position+1));
                index.setPaintFlags(index.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }else {
                index.setText("0"+String.valueOf(position+1));
                index.setPaintFlags(index.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            title.setText(list2.get(position).getTitle());
            title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            return v;
        }
    }

    public void get_grammer_list(){
        final database db = new database(getActivity());
        Call<List<Get_Grammer>> call = requests.get_grammer();
        call.enqueue(new Callback<List<Get_Grammer>>() {
            @Override
            public void onResponse(Call<List<Get_Grammer>> call, Response<List<Get_Grammer>> response) {
                if (!response.isSuccessful()){
                   // Toast.makeText(getActivity(),response.code(),Toast.LENGTH_SHORT).show();
                    return;}
                add.clear();
                add2.clear();
                List<Get_Grammer> grammers = response.body();

                for (Get_Grammer i : grammers){
                    Get_Grammer grammer = new Get_Grammer(i.get_id(),i.getText(),i.getTitle(),i.getAuthor(),i.getCreated_at(),i.getUpdatedAt());
                    add.add(grammer);
                }
                int dbsize = db.getgrammer().size();
                int getsize = add.size();


               /* if (getsize == dbsize){
                    for (int x = 0; x < dbsize; x++){
                        if (!(add.get(x).getTitle().equals(db.getgrammer().get(x).getTitle()))) {
                            db.Delete("Grammers");
                            String id = add.get(x).get_id();
                            String text = add.get(x).getText();
                            String title = add.get(x).getTitle();
                            String author = add.get(x).getAuthor();
                            String crated = add.get(x).getCreated_at();
                            String updated = add.get(x).getUpdatedAt();
                            Get_Grammer grammer = new Get_Grammer(id,text,title,author,crated,updated);
                            db.insert_grammer(grammer);
                        }
                    }
                }*/
                if (getsize > dbsize){
                    for (int x = dbsize; x < getsize; x++){
                        String id = add.get(x).get_id();
                        String text = add.get(x).getText();
                        String title = add.get(x).getTitle();
                        String author = add.get(x).getAuthor();
                        String crated = add.get(x).getCreated_at();
                        String updated = add.get(x).getUpdatedAt();
                        Get_Grammer grammer = new Get_Grammer(id,text,title,author,crated,updated);
                        db.insert_grammer(grammer);
                        add2.add(grammer);

                    }

                }else if (getsize<dbsize){
                    add3=db.getgrammer();
                    add3.removeAll(add);
                    for (int x=0; x<add3.size(); x++){
                        String _No = add3.get(x)._id;
                        db.Delete_grammer(_No);
                    }

                }

            }
            @Override
            public void onFailure(Call<List<Get_Grammer>> call, Throwable t) {
               // Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
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
                    ad.addlistitemtoadapter((ArrayList<Get_Grammer>)msg.obj);
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

            mHandler.sendEmptyMessage(0);
            get_grammer_list();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = mHandler.obtainMessage(1,add2);
            mHandler.sendMessage(msg);

        }
    }


}
