package com.example.lab_6;

public class userModel {

    private int id;
    private String name;
    private String pass;
    private String salt;

    public userModel(int id, String name, String pass, String salt) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "userModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
