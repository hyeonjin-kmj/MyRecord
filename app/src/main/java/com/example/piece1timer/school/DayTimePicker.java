package com.example.piece1timer.school;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.piece1timer.R;
import com.example.piece1timer.diary.RecyclerAdapterDiary;

public class DayTimePicker extends DialogFragment{
    Spinner spinner;
    final int MIN_HOUR = 9, MAX_HOUR = 20;
    final int MIN_MIN = 0, MAX_MIN = 59;
    Button btn_cancel, btn_done;
    String week_day="초기값";
    TextView tv_error;

    NumberPicker start_hour_picker, start_min_picker, end_hour_picker, end_min_picker;

    public interface OnItemClickListener {
        void onItemClick(String week_day, int start_hour, int start_min, int end_hour, int end_min);
    }

    OnItemClickListener listener;

    DayTimePicker (DayTimePicker.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.day_time_picker, null);

        spinner = dialog.findViewById(R.id.weekday_spinner); //연결
        spinner.setSelection(0);

        start_hour_picker = dialog.findViewById(R.id.start_hour_picker);
        start_min_picker = dialog.findViewById(R.id.start_min_picker);
        end_hour_picker = dialog.findViewById(R.id.end_hour_picker);
        end_min_picker = dialog.findViewById(R.id.end_min_picker);

        btn_done = dialog.findViewById(R.id.btn_done_set_weekday_time);
        btn_cancel = dialog.findViewById(R.id.btn_cancel_set_weekday_time);

        tv_error = dialog.findViewById(R.id.tv_error);
        //본 내용
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.my_header_title, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayTimePicker.this.getDialog().cancel();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 세팅
                if (시간잘못됨()){
                    tv_error.setVisibility(View.VISIBLE);
                }
                else {
                    listener.onItemClick(week_day, start_hour_picker.getValue(), start_min_picker.getValue(), end_hour_picker.getValue(), end_min_picker.getValue());
                    DayTimePicker.this.getDialog().cancel();
                }
            }
        });
        start_hour_picker.setMinValue(MIN_HOUR); end_hour_picker.setMinValue(MIN_HOUR);
        start_hour_picker.setMaxValue(MAX_HOUR); end_hour_picker.setMaxValue(MAX_HOUR);
        start_min_picker.setMinValue(MIN_MIN); end_min_picker.setMinValue(MIN_MIN);
        start_min_picker.setMaxValue(MAX_MIN); end_min_picker.setMaxValue(MAX_MIN);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                week_day = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //
        builder.setView(dialog);
        return builder.create();
    }

    public boolean 시간잘못됨(){
        if (start_hour_picker.getValue() > end_hour_picker.getValue() ||
                (start_hour_picker.getValue()==end_hour_picker.getValue()&&start_min_picker.getValue() > end_min_picker.getValue())) return true;
        return false;
    }
}
