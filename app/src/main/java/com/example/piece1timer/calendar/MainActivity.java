package com.example.piece1timer.calendar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piece1timer.DrawerBaseActivity;
import com.example.piece1timer.R;
import com.example.piece1timer.databinding.ActivityMainBinding;
import com.example.piece1timer.diary.MyDiary;
import com.example.piece1timer.diary.RecyclerItemDiary;
import com.example.piece1timer.timer.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
<View Binding 뷰 결합>
뷰와 상호작용하는 코드 쉽게 작성 가능.
모듈에서 사용 설정된 viewBinding => 각 xml 레이아웃 파일의 바인딩 클래스를 생성함.
ex. MainActivity => ActivityMainBinding
바인딩 클래스의 인스턴스에는, 상응하는 레이아웃에 id가 있는 모든 뷰의, 직접 참조가 포함됨.
=> view binding이 대부분의 findViewById를 대체함.

 */

public class MainActivity extends DrawerBaseActivity {
    ActivityMainBinding activityMainBinding;
    Calendar cal;
    RecyclerView tRecyclerView;
    RecyclerAdapterTodo tAdapter;
    FragmentManager fManager;
    SharedPreferences.OnSharedPreferenceChangeListener listenerT;

    //일기 세팅용 연결
    TextView DayDay;
    TextView YearMonth;
    ImageView feel_image;//(감정)
    EditText today_emotion, content; // (setEnabled(false))(감정글), (setEnabled(false))
    boolean 첫시작;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //일기 세팅용 연결
        DayDay = findViewById(R.id.DayDay);
        YearMonth = findViewById(R.id.YearMonth);
        feel_image = findViewById(R.id.feel_image);
        today_emotion = findViewById(R.id.today_emotion);
        content = findViewById(R.id.content);

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());//inflate = 생성하다.
        setContentView(activityMainBinding.getRoot());//getRoot() = 여기 연결된 xml 파일의 젤 상단 레이아웃 반환.
                                                    //activity_main.xml 들어가보면 여기서는 linearlayout.

        allocateActivityTitleCal("일정 관리");

        cal = Calendar.getInstance();

        listenerT = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                getListnRefresh();
            }
        };
        MyDiary.DBtodo.registerOnSharedPreferenceChangeListener(listenerT);

        activityMainBinding.mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();

                return false;
            }
        });


        activityMainBinding.addNewTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyDiary.DBtodo.contains(MyDiary.현재날짜)){
                    MyDiary.editorT.putString(MyDiary.현재날짜, MyDiary.DBtodo.getString(MyDiary.현재날짜, "")+"| *not_yet");
                }
                else {
                    MyDiary.editorT.putString(MyDiary.현재날짜, " *not_yet");
                }
                MyDiary.editorT.apply();
            }
        });

        tRecyclerView = findViewById(R.id.recyclerview_todo);
        fManager = getSupportFragmentManager();

        getListnRefresh();

        //floating button 클릭 처리
        activityMainBinding.timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, timer.class);
                startActivity(intent);
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.KOREAN);
        activityMainBinding.nowDate.setText(format.format(new Date()));

        // Calendar 클릭 처리
        activityMainBinding.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                activityMainBinding.nowDate.setText(year+ "년 " + (month + 1) + "월 " + day + "일");
                MyDiary.현재날짜 = year+String.format("%02d", month+1)+String.format("%02d", day);
                getListnRefresh();
            }
        });
    }
    public void getListnRefresh() {
        MyDiary.setTList();
        tAdapter = new RecyclerAdapterTodo(this, fManager);
        tRecyclerView.setAdapter(tAdapter);
        tRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

//    private void setDiary(String 현재날짜) {
//        if (MyDiary.CheckIfExist(현재날짜)){
//            RecyclerItemDiary diary = MyDiary.mList.get(MyDiary.position);//데이터 가져오기
//
//            DayDay.setText(MyDiary.getDayDay(diary.yyyyMMdd));//뷰에 데이터 연결
//            YearMonth.setText(MyDiary.getYearMonth(diary.yyyyMMdd));
//            feel_image.setImageDrawable(ContextCompat.getDrawable(this, diary.mood_name));
//            today_emotion.setText(diary.feeling);
//            content.setText(diary.내용);
//
//        }
//    }

    public void hideKeyboard()
    {
        if (this.getCurrentFocus()!=null){
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (!첫시작) {
            getListnRefresh();
            첫시작 = true;
        }
    }
}