package com.example.piece1timer.diary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piece1timer.R;

import java.util.ArrayList;

//어댑터 = "사용자 데이터 리스트"로부터 "아이템 뷰"를 "만듦"
public class RecyclerAdapterRep extends RecyclerView.Adapter<RecyclerAdapterRep.ViewHolder>{
    ArrayList<RecyclerItemRep> list_rep;
    int pos;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){this.mListener = listener;}

    RecyclerAdapterRep(ArrayList<RecyclerItemRep> list){this.list_rep = list;}

    @NonNull
    @Override
    public RecyclerAdapterRep.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_item_written, parent, false);
        RecyclerAdapterRep.ViewHolder viewHolder = new RecyclerAdapterRep.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterRep.ViewHolder holder, int position) {
        RecyclerItemRep rep = list_rep.get(position);
        holder.rep.setImageDrawable(rep.emotion);
    }

    @Override
    public int getItemCount() {
        return list_rep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rep;

        ViewHolder(View itemView){
            super(itemView);

            this.rep = itemView.findViewById(R.id.written);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAbsoluteAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION && mListener != null) mListener.onItemClick(v, pos);
                }
            });
        }
    }
}
