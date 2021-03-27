package com.example.modawana;

public class items {


    public String _id;
    public String ar_word;
    public String en_word;
    public String ar_mean;
    public String en_mean;

    public items( String ar_word, String en_word, String ar_mean, String en_mean,String _id) {
        this._id = _id;
        this.ar_word = ar_word;
        this.en_word = en_word;
        this.ar_mean = ar_mean;
        this.en_mean = en_mean;
    }


    public String get_id() {
        return _id;
    }

    public String getAr_word() {
        return ar_word;
    }

    public String getEn_word() {
        return en_word;
    }

    public String getAr_mean() {
        return ar_mean;
    }

    public String getEn_mean() {
        return en_mean;
    }
}
