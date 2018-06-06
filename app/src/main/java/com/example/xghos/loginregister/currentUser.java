package com.example.xghos.loginregister;

public class currentUser {

    public static String id;
    public static String userName;
    public static String email;
    public static String accType;
    public static String phoneNumber;
    public static String avatar;
    public static String status;

    private static final currentUser ourInstance = new currentUser();

    public static currentUser getInstance() {
        return ourInstance;
    }

    private currentUser() {
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        currentUser.status = status;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        currentUser.id = id;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        currentUser.userName = userName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        currentUser.email = email;
    }

    public static String getAccType() {
        return accType;
    }

    public static void setAccType(String accType) {
        currentUser.accType = accType;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        currentUser.phoneNumber = phoneNumber;
    }

    public static String getAvatar() {
        return avatar;
    }

    public static void setAvatar(String avatar) {
        currentUser.avatar = avatar;
    }

    public static currentUser getOurInstance() {
        return ourInstance;
    }
}
