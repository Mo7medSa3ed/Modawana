package com.example.modawana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Comments extends AppCompatActivity {

    ListView list;
    EditText edit;
    ImageView send;
    Myadapter myadapter;
    int pos=0;
    ConnectivityManager connectivityManager;
    api_requests requests;
    database database = new database(this);
    ArrayList<reply_item> arrayList = new ArrayList<>();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        String sn = getIntent().getExtras().getString("sendname");
        String sc = getIntent().getExtras().getString("sendcontent");
        final String id = getIntent().getExtras().getString("id");
        pos = getIntent().getExtras().getInt("pos");
        arrayList = database.getAllreplies(database.getAllcomments(id).get(pos).getId());

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        requests = retrofit.create(api_requests.class);

        LayoutInflater layoutInflater = getLayoutInflater();
        list = (ListView) findViewById(R.id.list_comment);
        edit = (EditText) findViewById(R.id.editText5);
        send = (ImageView) findViewById(R.id.imageView13);
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.article_item2,list,false);
        TextView sendname = (TextView)header.findViewById(R.id.textView31);
        TextView sendcontent = (TextView)header.findViewById(R.id.textView33);
        TextView dat = (TextView)header.findViewById(R.id.textView44);
        Date date = new Date();
        sendname.setText(sn);
        sendcontent.setText(sc);
        dat.setText(dateFormat.format(date));
        list.addHeaderView(header);
        myadapter = new Myadapter(getApplicationContext(),arrayList);
        list.setAdapter(myadapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


                    final String name = database.getusername();
                    final String edit_text = edit.getText().toString();
                    if (edit_text.isEmpty()||edit_text.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "No Reply Writer", Toast.LENGTH_SHORT).show();
                    } else {
                        final Date date = new Date();
                        reply_item item = new reply_item(name, edit_text, dateFormat.format(date));
                        Post_reply reply = new Post_reply(id, database.getAllcomments(id).get(pos).getId(), item);
                        Call<Post_reply> call = requests.post_reply(reply);
                        call.enqueue(new Callback<Post_reply>() {
                            @Override
                            public void onResponse(Call<Post_reply> call, Response<Post_reply> response) {
                                if (!(response.isSuccessful())) {
                                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                                }

                                reply_item co = new reply_item(name, edit_text, id);
                                database.insert_reply(co, database.getAllcomments(id).get(pos).getId());
                                arrayList = database.getAllreplies(database.getAllcomments(id).get(pos).getId());
                                myadapter.addnew(arrayList);
                                list.setSelection(list.getCount());
                                edit.setText("");
                            }

                            @Override
                            public void onFailure(Call<Post_reply> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }else {
                    Toast.makeText(getApplicationContext(),"No Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class Myadapter extends BaseAdapter {
        Context context;
        ArrayList<reply_item> arrayList =new ArrayList<>();

        public Myadapter(Context context, ArrayList<reply_item> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public void addnew(ArrayList<reply_item> arrayList){
            this.arrayList=arrayList;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v =convertView;
            if (v==null){
                v= layoutInflater.inflate(R.layout.com_footer,null,false);
            }
            TextView send_name = (TextView)v.findViewById(R.id.textView31);
            TextView content = (TextView) v.findViewById(R.id.textView33);
            TextView dat = (TextView)v.findViewById(R.id.textView37);

            Date date = new Date();
            send_name.setText(arrayList.get(position).getName());
            content.setText(arrayList.get(position).getDate());
            dat.setText(dateFormat.format(date));

            return v;
        }
    }



}
