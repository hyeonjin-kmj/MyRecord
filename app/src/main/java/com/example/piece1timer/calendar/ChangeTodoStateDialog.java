package com.example.piece1timer.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.piece1timer.R;
import com.example.piece1timer.diary.MyDiary;

public class ChangeTodoStateDialog extends DialogFragment implements View.OnClickListener{

    public static String TAG_EVENT_DIALOG = "dialog_event";

    //dismiss될 때 넘겨주기 위한 interface
    int 바뀐진행상태;
    CustomDialogListener customDialogListener;
    public interface CustomDialogListener{
        void onCancel();
        void onStateChanged(int 바뀐진행상태);
    }

    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()!= R.id.btn_no) {
            if (v.getId() == R.id.not_yet) {바뀐진행상태 = R.drawable.not_yet;}
            else if (v.getId() == R.id.ing) {바뀐진행상태 = R.drawable.ing;}
            else if (v.getId() == R.id.done) {바뀐진행상태 = R.drawable.done;}
            customDialogListener.onStateChanged(바뀐진행상태);
        }

        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_change_state, container);

        //나가기
        ImageButton btn_no = v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);

        LinearLayout not_yet = v.findViewById(R.id.not_yet);
        not_yet.setOnClickListener(this);
        LinearLayout ing = v.findViewById(R.id.ing);
        ing.setOnClickListener(this);
        LinearLayout done = v.findViewById(R.id.done);
        done.setOnClickListener(this);

        return v;
    }
}
