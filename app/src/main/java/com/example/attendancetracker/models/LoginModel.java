package com.example.attendancetracker.models;

public class LoginModel {

    private String mEmail;
    private String mPassword;
    private String mLoginButton;
    private String mSignUpButton;
    private String mEmailWarning;
    private String mPasswordWarning;
    private String mWordLength;

    public LoginModel(String mEmail, String mPassword, String mLoginButton, String mSignUpButton, String mEmailWarning, String mPasswordWarning,String mWordLength) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mLoginButton = mLoginButton;
        this.mSignUpButton = mSignUpButton;
        this.mEmailWarning = mEmailWarning;
        this.mPasswordWarning = mPasswordWarning;
        this.mWordLength= mWordLength;
    }

    public LoginModel() {
    }

    public int getWordLength() {
        return mWordLength.length();
    }

    public void setWordLength(String mWordLength) {
        this.mWordLength = mWordLength;
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

    public String getLoginButton() {
        return mLoginButton;
    }

    public void setLoginButton(String mLoginButton) {
        this.mLoginButton = mLoginButton;
    }

    public String getSignUpButton() {
        return mSignUpButton;
    }

    public void setSignUpButton(String mSignUpButton) {
        this.mSignUpButton = mSignUpButton;
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
}
