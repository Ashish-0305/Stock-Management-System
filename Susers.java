package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Susers {
    private int userId;
    private String userName;
    private String pswd;
    private String userType;

    public Susers(int userId, String userName, String pswd, String userType) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.pswd = pswd;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("pwsd")
    public String getPswd() {
        return pswd;
    }

    @JsonProperty("pwsd")
    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}