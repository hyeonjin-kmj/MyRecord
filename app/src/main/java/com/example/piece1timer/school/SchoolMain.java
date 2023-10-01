package com.example.piece1timer.school;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piece1timer.DrawerBaseActivity;
import com.example.piece1timer.R;
import com.example.piece1timer.databinding.ActivitySchoolMainBinding;
import com.example.piece1timer.diary.MyDiary;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class SchoolMain extends DrawerBaseActivity {
    SharedPreferences DBlecture;
    SharedPreferences.Editor editorL;
    Gson gson = new GsonBuilder().create();

    ActivitySchoolMainBinding activitySchoolMainBinding;

    ArrayList<Schedule> all_schedules = new ArrayList<>(), deleted_schedules = new ArrayList<>(), changed_schedules = new ArrayList<>();

    String[] 요일들 = new String[] {"월", "화", "수", "목", "금", "토", "일"};

    boolean clicked_create=false, clicked_enter=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySchoolMainBinding = ActivitySchoolMainBinding.inflate(getLayoutInflater());
        setContentView(activitySchoolMainBinding.getRoot());
        allocateActivityTitle("강의 시간표");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DBlecture = getSharedPreferences("DB", MODE_PRIVATE);
        editorL = DBlecture.edit();

        load();

        activitySchoolMainBinding.midTerm.setCursorVisible(false);
        activitySchoolMainBinding.finalTerm.setCursorVisible(false);

        activitySchoolMainBinding.btnAddLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), school_add_lecture.class);
                startActivity(intent);
            }
        });

        activitySchoolMainBinding.timeTable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                //데이터 가져와서
                all_schedules = activitySchoolMainBinding.timeTable.getAllSchedulesInStickers();
                reset();//칸들 찬 내용 정리.
                deleted_schedules.clear();//다 쓰고 정리
                activitySchoolMainBinding.timeTable.remove(idx);

                for (Schedule sc : all_schedules){//String 바뀌기전 = 바뀌기 전 저장
                    deleted_schedules.add(sc);
                }
                all_schedules=activitySchoolMainBinding.timeTable.getAllSchedulesInStickers(); //바꿈
                for (Schedule sc : all_schedules){
                    if (deleted_schedules.contains(sc)){
                        deleted_schedules.remove(sc);
                        changed_schedules.add(sc);
                    }
                }

                // 강의명, 교수명, 장소(1~3개), 시간(1~3개) 설정
                //기본 정보 세팅
                Schedule sc = deleted_schedules.get(0);//어차피 시간만 다르고 기본 정보는 같으니 0의 스케줄을 겟
                activitySchoolMainBinding.professorName.setText(sc.getProfessorName());
                activitySchoolMainBinding.lectureName.setText(sc.getClassTitle());

                //장소, 시간 정보 세팅
                int cnt = deleted_schedules.size();

                switch (cnt) {
                    case 3 : {
                        sc = deleted_schedules.get(2);
                        activitySchoolMainBinding.time.setText(getFormattedString(sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(), sc.getEndTime().getHour(), sc.getEndTime().getMinute(), sc.getClassPlace()));
                    }
                    case 2 : {
                        sc = deleted_schedules.get(1);
                        activitySchoolMainBinding.time.setText(getFormattedString(sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(), sc.getEndTime().getHour(), sc.getEndTime().getMinute(), sc.getClassPlace()));
                    }
                    case 1 : {
                        sc = deleted_schedules.get(0);
                        activitySchoolMainBinding.time.setText(getFormattedString(sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(), sc.getEndTime().getHour(), sc.getEndTime().getMinute(), sc.getClassPlace()));
                    }
                }

                //강의명을 key로 DB에 저장된 중간고사 일정, 기말고사 일정이 있다면 가져오기.
                if (DBlecture.contains(sc.getClassTitle())){
                    mid_fin_date 일정 = gson.fromJson(DBlecture.getString(sc.getClassTitle(), "메롱"), mid_fin_date.class);
                    activitySchoolMainBinding.midTerm.setText(일정.getMid_term_date());
                    activitySchoolMainBinding.finalTerm.setText(일정.getFin_term_date());
                }

                ArrayList<Schedule> temp = new ArrayList<>();
                for (Schedule sc1 : schedules){//깊은 복사
                    Schedule sc_copy = new Schedule();
                    sc_copy.setClassTitle(sc1.getClassTitle());
                    sc_copy.setProfessorName(sc1.getProfessorName());
                    sc_copy.setClassPlace(sc1.getClassPlace());
                    sc_copy.setDay(sc1.getDay());
                    sc_copy.setStartTime(sc1.getStartTime());
                    sc_copy.setEndTime(sc1.getEndTime());
                    temp.add(sc_copy);
                }
                activitySchoolMainBinding.timeTable.add(temp);
            }
        });

        activitySchoolMainBinding.btnSaveTermDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lecture_name = activitySchoolMainBinding.lectureName.getText().toString();
                mid_fin_date 일정
                        = DBlecture.contains(lecture_name)
                        ? gson.fromJson(DBlecture.getString(lecture_name, "메롱"), mid_fin_date.class)
                        : new mid_fin_date(lecture_name);

                String mid_date = activitySchoolMainBinding.midTerm.getText().toString();
                String fin_date = activitySchoolMainBinding.finalTerm.getText().toString();

                if (!mid_date.isEmpty()) 일정.setMid_term_date(mid_date);
                if (!fin_date.isEmpty()) 일정.setFin_term_date(fin_date);

                String json = gson.toJson(일정);
                Log.i("보기", "json = "+ json);
                editorL.putString(lecture_name, json);
                editorL.apply();

                InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        activitySchoolMainBinding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySchoolMainBinding.timeTable.removeAll();
                String json = activitySchoolMainBinding.timeTable.createSaveData();
                editorL.putString("all", json);
                editorL.apply();
                load();
            }
        });

        setInform();

        activitySchoolMainBinding.btnCreateFriendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked_create){
                    activitySchoolMainBinding.friendCode.setVisibility(View.INVISIBLE);
                    activitySchoolMainBinding.timeLeft.setVisibility(View.INVISIBLE);
                    clicked_create = false;
                }
                else {
                    activitySchoolMainBinding.friendCode.setVisibility(View.VISIBLE);
                    activitySchoolMainBinding.timeLeft.setVisibility(View.VISIBLE);
                    clicked_create = true;
                }
            }
        });

        activitySchoolMainBinding.btnEnterFriendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked_enter){
                    activitySchoolMainBinding.enterFriendCode.setVisibility(View.VISIBLE);
                    activitySchoolMainBinding.btnDoneEnter.setVisibility(View.VISIBLE);
                    clicked_enter = true;
                }
            }
        });

        activitySchoolMainBinding.btnDoneEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //친구코드 따라 친구 목록에 친구 추가

                //정렬

                //다시 show
                activitySchoolMainBinding.enterFriendCode.setVisibility(View.INVISIBLE);
                activitySchoolMainBinding.btnDoneEnter.setVisibility(View.INVISIBLE);
                clicked_enter = false;
            }
        });

    }

    private void reset() {
        activitySchoolMainBinding.lectureName.setText("");
        activitySchoolMainBinding.professorName.setText("");
        activitySchoolMainBinding.time.setText("");
        activitySchoolMainBinding.midTerm.setText("");
        activitySchoolMainBinding.finalTerm.setText("");
    }

    public void load(){
        String json = DBlecture.getString("all", "없지롱");
        if (!json.equals("없지롱")){
            activitySchoolMainBinding.timeTable.load(json);
            all_schedules = activitySchoolMainBinding.timeTable.getAllSchedulesInStickers();
        }
    }

    public String getFormattedString(int week_day, int start_hour, int start_min, int end_hour, int end_min, String place){
        return activitySchoolMainBinding.time.getText().toString()+"\n"+
                요일들[week_day] + " " + getAmPm(start_hour) + ":"+String.format("%02d", start_min) + " - " + getAmPm(end_hour) + ":" + String.format("%02d", end_min) + " " + place;
    }
    public int getAmPm (int hour){
        return hour%12==0 ? 12 : hour%12;
    }

    public void setInform(){

        if (MyDiary.현재유저.getUri()!=null) {
            activitySchoolMainBinding.myProfile.profileImage.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));
        }
        activitySchoolMainBinding.myProfile.myName.setText("나");
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
        setInform();
    }
}