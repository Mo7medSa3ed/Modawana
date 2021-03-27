package com.example.modawana;

public class ApiClass {

    private get_uer_login bookmark;
    private String name;
    private String email;
    private String password;
    private String _id;





    public ApiClass(String first_name, String email, String password) {
        this.name = first_name;
        this.email = email;
        this.password = password;
    }

    public ApiClass(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public get_uer_login getBookmark() {
        return bookmark;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }



    public String getPassword() {
        return password;
    }


}