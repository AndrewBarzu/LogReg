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

}
