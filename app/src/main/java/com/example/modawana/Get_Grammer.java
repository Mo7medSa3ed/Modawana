package com.example.modawana;


import java.util.ArrayList;

public class Get_Grammer {




    public ArrayList<comments_items> comments ;
    public String _id;
    public String text;
    public String title;
    public String author;
    public String created_at;
    public String updatedAt;




    public Get_Grammer(String _id) {
        this._id = _id;
    }

    public Get_Grammer(String _id, String text, String title, String author, String created_at, String updatedAt) {
        this._id = _id;
        this.text = text;
        this.title = title;
        this.author = author;
        this.created_at = created_at;
        this.updatedAt = updatedAt;
    }
    public Get_Grammer( String text, String title, String author, String created_at, String updatedAt) {

        this.text = text;
        this.title = title;
        this.author = author;
        this.created_at = created_at;
        this.updatedAt = updatedAt;
    }

    public Get_Grammer(String index ,String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ArrayList<comments_items> getComments() {
        return comments;
    }

}