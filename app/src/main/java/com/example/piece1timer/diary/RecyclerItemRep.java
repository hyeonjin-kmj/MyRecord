package com.example.piece1timer.diary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class RecyclerItemRep {
    Context context;
    int e_name;
    Drawable emotion;
    String yyyyMMdd;

    RecyclerItemDiary diary;

    public RecyclerItemRep (Context context, String yyyyMMdd, int e_name, RecyclerItemDiary diary) {
        this.context = context;
        this.e_name = e_name;
        this.yyyyMMdd = yyyyMMdd;
        //this.emotion = ContextCompat.getDrawable(context, e_name);
        this.diary = diary;
    }
}
