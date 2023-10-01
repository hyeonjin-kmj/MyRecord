package com.example.piece1timer.calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.piece1timer.R;

public class RecyclerItemTodo {
    Context context;
    int state_name;
    Drawable state;
    public String todo, yyyyMMdd, key_name;

    public RecyclerItemTodo (Context context,String yyyyMMdd, String todo, int state_name) {
        this.context = context;
        this.yyyyMMdd = yyyyMMdd;
        this.state_name = state_name;
        this.state = ContextCompat.getDrawable(context, state_name);
        this.todo = todo;

        setKey_name(state_name);

    }

    public void setKey_name (int state_name) {
        if (state_name == R.drawable.not_yet)key_name = "not_yet";
        else if (state_name == R.drawable.ing)key_name = "ing";
        else if (state_name == R.drawable.done)key_name = "done";
    }
}
