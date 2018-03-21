package com.example.xghos.loginregister;



class User {
    String id;
    String userName;
    String email;
    String password;
    String accType;

    User(String id, String userName, String email, String password, String accType){
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.accType = accType;
    }

    User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
}
