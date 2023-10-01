package com.example.piece1timer.diary;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.piece1timer.R;
public class diary_delete extends DialogFragment {
    String TAG_EVENT_DIALOG = "dialog_event";

    public diary_delete () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_delete, container);
        ImageButton btn_yes = (ImageButton) v.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //해당 일기 삭제
            }
        });

        ImageButton btn_no = v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//화면 그대로 유지.
                dismiss();
            }
        });

        return v;
    }
}