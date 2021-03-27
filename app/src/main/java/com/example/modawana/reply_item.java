package com.example.modawana;

public class reply_item {
    public String name;
    public String date;
    public String content;

    public reply_item(String name,  String content,String date) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

    public reply_item(String name, String content) {
        this.name = name;
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
}
