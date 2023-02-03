package com.example.speechease.Utils;


public class User {
    public String name;
    public String email;
    public String contactn;
    public String uid;
    public String password;
    public String profileimg;
    public String profilestatus;
public String  code;

    public User(){

    }

    public User(String name, String email, String contactn, String uid, String password, String code) {
        this.name = name;
        this.email = email;
this.code=code;
        this.contactn = contactn;

        this.uid = uid;

        this.password = password;


    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void setEmail(String email) {
        this.email = email;
    }



    public String getContactn() {
        return contactn;
    }

    public void setContactn(String contactn) {
        this.contactn = contactn;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}