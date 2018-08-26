package com.example.xghos.Wrenchy.helpers_extras;

public class CurrentUser {

    public static String id;
    public static String userName;
    public static String email;
    public static String accType;
    public static String phoneNumber;
    public static String avatar;
    public static String oldpw;
    public static int tabindex = 0;

    private static final CurrentUser ourInstance = new CurrentUser();

    public static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        CurrentUser.id = id;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static String getAccType() {
        return accType;
    }

    public static void setAccType(String accType) {
        CurrentUser.accType = accType;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        CurrentUser.phoneNumber = phoneNumber;
    }

    public static String getAvatar() {
        return avatar;
    }

    public static void setAvatar(String avatar) {
        CurrentUser.avatar = avatar;
    }

    public static String getOldpw() {
        return oldpw;
    }

    public static void setOldpw(String oldpw) {
        CurrentUser.oldpw = oldpw;
    }

    public static CurrentUser getOurInstance() {
        return ourInstance;
    }

    public static int getTabindex() {
        return tabindex;
    }

    public static void setTabindex(int tabindex) {
        CurrentUser.tabindex = tabindex;
    }
}
