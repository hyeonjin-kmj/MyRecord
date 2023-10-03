package com.example.piece1timer.school;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.github.tlaabs.timetableview.Sticker;
import com.github.tlaabs.timetableview.TimetableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String ID;
    String PW;
    String name;

    String uri;

    String timetable_info;

    int unique_num;

    //친구 목록
    ArrayList<Integer> friend_unqs = new ArrayList<>();

    public User (){}

    public User (int unique_num, String ID, String PW, String name){
        this.ID = ID;
        this.PW = PW;
        this.name = name;
        this.unique_num = unique_num;
    }

    public User(String json){//= load
        //Log.i("보기", "!!들어온 json 스트링 : "+json);
        friend_unqs = new ArrayList<>();
        try {//[] array, {} object
            JSONObject obj = new JSONObject(json);//전체
            this.ID = obj.getString("id");//기본 값은 바로 담고
            this.name = obj.getString("name");
            this.PW = obj.getString("pw");
            //Log.i("보기", "!!세팅 후 값 : "+getID()+", "+getPW()+", "+getName());
            JSONArray friends_list = new JSONArray(obj.getString("friends"));//list 값은 안에 것부터 꼼꼼이 가져옴.

            for(int i=0; i < friends_list.length(); i++){
                JSONObject jObject = friends_list.getJSONObject(i);
                Integer unq = Integer.parseInt(jObject.getString("unq"));
                friend_unqs.add(unq);
            }
            this.timetable_info = obj.getString("timetable");
            if (obj.getString("uri")!=null) {
                this.uri = obj.getString("uri");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("보기", "지금 현재 검사하는 유저");
    }

    public String getID() {return ID;}

    public String getPW(){return PW;}

    public String getUri() {return uri;}

    public String getName() {return name;}

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setID(String ID) {this.ID = ID;}

    public void setName(String name) {this.name = name;}

    public void setPW(String PW) {this.PW = PW;}

    public void setList_friends(ArrayList<Integer> friend_unqs) {
        this.friend_unqs = friend_unqs;
    }

    public void setTimetable_info(String timetable_info) {
        this.timetable_info = timetable_info;
    }

    public String getTimetable_info() {
        return timetable_info;
    }

    public ArrayList<Integer> getFriend_unqs() {
        return friend_unqs;
    }

    public int getUnique_num() {
        return unique_num;
    }

    public String toJson(){//내 거 : !!하기 전 timetable.createsavedata 해줘야 함.
        String json = "";

        JSONObject object = new JSONObject();
        try{
            object.put("id", ID);
            object.put("name", name);
            object.put("pw", PW);
            object.put("uri", uri);
            object.put("friends", list_toString());
            object.put("timetable", timetable_info);
            json = object.toString();
        }
        catch (Exception e){

        }

        return json;
    }

    public String list_toString(){
        JSONArray jsonArray = new JSONArray();
        try {
            for (Integer unq : friend_unqs){
                JSONObject object = new JSONObject();
                object.put("unq", unq);
                jsonArray.put(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonArray.toString().replace("\"", "");
    }
}
