package com.example.piece1timer.diary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.example.piece1timer.R;
import com.example.piece1timer.calendar.*;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import com.example.piece1timer.school.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class MyDiary extends Application {
    public static SharedPreferences DBtodo, DBdiary, DBemotion;
    public static SharedPreferences.Editor editorT, editorD, editorE;
    public static SharedPreferences.OnSharedPreferenceChangeListener listenerD;
    static Gson gson2 = new GsonBuilder().registerTypeAdapter(RecyclerItemDiary.class, new MyTypeAdapter<RecyclerItemDiary>()).create();

    public static User 현재유저;
    @Override
    public void onCreate(){
        super.onCreate();
        MyDiary.context = getApplicationContext();

        //CHANGE
        DBtodo = getSharedPreferences("DBtodo", Activity.MODE_PRIVATE);
        DBdiary = getSharedPreferences("DBdiary", Activity.MODE_PRIVATE);
        DBemotion = getSharedPreferences("DBemotion", Activity.MODE_PRIVATE);
        editorT = DBtodo.edit();
        editorD = DBdiary.edit();
        editorE = DBemotion.edit();
        DBdiary.registerOnSharedPreferenceChangeListener(listenerD);


        editorD.clear();
        RecyclerItemDiary sampleD1 = new RecyclerItemDiary(context, "20230909", R.drawable.very_happy, "짱 행복해!!", "아따 맛있다");
        RecyclerItemDiary sampleD2 = new RecyclerItemDiary(context, "20231020", R.drawable.peaceful, "평온티비", "조용-하니 좋다");
        RecyclerItemDiary sampleD3 = new RecyclerItemDiary(context, "20231010", R.drawable.sad, "슬퍼", "힘들다잉");

        editorD.putString(sampleD1.yyyyMMdd, gson2.toJson(sampleD1));
        editorD.putString(sampleD2.yyyyMMdd, gson2.toJson(sampleD2));
        editorD.putString(sampleD3.yyyyMMdd, gson2.toJson(sampleD3));
        editorD.apply();

        MyDiary.setMList();
        MyDiary.setRList();
    }

    //Intent
    static int CODE_START_NEW = 0, CODE_EDIT = 1, CODE_SEE = 2, CODE_END_WRITE = 3;

    //Date
    public static SimpleDateFormat format_yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public static String 현재날짜 = format_yyyyMMdd.format(Calendar.getInstance().getTime());
    public static String 현재yyyyMM = format_yyyyMMdd.format(Calendar.getInstance().getTime()).substring(0, 6);
    public static String getYear(String yyyyMMdd) {
        return yyyyMMdd.substring(0,4);
    }

    public static String getMonth(String yyyyMMdd) {
        return yyyyMMdd.substring(4,6)+"월";
    }
    public static String getDay(String yyyyMMdd) {
        return yyyyMMdd.substring(6)+"일";
    }

    public static String getYearMonth(String yyyyMMdd) {
        return getYear(yyyyMMdd) + " " + getMonth(yyyyMMdd);
    }
    public static String getDayDay(String yyyyMMdd) {
        LocalDate date = LocalDate.of(Integer.parseInt(yyyyMMdd.substring(0,4)), Integer.parseInt(yyyyMMdd.substring(4,6)), Integer.parseInt(yyyyMMdd.substring(6)));
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String 요일 = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        return yyyyMMdd.substring(6)+ " "+요일;
    }

    //샘플용으로 쓰던 eList, pList 삭제
    static Context context;
    public static ArrayList<RecyclerItemDiary> mList = new ArrayList<>();

    static ArrayList<RecyclerItemRep> rList = new ArrayList<>();

    public static ArrayList<RecyclerItemTodo> tList = new ArrayList<>();

    public static void sorting(){
        Collections.sort(mList, new Comparator<RecyclerItemDiary>() {
            @Override
            public int compare(RecyclerItemDiary o1, RecyclerItemDiary o2) {
                if (o1.yyyyMMdd.compareTo(o2.yyyyMMdd) < 0) return -1;
                return 1;
            }
        });
    }

    static int BASIC_MOOD_NAME = R.drawable.little_happy;
    static int BASIC_MOOD_NAME_POSTION = 4;

    public static void setMList () {//전체 가져온 후 정렬하기 => mainDairy, diary_list에서 필요한.
        mList.clear();
        Map<String, ?> map = DBdiary.getAll();
//        Log.i("상태", "=====================================");
//        for (String key : map.keySet()){
//            Log.i("상태", "key > "+key);
//            Log.i("상태", "DBdiary.getString(key) > "+DBdiary.getString(key, "값 없음 메롱"));
//            mList.add(gson2.fromJson(DBdiary.getString(key, "값 없음 메롱"), RecyclerItemDiary.class));
//            Log.i("상태", "=====================================");
//        }
//        Log.i("상태", "mlist 상태 > "+mList.toString());//ㅋ 일단 포기^^!
//        sorting();
        //https://yermi.tistory.com/entry/Library-%EC%9E%90%EB%B0%94Java%EB%A1%9C-%EC%A0%9C%EC%9D%B4%EC%8A%A8Json-%EC%B6%9C%EB%A0%A5%ED%95%98%EA%B8%B0-Json-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-Gson
        //검색어 > gson.tojson json 구조 출력하기
    }

    public static void setRList () {//해당하는 월의 일기만 가져와서 정렬하기
        rList.clear();
        for (RecyclerItemDiary diary : mList){
            if (diary.yyyyMMdd.contains(MyDiary.현재yyyyMM)) rList.add(new RecyclerItemRep(context, diary.yyyyMMdd, diary.mood_name, diary));
        }
    }

    public static String[] todayTodo;
    public static String[] todoItem;
    public static void setTList () {//그 날의 할 일 목록 구성.
        tList.clear();

        //CHANGE
        if (DBtodo.contains(현재날짜)){
            todayTodo = DBtodo.getString(현재날짜, " ").split("\\|");
            for (String todo : todayTodo){
                todoItem = todo.split("\\*"); //20230917 할일*상태|하일*상태
                tList.add(new RecyclerItemTodo(context, 현재날짜, todoItem[0], DBtodo.getInt(todoItem[1], -1)));
            }
        }
    }

}
