package com.example.attendancetracker.models;

public class SignUpModel {

    private String mEmail;
    private String mPassword;
    private String mName;
    private String mLogin;
    private String mEmailWarning;
    private String mPasswordWarning;
    private String mWordLength;

    public SignUpModel(String mEmail, String mPassword, String mName, String mLogin, String mEmailWarning, String mPasswordWarning, String mWordLength) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mName = mName;
        this.mLogin = mLogin;
        this.mEmailWarning = mEmailWarning;
        this.mPasswordWarning = mPasswordWarning;
        this.mWordLength = mWordLength;
    }

    public SignUpModel() {
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public String getEmailWarning() {
        return mEmailWarning;
    }

    public void setEmailWarning(String mEmailWarning) {
        this.mEmailWarning = mEmailWarning;
    }

    public String getPasswordWarning() {
        return mPasswordWarning;
    }

    public void setPasswordWarning(String mPasswordWarning) {
        this.mPasswordWarning = mPasswordWarning;
    }

    public int getWordLength() {
        return mWordLength.length();
    }

    public void setWordLength(String mWordLength) {
        this.mWordLength = mWordLength;
    }
}
