package com.academy.learnprogramming.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserNaNo implements Parcelable {
    private String number;
    private String userName;

    public UserNaNo(String number, String userName) {
        this.number = number;
        this.userName = userName;
    }

    protected UserNaNo(Parcel in) {
        number = in.readString();
        userName = in.readString();
    }

    public static final Creator<UserNaNo> CREATOR = new Creator<UserNaNo>() {
        @Override
        public UserNaNo createFromParcel(Parcel in) {
            return new UserNaNo(in);
        }

        @Override
        public UserNaNo[] newArray(int size) {
            return new UserNaNo[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
        parcel.writeString(userName);
    }
}
