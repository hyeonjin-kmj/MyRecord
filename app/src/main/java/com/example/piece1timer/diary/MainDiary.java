package com.example.piece1timer.diary;

import static android.opengl.ETC1.getWidth;
import static androidx.core.view.ViewCompat.getPaddingEnd;
import static androidx.core.view.ViewCompat.getPaddingStart;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.piece1timer.DrawerBaseActivity;
import com.example.piece1timer.R;
import com.example.piece1timer.databinding.ActivityMainDiaryBinding;
import com.google.gson.Gson;

import java.util.Calendar;

public class MainDiary extends DrawerBaseActivity {
    ActivityMainDiaryBinding activityMainDiaryBinding;

    //변수 선언
    boolean pushed;

    //RecyclerView용
    RecyclerView mRecyclerView, rRecyclerView;
    RecyclerAdapterEmotion mAdapter;
    RecyclerAdapterRep  rAdapter;
    GridLayoutManager gManager;

    RecyclerAdapterRep.OnItemClickListener rListener;

    SharedPreferences.OnSharedPreferenceChangeListener sListener;
    int spanCount;

    //날짜
    String 현재날짜;
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            activityMainDiaryBinding.year.setText(year+"");
            activityMainDiaryBinding.month.setText("  "+ month+"월  ");
            MyDiary.현재yyyyMM = ""+year+String.format("%02d", month);
            getListNRefresh();
        }};

    YearMonthPicker pickDia = new YearMonthPicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainDiaryBinding = ActivityMainDiaryBinding.inflate(getLayoutInflater());
        setContentView(activityMainDiaryBinding.getRoot());
        allocateActivityTitle("일기");

        //(일기 대표 이모지용)
        rRecyclerView = findViewById(R.id.recyclerview_written);
        rRecyclerView.setHasFixedSize(true);

        spanCount = MyDiary.rList.size()/6+1;
        gManager = new GridLayoutManager(this, spanCount, GridLayoutManager.HORIZONTAL, false);//행의 수 뜻 함.
        getListNRefresh();

        //(밑에 일기 쓰기 감정용)
        mRecyclerView = findViewById(R.id.recyclerview_emotions);
        mAdapter = new RecyclerAdapterEmotion();
        mAdapter.loadEList(MyDiary.DBemotion, new Gson());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //리사이클러뷰
        mAdapter.setOnItemClickListener(new RecyclerAdapterEmotion.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(), diary_write.class);
                intent.putExtra("CODE", MyDiary.CODE_START_NEW);
                intent.putExtra("yyyyMMdd", 현재날짜);
                intent.putExtra("mood_name", mAdapter.list_emotion.get(pos).mood_name);
                intent.putExtra("감정desc", mAdapter.list_emotion.get(pos).감정desc);
                startActivity(intent);
            }
        });

        rListener = new RecyclerAdapterRep.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.i("테스트", "현재r_list 크기 > "+MyDiary.rList.size());
                Intent intent = new Intent(getApplicationContext(), diary_list.class);
                intent.putExtra("CODE", MyDiary.CODE_SEE);
                intent.putExtra("POSITION", MyDiary.mList.indexOf(MyDiary.rList.get(pos).diary));//전체 중 인덱스
                //intent.putExtra("yyyyMMdd", MyDiary.rList.get(pos).yyyyMMdd);
                startActivity(intent);
            }
        };

        rAdapter.setOnItemClickListener(rListener);

        //리스너 연결
        sListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                getListNRefresh();
            }
        };
        MyDiary.DBdiary.registerOnSharedPreferenceChangeListener(sListener);

        //나머지 기능 정의
        activityMainDiaryBinding.btnWriteNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pushed) {
                    activityMainDiaryBinding.container.setVisibility(View.VISIBLE);
                    pushed = true;
                }
                else {
                    activityMainDiaryBinding.container.setVisibility(View.INVISIBLE);
                    pushed = false;
                }
            }
        });

        //날짜
            //기본 날짜 데이터 형식 = yyyyMMdd(20020809)
        현재날짜 = MyDiary.format_yyyyMMdd.format(Calendar.getInstance().getTime());

        activityMainDiaryBinding.year.setText(MyDiary.getYear(현재날짜));
        activityMainDiaryBinding.month.setText("  "+MyDiary.getMonth(현재날짜)+"  ");

        pickDia.setMaxYear(현재날짜);
        activityMainDiaryBinding.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDia.setListener(listener);
                pickDia.show(getSupportFragmentManager(), "연월선택기");
            }
        });
    }

    private void getListNRefresh() {
        MyDiary.setRList();
        rAdapter = new RecyclerAdapterRep(MyDiary.rList);
        rAdapter.setOnItemClickListener(rListener);
        rRecyclerView.setAdapter(rAdapter);

        spanCount = MyDiary.rList.size()/6+1;
        gManager.setSpanCount(spanCount);
        rRecyclerView.setLayoutManager(gManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyDiary.setRList();
        spanCount = MyDiary.rList.size()/6+1;
        gManager.setSpanCount(spanCount);
        rRecyclerView.setAdapter(rAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityMainDiaryBinding.container.setVisibility(View.GONE);
    }
}