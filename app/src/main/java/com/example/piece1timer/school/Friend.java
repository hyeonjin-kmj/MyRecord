package com.example.piece1timer.school;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Friend extends User{

    int profile;
    public Friend (int unique_num, String name,int profile){
        this.unique_num = unique_num;
        this.name = name;
        this.profile = profile;
    }
    public int getProfile() {
        return profile;
    }
}
