package com.androidadvance.encryptedapi.models;
import org.json.JSONObject;

public class User {
    private JSONObject user_data;

    public User(){}

    public User(JSONObject xObj) {
        this.user_data = xObj;
    }
}
