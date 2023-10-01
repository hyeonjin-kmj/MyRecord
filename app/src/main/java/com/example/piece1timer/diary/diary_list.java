package com.example.piece1timer.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.piece1timer.R;

public class diary_list extends AppCompatActivity {

    //RecyclerView용
    RecyclerView mRecyclerView = null;
    RecyclerAdapterDiary mAdapter = null;
    RecyclerView.LayoutManager lManager;
    FragmentManager fManager;
    SharedPreferences.OnSharedPreferenceChangeListener listenerM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        //데이터 설정
        mRecyclerView = findViewById(R.id.recyclerview_diaries);
        lManager = new LinearLayoutManager(this);

        //리사이클러뷰 연결
        getListnRefresh();


        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this){
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        fManager = getSupportFragmentManager();

        //리스너 연결
        listenerM = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                getListnRefresh();
            }
        };
        MyDiary.DBdiary.registerOnSharedPreferenceChangeListener(listenerM);

        Intent dia = getIntent();
        RecyclerItemDiary written_diary;
        int code = dia.getIntExtra("CODE", -1);
        if (code==MyDiary.CODE_SEE) {
            smoothScroller.setTargetPosition(dia.getIntExtra("POSITION", -1));
            mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
        }
        else if (code==MyDiary.CODE_END_WRITE){
            getListnRefresh();
            smoothScroller.setTargetPosition(getIntent().getIntExtra("pos", -10));
            Log.i("스크롤 테스트", getIntent().getIntExtra("pos", -10)+"");
            mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }

    private void getListnRefresh() {
        MyDiary.setMList();
        mAdapter = new RecyclerAdapterDiary(MyDiary.mList, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterDiary.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {}});
        mRecyclerView.setLayoutManager(lManager);
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }
}