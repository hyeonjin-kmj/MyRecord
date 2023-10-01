package com.example.piece1timer.diary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.example.piece1timer.R;

public class RecyclerItemEmotion {
    int mood_name;
    String 감정desc;

    public RecyclerItemEmotion (int mood_name, String 감정desc) {
        this.mood_name = mood_name;
        this.감정desc = 감정desc;
    }
}
