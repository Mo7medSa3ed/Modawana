package com.example.modawana;

import java.util.ArrayList;

public class comments_items {

    public String name;
    public String date;
    public String content;
    public String _id ;
    public ArrayList<reply_item> replies;


    public comments_items(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public comments_items(String name, String date, String content, String id ) {
        this.name = name;
        this.date = date;
        this.content = content;
        this._id = id;
    }

    public comments_items(String name, String content, String date) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return _id;
    }

    public ArrayList<reply_item> getReplies() {
        return replies;
    }
}
