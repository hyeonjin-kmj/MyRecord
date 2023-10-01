package com.example.piece1timer.diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piece1timer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class RecyclerAdapterDiary extends RecyclerView.Adapter<RecyclerAdapterDiary.ViewHolder> {
    Context context;
    ArrayList<RecyclerItemDiary> list_diary;

    int checkedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {//리스너 객체를 어댑터에 전달하는 메서드
        this.mListener = listener;
    }


    RecyclerAdapterDiary(ArrayList<RecyclerItemDiary> list, Context context) {
        this.list_diary = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//당장 화면에 보이는 만큼만 viewHolder 만들어짐 => 바로 BindViewHolder 호출해서 데이터 연결
        //스크롤을 끝까지 내려 모든 데이터가 화면에 표시된 후부터는 onCreateViewHolder, onBindViewHolder가 더이상 호출되지 않음.
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.recycler_item_diary, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterDiary.ViewHolder holder, int position) {
        RecyclerItemDiary diary = list_diary.get(position);
        holder.date.setText(MyDiary.getDayDay(diary.yyyyMMdd));
        holder.YearMonth.setText(MyDiary.getYearMonth(diary.yyyyMMdd));
        holder.feel_image.setImageDrawable(diary.emotion);
        holder.감정desc.setText(diary.감정desc);
        holder.내용.setText(diary.내용);
    }

    @Override
    public int getItemCount() {
        return list_diary.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, YearMonth;
        ImageView feel_image;
        EditText 감정desc;
        EditText 내용;
        ImageButton btn_share, btn_edit, btn_delete;

        ViewHolder(View itemView) {
            super(itemView);

            this.date = itemView.findViewById(R.id.DayDay);
            this.YearMonth = itemView.findViewById(R.id.YearMonth);
            this.feel_image = itemView.findViewById(R.id.feel_image);
            this.감정desc = itemView.findViewById(R.id.today_emotion);
            감정desc.setEnabled(false);
            this.내용 = itemView.findViewById(R.id.content);
            내용.setEnabled(false);

            this.btn_edit = itemView.findViewById(R.id.btn_edit);
            this.btn_delete = itemView.findViewById(R.id.btn_delete);
            this.btn_share = itemView.findViewById(R.id.btn_share);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedPosition = getAbsoluteAdapterPosition();
                    if (checkedPosition != RecyclerView.NO_POSITION && mListener != null)
                        mListener.onItemClick(v, checkedPosition);
                    RecyclerItemDiary dia = list_diary.get(checkedPosition);
                    //intent 내용 넣어보내기 => 날짜만 보내기 알아서 get
                    Intent intent = new Intent(context, diary_write.class);
                    intent.putExtra("CODE", MyDiary.CODE_EDIT);
                    intent.putExtra("yyyyMMdd", dia.yyyyMMdd);
                    intent.putExtra("mood_name", dia.mood_name);
                    intent.putExtra("감정desc", dia.감정desc);
                    intent.putExtra("내용", dia.내용);
                    //deleteDiary();
                    context.startActivity(intent);
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedPosition = getAbsoluteAdapterPosition();
                    if (checkedPosition != RecyclerView.NO_POSITION && mListener != null)
                        mListener.onItemClick(v, checkedPosition);

                    AlertDialog.Builder msgBuilder = new AlertDialog.Builder(v.getContext())
                            .setTitle("한 번 삭제되면 다시 되돌릴 수 없습니다.")
                            .setMessage("정말 삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteDiary();
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss();
                                }
                            });
                    AlertDialog msgDlg = msgBuilder.create();
                    msgDlg.show();
                }
            });

            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);//manifest  내 intent filter = 암시적 인텐트 수신 시.
                    sendIntent.setType("image/*");

                    Intent shareIntent = Intent.createChooser(sendIntent, "일기 공유");
                    //startActivity(shareIntent);
                }
            });
        }

        public void deleteDiary() {
            RecyclerItemDiary delete_dia = list_diary.get(checkedPosition);
            MyDiary.editorD.remove(delete_dia.yyyyMMdd);
            MyDiary.editorD.apply();
            notifyItemRemoved(checkedPosition);
            checkedPosition = -1;
        }
    }
}
