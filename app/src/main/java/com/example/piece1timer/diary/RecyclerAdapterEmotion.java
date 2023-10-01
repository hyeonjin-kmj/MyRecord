package com.example.piece1timer.diary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piece1timer.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class RecyclerAdapterEmotion extends RecyclerView.Adapter<RecyclerAdapterEmotion.ViewHolder> {
    public static ArrayList<RecyclerItemEmotion> list_emotion = new ArrayList<>();
    int pos;

    public interface OnItemClickListener { void onItemClick(View v, int pos);}

    OnItemClickListener mListener = null;
    Context context;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_emotions, parent, false);
        RecyclerAdapterEmotion.ViewHolder viewHolder = new RecyclerAdapterEmotion.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerItemEmotion emotion = list_emotion.get(position);

        holder.emotion.setImageResource(emotion.mood_name);
    }

    @Override
    public int getItemCount() {
        return list_emotion.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView emotion;

        ViewHolder(View itemView) {
            super(itemView);

            this.emotion = itemView.findViewById(R.id.emotion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null) mListener.onItemClick(v, pos);
                }
            });
        }
    }

    public void loadEList (SharedPreferences pref, Gson gson) {
        Type typeToken = new TypeToken<ArrayList<RecyclerItemEmotion>>() {}.getType();
        this.list_emotion = gson.fromJson(pref.getString(("all"), ""), typeToken);
    }
}
