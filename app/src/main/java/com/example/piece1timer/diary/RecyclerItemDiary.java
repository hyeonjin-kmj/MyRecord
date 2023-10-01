package com.example.piece1timer.diary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import com.example.piece1timer.R;

import java.io.Serializable;

public class RecyclerItemDiary implements Serializable {
    public Context context;
    public Drawable emotion;
    public String yyyyMMdd;
    public int mood_name;
    public String 감정desc;
    public String 내용;
    public static String key_name;


    //json 오류 난  거 알아내기 위한 몸부림..
    RecyclerItemDiary () {}

    public void setContext(Context context){this.context = context;}

    public Context getContext() {
        return context;
    }

    public void setEmotion(Drawable drawable){this.emotion = drawable;}

    public Drawable getEmotion() {
        return emotion;
    }

    public void setYyyyMMdd(String yyyyMMdd){this.yyyyMMdd = yyyyMMdd;}

    public String getYyyyMMdd() {
        return yyyyMMdd;
    }

    public void setMood_name(int mood_name){this.mood_name = mood_name;}

    public int getMood_name() {
        return mood_name;
    }

    public void set감정desc(String 감정desc){this.감정desc = 감정desc;}

    public String get감정desc() {
        return 감정desc;
    }

    public void set내용(String 내용){this.내용 = 내용;}

    public String get내용() {
        return 내용;
    }

    public void setKey_name(String name){this.key_name = name;}

    public String getKey_name(){
        return key_name;
    }




    //
    RecyclerItemDiary (Context context, String yyyyMMdd, int mood_name, String 감정desc, String 내용){
        this.context = context;
        this.yyyyMMdd = yyyyMMdd;
        this.mood_name = mood_name;
        this.감정desc = 감정desc;
        this.내용 = 내용;
        setKey_name(mood_name);
    }

    public void setKey_name(int mood_name){//일단 나중에 삭제
        if (mood_name == R.drawable.angry) this.key_name = "화나";
        else if (mood_name == R.drawable.tired) this.key_name = "피곤해";
        else if (mood_name == R.drawable.dizzy) this.key_name = "어질어질해";
        else if (mood_name == R.drawable.depressed) this.key_name = "걱정돼";
        else if (mood_name == R.drawable.peaceful) this.key_name = "평온해";
        else if (mood_name == R.drawable.very_happy) this.key_name = "엄청 행복해";
        else if (mood_name == R.drawable.little_happy) this.key_name = "꽤 행복해";
        else if (mood_name == R.drawable.sad) this.key_name = "슬퍼";
        else if (mood_name == R.drawable.zzipzzip) this.key_name = "뭔가 찜찜해..";
    }

    //"엄청 행복해|화이팅|더 읻으면 돼. 잘하고 있어."
    public String toOneString(){
        return key_name+"|"+감정desc+"|"+내용;
    }
}
