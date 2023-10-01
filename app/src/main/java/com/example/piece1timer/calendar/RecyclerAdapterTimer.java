package com.example.piece1timer.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piece1timer.R;
import com.example.piece1timer.diary.MyDiary;
import com.example.piece1timer.diary.RecyclerAdapterEmotion;
import com.example.piece1timer.diary.RecyclerItemEmotion;
import com.example.piece1timer.timer.timer;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapterTimer extends RecyclerView.Adapter<RecyclerAdapterTimer.ViewHolder> {
    ArrayList<RecyclerItemTimer> list_timers = new ArrayList<>();

    public RecyclerAdapterTimer (ArrayList<RecyclerItemTimer> list){
        this.list_timers = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item_timer, parent, false);
        RecyclerAdapterTimer.ViewHolder viewHolder = new RecyclerAdapterTimer.ViewHolder(view);
        return viewHolder;
    }
    int totalToday;
    String message;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerItemTimer time = list_timers.get(position);
        holder.yyyyMMdd.setText(MyDiary.getYear(time.yyyyMMdd)+ " "+MyDiary.getMonth(time.yyyyMMdd)+"\n"+MyDiary.getDay(time.yyyyMMdd));
        totalToday = timer.DBtimer.getInt(time.yyyyMMdd, 0);
        message = totalToday < 60 ? String.format("%02d",totalToday)+"분":
                String.format("%02d", totalToday/60)+"시간 "
                        +String.format("%02d", totalToday%60)+"분";
        holder.hhmm.setText(message);
    }

    @Override
    public int getItemCount() {
        return list_timers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView yyyyMMdd, hhmm;

        ViewHolder(View itemView) {
            super(itemView);

            this.yyyyMMdd = itemView.findViewById(R.id.yyyyMMdd);
            this.hhmm = itemView.findViewById(R.id.hhmm);
        }
    }
}