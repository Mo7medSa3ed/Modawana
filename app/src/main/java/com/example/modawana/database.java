package com.example.modawana;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class database extends SQLiteOpenHelper {

    public static final String dbname = "Data";

    public database(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Users ( id INTEGER PRIMARY KEY AUTOINCREMENT ,Name TEXT ,Email TEXT , Password TEXT  )");
        db.execSQL("create table Words ( id INTEGER PRIMARY KEY AUTOINCREMENT ,ar_word TEXT ,ar_mean TEXT , en_word TEXT ,en_mean TEXT ,_id TEXT)");
        db.execSQL("create table Grammers ( id INTEGER PRIMARY KEY AUTOINCREMENT ,_id TEXT ,text LONGTEXT ,title TEXT, author TEXT,created_at TEXT ,updatedAt TEXT )");
        db.execSQL("create table Words_Favourite ( id INTEGER PRIMARY KEY AUTOINCREMENT ,ar_word TEXT ,ar_mean TEXT , en_word TEXT ,en_mean TEXT,_id TEXT )");
        db.execSQL("create table comments ( id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , date TEXT , content TEXT , _id TEXT , article_id TEXT)");
        db.execSQL("create table replies ( id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , date TEXT , content TEXT , reply_id TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Words");
        db.execSQL("DROP TABLE IF EXISTS Grammers");
        db.execSQL("DROP TABLE IF EXISTS Words_Favourite");
        db.execSQL("DROP TABLE IF EXISTS comments");
        db.execSQL("DROP TABLE IF EXISTS replies");
        onCreate(db);
    }

    public  Boolean insert_users (String Name , String Email,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",Name);
        contentValues.put("Email",Email);
        contentValues.put("Password",Password);
        Long result = db.insert("Users",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;

    }
    public  Boolean insert_words (items words){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ar_word",words.getAr_word());
        contentValues.put("ar_mean",words.getAr_mean());
        contentValues.put("en_word",words.getEn_word());
        contentValues.put("en_mean",words.getEn_mean());
        contentValues.put("_id",words.get_id());
        Long result = db.insert("Words",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;

    }
    public  Boolean insert_words_favourite (items words){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ar_word",words.getAr_word());
        contentValues.put("ar_mean",words.getAr_mean());
        contentValues.put("en_word",words.getEn_word());
        contentValues.put("en_mean",words.getEn_mean());
        contentValues.put("_id",words.get_id());
        Long result = db.insert("Words_Favourite",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;

    }
    public  Boolean insert_grammer (Get_Grammer grammer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id",grammer.get_id());
        contentValues.put("text",grammer.getText());
        contentValues.put("title",grammer.getTitle());
        contentValues.put("author",grammer.getAuthor());
        contentValues.put("created_at",grammer.getCreated_at());
        contentValues.put("updatedAt",grammer.getUpdatedAt());
        Long result = db.insert("Grammers",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;

    }
    public  Boolean insert_comment(comments_items items , String article_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",items.getName());
        contentValues.put("date",items.getDate());
        contentValues.put("content",items.getContent());
        contentValues.put("_id",items.getId());
        contentValues.put("article_id",article_id);
        Long result = db.insert("comments",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }
    public  Boolean insert_reply(reply_item reply_item , String reply_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",reply_item.getName());
        contentValues.put("date",reply_item.getDate());
        contentValues.put("content",reply_item.getContent());
        contentValues.put("reply_id",reply_id);
        Long result = db.insert("replies",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }



    public ArrayList getAllData(){
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Users",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String t1 = res.getString(0);
            String t2 = res.getString(1);
            String t3 = res.getString(2);
            String t4 = res.getString(3);

            arrayList.add(t1+"//"+t2+"//"+t3+"//"+t4);
            res.moveToNext();
        }
        return arrayList;
    }
    public String getusername(){

        String name="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Users",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
           name= res.getString(1);
            res.moveToNext();
        }
        return name;
    }
    public String getuseremail(){
        String name="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Users",null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            name= res.getString(2);
            res.moveToNext();
        }
        return name;
    }
    public ArrayList<items> getAllwords( ) {
        ArrayList<items> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Words", null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String ar_word = res.getString(1);
            String en_word = res.getString(3);
            String ar_mean = res.getString(2);
            String en_mean = res.getString(4);
            String _id = res.getString(5);
            items words = new items(ar_word,en_word,ar_mean,en_mean,_id);
            arrayList.add(words);
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<Get_Grammer> getgrammer( ) {
        ArrayList<Get_Grammer> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Grammers", null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String _id = res.getString(1);
            String ar_word = res.getString(2);
            String en_word = res.getString(3);
            String ar_mean = res.getString(4);
            String en_mean = res.getString(5);
            String en_mean2= res.getString(6);
            Get_Grammer words = new Get_Grammer(_id,ar_word,en_word,ar_mean,en_mean,en_mean2);
            arrayList.add(words);
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<Get_Grammer> getgrammer2(int id) {
        ArrayList<Get_Grammer> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Grammers WHERE id ="+id, null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String ar_word = res.getString(2);
            String en_word = res.getString(3);
            String ar_mean = res.getString(4);
            String en_mean = res.getString(5);
            String en_mean2= res.getString(6);
            Get_Grammer words = new Get_Grammer(ar_word,en_word,ar_mean,en_mean,en_mean2);
            arrayList.add(words);
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<items> getAllfavouritewords( ) {
        ArrayList<items> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Words_Favourite", null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String ar_word = res.getString(1);
            String en_word = res.getString(3);
            String ar_mean = res.getString(2);
            String en_mean = res.getString(4);
            String _id = res.getString(5);
            items words = new items(ar_word,en_word,ar_mean,en_mean,_id);
            arrayList.add(words);
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<comments_items> getAllcomments(String id){
        ArrayList<comments_items> arrayList =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from comments WHERE article_id ='"+id+"'", null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String name = res.getString(1);
            String date = res.getString(2);
            String content = res.getString(3);
            String _id = res.getString(4);
            comments_items items = new comments_items(name,date,content,_id);
            arrayList.add(items);
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<reply_item> getAllreplies(String id){
        ArrayList<reply_item> arrayList =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from replies WHERE reply_id ='"+id+"'", null);
        res.moveToFirst();
        while (res.isAfterLast()==false){
            String name = res.getString(1);
            String date = res.getString(2);
            String content = res.getString(3);
            reply_item items = new reply_item(name,date,content);
            arrayList.add(items);
            res.moveToNext();
        }
        return arrayList;
    }



    public void Delete(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + table_name + "'");
    }
    public void Delete_favorite_word(String _id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Words_Favourite", "_id=?",new String[]{_id} );
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Words_Favourite"+ "'");
    }
    public void Delete_word(String _id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Words", "_id=?",new String[]{_id} );
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Words"+ "'");
    }
    public void Delete_grammer(String _id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Grammers", "_id=?",new String[]{_id} );
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "Grammers"+ "'");
    }



    public Boolean isEmpty(String table_name){
        boolean empty = true;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM"+ table_name, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }



}

