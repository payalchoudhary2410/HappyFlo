package com.example.happyflo;

public class Users {

    String userName,userphone,dateofBirth,dateofPeriod;

    public Users() {
    }

    public Users(String userName, String userphone, String dateofBirth, String dateofPeriod) {
        this.userName = userName;
        this.userphone = userphone;
        this.dateofBirth = dateofBirth;
        this.dateofPeriod = dateofPeriod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getDateofPeriod() {
        return dateofPeriod;
    }

    public void setDateofPeriod(String dateofPeriod) {
        this.dateofPeriod = dateofPeriod;
    }
}
