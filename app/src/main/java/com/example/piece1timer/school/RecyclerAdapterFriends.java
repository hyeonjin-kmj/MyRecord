package com.example.piece1timer.school;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.google.gson.Gson;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterFriends extends RecyclerView.Adapter<RecyclerAdapterFriends.ViewHolder> {
    SharedPreferences DBuser;
    SharedPreferences.Editor editorU;
    Gson gson;

    public static ArrayList<Friend> list_friend = new ArrayList<>();
    int pos;

    public interface OnItemClickListener {void onItemClick(View v, int pos);}

    OnItemClickListener mListener = null;
    Context context;

    public void setOnItemClickListener(OnItemClickListener listener){this.mListener = listener;}
    public void setSP(SharedPreferences sp, SharedPreferences.Editor e, Gson gson){
        this.DBuser = sp;
        this.editorU = e;
        this.gson = gson;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item_friend, parent, false);
        RecyclerAdapterFriends.ViewHolder viewHolder = new RecyclerAdapterFriends.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterFriends.ViewHolder holder, int position) {
        Friend friend = list_friend.get(position);
        holder.profile_image.setImageResource(friend.profile);
        holder.name.setText(friend.name);
        //뷰 연결
    }

    @Override
    public int getItemCount() {
        return list_friend.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);

            this.profile_image = itemView.findViewById(R.id.my_profile_image);
            this.name = itemView.findViewById(R.id.my_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null) mListener.onItemClick(v, pos);
                }
            });
        }
    }

    public void setList_friend(){//DB에서 가져와서 어쩌구 저쩌구
        //현재유저의 친구키목록 순회=> DBuser에서 가져와서
        Log.i("보기", "친구목록 생성 : 친구 개수 : "+MyDiary.현재유저.friend_unqs.size());
        for (int unq_key : MyDiary.현재유저.friend_unqs){
            Friend friend = gson.fromJson(DBuser.getString(unq_key+"", "없음 메롱"), Friend.class);
            list_friend.add(friend);
        }
    }
}
