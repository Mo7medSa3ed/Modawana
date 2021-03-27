package com.example.modawana;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Profile extends Fragment {
    TextView chip;
    TextView chip_word;

    Button btn;
    ListView list;
    adapter ad;
    ArrayList<items> fav_words = new ArrayList<>();
    int fav = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        final database db = new database(getActivity());
        fav_words = db.getAllfavouritewords();
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.profile_header, list, false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.profile_footer, list, false);
        list = (ListView) view.findViewById(R.id.fav_list);
        ImageView img =(ImageView)footer.findViewById(R.id.imageView12);
        TextView txt = (TextView) header.findViewById(R.id.textView42);
        btn = (Button) footer.findViewById(R.id.button5);
        chip = (TextView) header.findViewById(R.id.textView12);
        chip_word = (TextView) header.findViewById(R.id.textView38);
        fav = db.getAllfavouritewords().size();
        txt.setText(db.getusername());
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Details()).commit();

            }
        });


        if (db.getgrammer().size() > 0) {
            String d = db.getgrammer().get(0).created_at;
            String s = d.substring(0, 10);

            chip.setText(s);
        } else {
            chip.setText("");
        }

        if (fav == 0) {
            chip_word.setText("لا يوجد كلمات فى قاموسك:");
        } else {
            chip_word.setText("يوجد " + String.valueOf(fav) + "  كلمات فى قاموسك:");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }

        });

        list.addHeaderView(header);
        list.addFooterView(footer);
        ad = new adapter(getActivity(), fav_words);
        list.setAdapter(ad);


    }

    public void dialog() {
        final database db = new database(getActivity());
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Log out");
        dialog.setMessage("Are you sure log out really");
        dialog.setIcon(R.drawable.ic_error_outline_black_24dp);
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.Delete("Users");
                Intent intent = new Intent(getActivity(), login_activity.class);
                startActivity(intent);
                
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }


    class adapter extends BaseAdapter {

        Context context;
        ArrayList<items> list2 = new ArrayList<items>();

        public adapter(Context context, ArrayList<items> list2) {
            this.context = context;
            this.list2 = list2;
        }

        public void addlistitemtoadapter(ArrayList<items> list2) {
            this.list2 = list2;
            this.notifyDataSetChanged();
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
            final database db = new database(getActivity());
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = convertView;
            if (v == null) {
                v = layoutInflater.inflate(R.layout.shap_words, null, false);
            }

            final TextView ar_word = (TextView) v.findViewById(R.id.textView24);
            final TextView en_word = (TextView) v.findViewById(R.id.textView22);
            final TextView ar_mean = (TextView) v.findViewById(R.id.textView25);
            final TextView en_mean = (TextView) v.findViewById(R.id.textView23);
            final CheckBox ck = (CheckBox) v.findViewById(R.id.checkBox);


            ar_word.setText(list2.get(position).ar_word);
            en_word.setText(list2.get(position).en_word);
            ar_mean.setText(list2.get(position).ar_mean);
            en_mean.setText(list2.get(position).en_mean);
            ck.setChecked(true);
            ck.setClickable(false);


            return v;
        }
    }
}