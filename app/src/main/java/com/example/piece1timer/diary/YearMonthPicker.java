package com.example.piece1timer.diary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.piece1timer.R;

import java.util.Calendar;

public class YearMonthPicker extends DialogFragment {
    int max_year;//날짜 받아서 설정
    int min_year = 2000;

    DatePickerDialog.OnDateSetListener listener;
    Calendar cal = Calendar.getInstance();

    public void setListener (DatePickerDialog.OnDateSetListener listener){this.listener = listener;}

    Button btn_cancel, btn_done;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.year_month_picker, null);

        //선언
        NumberPicker year_picker = dialog.findViewById(R.id.year_picker);
        NumberPicker month_picker = dialog.findViewById(R.id.month_picker);

        btn_done = dialog.findViewById(R.id.btn_done);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMonthPicker.this.getDialog().cancel();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDateSet(null, year_picker.getValue(), month_picker.getValue(), 0);
                YearMonthPicker.this.getDialog().cancel();
            }
        });

        //picker value 설정
            //month
        month_picker.setMinValue(1);
        month_picker.setMaxValue(12);
        month_picker.setValue(cal.get(Calendar.MONTH)+1);

            //year
        year_picker.setMinValue(min_year);
        year_picker.setMaxValue(max_year);
        year_picker.setValue(cal.get(Calendar.YEAR));

        builder.setView(dialog);

        return builder.create();
    }

    public void setMaxYear (String yyyyMMdd) {
        this.max_year = Integer.valueOf(MyDiary.getYear(yyyyMMdd));
    }
}
