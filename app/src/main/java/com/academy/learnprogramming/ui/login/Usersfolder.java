package com.academy.learnprogramming.ui.login;

public class Usersfolder {
    private String uid;

    @Override
    public String toString() {
        return "Usersfolder{" +
                "uid='" + uid + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Usersfolder(String uid) {
        this.uid = uid;
    }
}
