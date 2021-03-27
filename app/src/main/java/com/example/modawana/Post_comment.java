package com.example.modawana;

import java.util.ArrayList;

public class Post_comment {

   public comments_items comment;
   public String article_id;

    public ArrayList<comments_items> comments ;
    public String _id;
    public String text;
    public String title;
    public String author;
    public String created_at;
    public String updatedAt;

    public ArrayList<comments_items> getComments() {
        return comments;
    }

    public Post_comment(String article_id, comments_items comment) {
        this.article_id = article_id;
        this.comment = comment;
    }
}
