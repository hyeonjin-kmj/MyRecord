package com.example.piece1timer.diary;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.piece1timer.R;
public class diary_quit_writing extends DialogFragment {
    String TAG_EVENT_DIALOG = "dialog_event";
    public static boolean quit_dialog_opened;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_quit_writing, container);
        ImageButton btn_yes = v.findViewById(R.id.btn_yes);
        quit_dialog_opened = false;

        diary_write write_page = new diary_write();
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                ((diary_write)getContext()).finish();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("시점","dialog onDestroyView 호출" );
        quit_dialog_opened = true;
    }
}