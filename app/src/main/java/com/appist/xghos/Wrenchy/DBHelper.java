package com.appist.xghos.Wrenchy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "MY_DATABASE.db";

    private static final String TABLE_NAME = "MY_TABLE";

    private static final String ACC_TYPE = "TYPE";
    private static final String USER_ID = "ID";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DBHelper) {
        String CREATE_USER_TABLE = " CREATE TABLE " + TABLE_NAME + " ( " + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACC_TYPE + " TEXT," + USERNAME + " TEXT, " + EMAIL + " TEXT, " + PASSWORD + " TEXT ); ";
        DBHelper.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DBHelper, int i, int il) {

        DBHelper.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(DBHelper);
    }

    void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(USERNAME, user.userName);
        values.put(EMAIL, user.email);
        values.put(PASSWORD, user.password);
        values.put(ACC_TYPE, user.accType);

        db.insert(TABLE_NAME, null, values);
    }

    User Authenticate(User user) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{USER_ID, USERNAME, EMAIL, PASSWORD, ACC_TYPE},
                EMAIL + "=?",
                new String[]{user.email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

            if (user.password.equals(user1.password)) {
                return user1;
            }
        }
        return null;
    }

//    public ArrayList<User> getUsers(DBHelper dbhelper)
//    {
//        SQLiteDatabase db = dbhelper.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT * from users_table", null);
//
//        ArrayList<User> users = new ArrayList<User>();
//
//        while (cursor.moveToNext())
//        {
//            User user = new User();
//            user.userName = cursor.getString(cursor.getColumnIndex(USERNAME));
//            user.email = cursor.getString(cursor.getColumnIndex(EMAIL));
//            user.accType = cursor.getString(cursor.getColumnIndex(ACC_TYPE));
//
//            users.add(user);
//        }
//
//        return users;
//    }

//    public boolean deleteUser(String username, DBHelper dbhelper, Context context)
//    {
//        SQLiteDatabase db = dbhelper.getWritableDatabase();
//
//        try {
//            db.delete(TABLE_NAME, "USERNAME=?", new String[]{username});
//        }
//        catch (Exception e) {
//            Log.d("+++", e.getStackTrace().toString());
//            return false;
//        }
//        return true;
//    }

}
