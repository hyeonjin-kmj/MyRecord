package com.example.piece1timer.school;

import android.net.Uri;

import com.github.tlaabs.timetableview.Sticker;
import com.github.tlaabs.timetableview.TimetableView;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String ID;
    String PW;
    String name;

    String uri;

    //timetable
    //TimetableView timetable;

    //친구 목록
    ArrayList<User> list_friends = new ArrayList<>();

    public User (String ID, String PW, String name){
        this.ID = ID;
        this.PW = PW;
        this.name = name;
    }

    public User (String ID, String PW, String name, String uri){
        this.ID = ID;
        this.PW = PW;
        this.name = name;
        this.uri = uri;
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

    public void setList_friends(ArrayList<User> list_friends) {
        this.list_friends = list_friends;
    }
}
