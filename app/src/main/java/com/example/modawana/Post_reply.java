package com.example.modawana;

public class Post_reply {
    public String article_id;
    public String comment_id;
    public reply_item reply;

    public Post_reply(String article_id, String comment_id, reply_item reply) {
        this.article_id = article_id;
        this.comment_id = comment_id;
        this.reply = reply;
    }
}
