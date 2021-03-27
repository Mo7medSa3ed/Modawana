package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class Article extends Fragment {
    TextView txt_title;
    TextView txt_authar;
    TextView txt_date;
    TextView txt_content;
    Button add_com;
    EditText edit;
    ArrayList<Get_Grammer> data =new ArrayList<>();
    ListView list;
    ArrayList<comments_items> comment =new ArrayList<>();
    adapter ad;
    String id;
    ConnectivityManager connectivityManager;
    api_requests requests;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    int postion=1;
    TextView com;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_article,container,false);

        Bundle bundle =this.getArguments();
        postion= bundle.getInt("position",1);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       final database db = new database(getActivity());
        data = db.getgrammer2(postion);
        id = db.getgrammer().get(postion-1).get_id();
        comment = db.getAllcomments(id);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        list=(ListView)view.findViewById(R.id.list);
        LayoutInflater inflater = getLayoutInflater();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://peaceful-lowlands-63106.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        requests = retrofit.create(api_requests.class);

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.article_header, list, false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.article_footer, list, false);

        txt_title=(TextView)header.findViewById(R.id.textView27);
        txt_authar=(TextView)header.findViewById(R.id.textView28);
        txt_date=(TextView)header.findViewById(R.id.textView29);
        txt_content=(TextView)header.findViewById(R.id.textView26);
        add_com = (Button)footer.findViewById(R.id.button4);
        edit = (EditText)footer.findViewById(R.id.editText4);
        com = (TextView)header.findViewById(R.id.text_comment);

        txt_title.setText(data.get(0).getTitle());
        txt_title.setPaintFlags(txt_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_authar.setText(data.get(0).getAuthor());
        if (comment.size()==0){
            com.setVisibility(View.VISIBLE);
        }else {
            com.setVisibility(View.GONE);
        }
        String d = data.get(0).getCreated_at();
        String s =d.substring(0,10);
        txt_date.setText(s);

        txt_content.setText(Html.fromHtml(data.get(0).getText(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                String img = source.substring(source.indexOf(",")+1,source.indexOf("="));
                LevelListDrawable d = new LevelListDrawable();
                byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Drawable empty = new BitmapDrawable(getResources(), decodedByte);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                return d;

            }
        }, null));




        list.addHeaderView(header);
        list.addFooterView(footer);
        ad = new adapter(getActivity(),comment);
        list.setAdapter(ad);

        add_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    final String edit_text = edit.getText().toString();
                    final String name = db.getusername();
                    if (edit_text.isEmpty() || edit_text.trim().equals("")) {
                        Toast.makeText(getActivity(), "No Comment Writer", Toast.LENGTH_SHORT).show();
                    } else {
                        final Date date = new Date();
                        final comments_items comments_items = new comments_items(name, edit_text, dateFormat.format(date));
                        Post_comment post_comment = new Post_comment(id, comments_items);
                        Call<Post_comment> call = requests.post_comment(post_comment);
                        call.enqueue(new Callback<Post_comment>() {
                            @Override
                            public void onResponse(Call<Post_comment> call, Response<Post_comment> response) {
                                if (!(response.isSuccessful())) {
                                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                                }
                                com.setVisibility(View.GONE);
                                Post_comment p = response.body();
                                int size = p.getComments().size();
                                String _id = p.getComments().get(size - 1).getId();

                                comments_items c = new comments_items(name, dateFormat.format(date), edit_text, _id);
                                db.insert_comment(c, id);
                                comment = db.getAllcomments(id);
                                ad.add_comment(comment);
                                edit.setText("");
                            }

                            @Override
                            public void onFailure(Call<Post_comment> call, Throwable t) {
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else {
                    Toast.makeText(getActivity(),"No Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }


    public class adapter extends BaseAdapter{

        public Context context;
        ArrayList<comments_items> list =new ArrayList<>();

        public adapter(Context context, ArrayList<comments_items> list) {
            this.context = context;
            this.list = list;
        }

        public void add_comment(ArrayList<comments_items> list){
            this.list=list;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            database db = new database(getActivity());
            LayoutInflater layoutInflater =getLayoutInflater();
            View v = convertView;
            if (v==null){
                v = layoutInflater.inflate(R.layout.article_item,null,false);
            }
            final TextView writer = (TextView)v.findViewById(R.id.textView39);
            final TextView post = (TextView)v.findViewById(R.id.textView33);
            TextView no_reply = (TextView)v.findViewById(R.id.textView35);
            TextView no = (TextView)v.findViewById(R.id.textView31);
            ImageView img = (ImageView) v.findViewById(R.id.imageButton2);



            Date data = new Date();
            writer.setText(list.get(position).getName());
            no.setText(dateFormat.format(data));
            post.setText(list.get(position).getContent());
            no_reply.setText( db.getAllreplies(db.getAllcomments(id).get(position).getId()).size()+" ردود");





           img.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String sendname = writer.getText().toString();
                   String sendcontent = post.getText().toString();
                   Intent intent = new Intent(getActivity(),Comments.class);
                   intent.putExtra("sendname",sendname);
                   intent.putExtra("sendcontent",sendcontent);
                   intent.putExtra("id",id);
                   intent.putExtra("pos",position);
                   startActivity(intent);
               }
           });



            return v;
        }
    }


}
