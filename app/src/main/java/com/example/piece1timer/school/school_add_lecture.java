package com.example.piece1timer.school;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.piece1timer.R;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import java.util.ArrayList;
import java.util.Arrays;

public class school_add_lecture extends AppCompatActivity {

    SharedPreferences DBlecture;
    SharedPreferences.Editor editorL;

    final String DEFAULT_DAY_TIME = "요일, 시간 설정";

    TimetableView timetable;

    String[] 요일들 = new String[] {"월", "화", "수", "목", "금", "토", "일"};

    ArrayList<Schedule> all_schedules = new ArrayList<>(), schedules = new ArrayList<>(), deleted_schedules = new ArrayList<>();
    Button btn_done, btn_cancel, btn_add_time_room;
    ImageButton btn_add_new_lecture;
    View add_time_room1, add_time_room2, add_time_room3;
    TextView when1, when2, when3, where1, where2, where3;

    CheckBox cb_copy_place;
    int cnt=0;

    EditText lecture_name, professor_name;

    Schedule sc1 = new Schedule(), sc2 = new Schedule(), sc3 = new Schedule();
    FragmentManager fManager;

    boolean copied;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_add_lecture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DBlecture = getSharedPreferences("DB", MODE_PRIVATE);
        editorL = DBlecture.edit();

        timetable = findViewById(R.id.time_table);
        btn_done = findViewById(R.id.btn_done_add_lecture);
        btn_cancel = findViewById(R.id.btn_cancel_add_lecture);
        btn_add_time_room = findViewById(R.id.btn_add_time_room);
        btn_add_new_lecture = findViewById(R.id.btn_add_new_lecture);
        add_time_room1 = findViewById(R.id.add_time_room1);
        add_time_room2 = findViewById(R.id.add_time_room2);
        add_time_room3 = findViewById(R.id.add_time_room3);
        cb_copy_place = findViewById(R.id.cb_copy_place);
        cb_copy_place.setChecked(false);
        fManager = getSupportFragmentManager();
        lecture_name = findViewById(R.id.lecture);
        lecture_name.setCursorVisible(false);
        professor_name = findViewById(R.id.professor);
        professor_name.setCursorVisible(false);

        when1 = add_time_room1.findViewById(R.id.set_time);
        when2 = add_time_room2.findViewById(R.id.set_time);
        when3 = add_time_room3.findViewById(R.id.set_time);

        where1 = add_time_room1.findViewById(R.id.set_room);
        where2 = add_time_room2.findViewById(R.id.set_room);
        where3 = add_time_room3.findViewById(R.id.set_room);

        load();

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                finish();//전의 페이지로 넘어가기.
            }
        });

        cb_copy_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_copy_place.isChecked()){
                    cb_copy_place.setChecked(false);
                    where2.setText("");
                    where3.setText("");
                }else {
                    cb_copy_place.setChecked(true);
                    if (!(where1.getText().toString() == "" ||where1.getText().toString().isEmpty())){
                        where2.setText(where1.getText().toString());
                        where3.setText(where1.getText().toString());
                        copied = true;
                    }
                }
            }
        });

        View.OnClickListener listener_set_when = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayTimePicker dayTimePicker = new DayTimePicker(new DayTimePicker.OnItemClickListener() {
                    @Override
                    public void onItemClick(String week_day, int start_hour, int start_min, int end_hour, int end_min) {
                        //textview에 받은 거 쓰기
                        TextView now = (TextView) v;
                        String set_when = getFormattedString(week_day, start_hour, start_min, end_hour, end_min);
                        now.setText(set_when);
                        //정보 가져와서 스케줄에 적용 시키기.
                        if (now.getParent().equals(add_time_room1)){
                            setWhenInSchedule(sc1, week_day, start_hour, start_min, end_hour, end_min);
                        }
                        else if (now.getParent().equals(add_time_room2)){
                            setWhenInSchedule(sc2, week_day, start_hour, start_min, end_hour, end_min);
                        }
                        else if (now.getParent().equals(add_time_room3)){
                            setWhenInSchedule(sc3, week_day, start_hour, start_min, end_hour, end_min);
                        }
                    }
                });
                dayTimePicker.show(fManager, "날짜 선택기");
            }
        };
        when1.setOnClickListener(listener_set_when);
        when2.setOnClickListener(listener_set_when);
        when3.setOnClickListener(listener_set_when);

        where1.setCursorVisible(false);
        where2.setCursorVisible(false);
        where3.setCursorVisible(false);

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                all_schedules = timetable.getAllSchedulesInStickers();
                deleted_schedules.clear();//다 쓰고 정리
                timetable.remove(idx);
                reset();

                for (Schedule sc : all_schedules){//String 바뀌기전 = 바뀌기 전 저장
                    deleted_schedules.add(sc);
                }
                all_schedules=timetable.getAllSchedulesInStickers(); //바꿈
                for (Schedule sc : all_schedules){
                    if (deleted_schedules.contains(sc))deleted_schedules.remove(sc);
                }

                //기본 정보 세팅
                Schedule sc = deleted_schedules.get(0);//어차피 시간만 다르고 기본 정보는 같으니 0의 스케줄을 겟
                professor_name.setText(sc.getProfessorName());
                lecture_name.setText(sc.getClassTitle());

                //장소, 시간 정보 세팅
                int cnt = deleted_schedules.size();
                printAll(deleted_schedules);

                switch (cnt) {
                    case 3 : {
                        add_time_room3.setVisibility(View.VISIBLE);
                        sc = deleted_schedules.get(2);
                        set_to_edit(sc, where3, when3);
                        setWhenInSchedule(sc3, sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(),
                                sc.getEndTime().getHour(), sc.getEndTime().getMinute());
                    }
                    case 2 : {
                        add_time_room2.setVisibility(View.VISIBLE);
                        sc = deleted_schedules.get(1);
                        set_to_edit(sc, where2, when2);
                        setWhenInSchedule(sc2, sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(),
                                sc.getEndTime().getHour(), sc.getEndTime().getMinute());
                    }
                    case 1 : {
                        sc = deleted_schedules.get(0);
                        set_to_edit(sc, where1, when1);
                        setWhenInSchedule(sc1, sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(),
                                sc.getEndTime().getHour(), sc.getEndTime().getMinute());
                    }
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(v.getContext())
                        .setTitle("")
                        .setMessage("지금 나가면 변경사항이 저장되지 않습니다. \n정말 나가겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();//저장 안 하고 나감. 뒤에 onstop override하려면 이 부분 바꿔야함.
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

        btn_add_time_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_copy_place.isChecked() && !copied) {
                    where2.setText(where1.getText().toString());
                    where3.setText(where1.getText().toString());
                }
                if (cnt==0) {
                    add_time_room2.setVisibility(View.VISIBLE);
                    cnt++;
                }
                else if (cnt==1) {
                    add_time_room3.setVisibility(View.VISIBLE);
                    btn_add_time_room.setVisibility(View.GONE);
                }
            }
        });

        btn_add_new_lecture.setOnClickListener(new View.OnClickListener() {//현재 쓰던 사항 저장 및 반영, 새 사항 시작.
            @Override
            public void onClick(View v) {
                //1. 시간표에 업로드
                //1개
                setTPInSchedule(sc1);
                sc1.setClassPlace(where1.getText().toString());
                schedules.add(sc1);//spinner 클릭됐을 때 요일, 시간 정보는 세팅됨.

                if (add_time_room2.getVisibility()==View.VISIBLE&&!입력없음(add_time_room2)){
                    setTPInSchedule(sc2);
                    sc2.setClassPlace(where2.getText().toString());
                    schedules.add(sc2);//spinner 클릭됐을 때 요일, 시간 정보는 세팅됨
                }
                if (add_time_room3.getVisibility()==View.VISIBLE&&!입력없음(add_time_room3)){
                    setTPInSchedule(sc3);
                    sc3.setClassPlace(where3.getText().toString());
                    schedules.add(sc3);//spinner 클릭됐을 때 요일, 시간 정보는 세팅됨.
                }
                Log.i("보기", "⭐⭐⭐⭐새로 넣은 거⭐⭐⭐⭐");
                printAll(schedules);

                //1. 안의 내용을 깊은 복사해서 전체에 넣은 다음 전부 전체를 초기화 후 전체를 넣음
//                for (Schedule sc : schedules){//깊은 복사
//                    Schedule sc_copy = new Schedule();
//                    sc_copy.setClassTitle(sc.getClassTitle());
//                    sc_copy.setProfessorName(sc.getProfessorName());
//                    sc_copy.setClassPlace(sc.getClassPlace());
//                    sc_copy.setDay(sc.getDay());
//                    sc_copy.setStartTime(sc.getStartTime());
//                    sc_copy.setEndTime(sc.getEndTime());
//                    all_schedules.add(sc_copy);
//                }

                //timetable.removeAll();
                //timetable.add(all_schedules);

                //2. ~ 다음 부분에 넣고 부분을 넣음.
                ArrayList<Schedule> temp = new ArrayList<>();
                for (Schedule sc : schedules){//깊은 복사
                    Schedule sc_copy = new Schedule();
                    sc_copy.setClassTitle(sc.getClassTitle());
                    sc_copy.setProfessorName(sc.getProfessorName());
                    sc_copy.setClassPlace(sc.getClassPlace());
                    sc_copy.setDay(sc.getDay());
                    sc_copy.setStartTime(sc.getStartTime());
                    sc_copy.setEndTime(sc.getEndTime());
                    temp.add(sc_copy);
                }

                Log.i("보기", "⭐⭐⭐⭐TEMP⭐⭐⭐⭐");
                printAll(temp);

                timetable.add(temp);
                Log.i("보기", "⭐⭐⭐⭐timetable 추가 후 전체 ⭐⭐⭐⭐");
                all_schedules = timetable.getAllSchedulesInStickers();
                printAll(all_schedules);

                //입력창 리셋
                reset();

                schedules.clear();
            }
        });
    }

    private boolean 입력없음(View v) {
        if (v.equals(add_time_room2)){
            return when2.getText().toString().equals("요일, 시간 설정")&&where2.getText().toString().isEmpty();
        }
        else {
            return when3.getText().toString().equals("요일, 시간 설정")&&where3.getText().toString().isEmpty();
        }
    }

    public String getFormattedString(String week_day, int start_hour, int start_min, int end_hour, int end_min){
        return week_day + " " + getAmPm(start_hour) + ":"+String.format("%02d", start_min) + " - " + getAmPm(end_hour) + ":" + String.format("%02d", end_min);
    }

    public String getFormattedString(int week_day, int start_hour, int start_min, int end_hour, int end_min){
        return 요일들[week_day] + " " + getAmPm(start_hour) + ":"+String.format("%02d", start_min) + " - " + getAmPm(end_hour) + ":" + String.format("%02d", end_min);
    }

    public int getAmPm (int hour){
        return hour%12==0 ? 12 : hour%12;
    }

    public void reset(){
        when1.setText(DEFAULT_DAY_TIME);
        when2.setText(DEFAULT_DAY_TIME);
        when3.setText(DEFAULT_DAY_TIME);

        where1.setText("");
        where2.setText("");
        where3.setText("");

        lecture_name.setText("");
        professor_name.setText("");

        add_time_room2.setVisibility(View.GONE);
        add_time_room3.setVisibility(View.GONE);
        btn_add_time_room.setVisibility(View.VISIBLE);

        cb_copy_place.setChecked(false);
        copied=false;

        cnt=0;
    }

    public void setTPInSchedule(Schedule sc){
        sc.setClassTitle(lecture_name.getText().toString());
        sc.setProfessorName(professor_name.getText().toString());
    }
    public void setWhenInSchedule(Schedule sc, String week_day, int start_hour, int start_min, int end_hour, int end_min){
        sc.setDay(Arrays.asList(요일들).indexOf(week_day));
        sc.setStartTime(new Time(start_hour,start_min));
        sc.setEndTime(new Time(end_hour, end_min));
    }

    public void setWhenInSchedule(Schedule sc, int week_day, int start_hour, int start_min, int end_hour, int end_min){
        sc.setDay(week_day);
        sc.setStartTime(new Time(start_hour,start_min));
        sc.setEndTime(new Time(end_hour, end_min));
    }

    public void load(){
        String json = DBlecture.getString("all", "없지롱");
        if (!json.equals("없지롱")){
            timetable.load(json);//load = timetable에만 load => 내부적으로 sticker와 schedule도 load.
            all_schedules = timetable.getAllSchedulesInStickers();
            Log.i("보기", "로드함.");
            printAll(all_schedules);
        }
    }

    public void save(){
        String json = timetable.createSaveData();
        editorL.putString("all", json);
        editorL.apply();
    }

    public void set_to_edit(Schedule sc, TextView where, TextView when){
        where.setText(sc.getClassPlace());
        when.setText(getFormattedString(sc.getDay(), sc.getStartTime().getHour(), sc.getStartTime().getMinute(), sc.getEndTime().getHour(), sc.getEndTime().getMinute()));
    }

    public void printAll(ArrayList<Schedule> arr){
        Log.i("보기", "===============리스트 출력===============");
        Log.i("보기", "데이터 개수 = "+arr.size()+"개\n");
        for (Schedule sc : arr){
            Log.i("보기", sc.getClassTitle()+ " by " + sc.getProfessorName());
            Log.i("보기", sc.getClassPlace()+"에서 "+요일들[sc.getDay()]+ " 요일 " + sc.getStartTime().getHour()+"시 ~ "+sc.getEndTime().getHour()+"시");
            Log.i("보기", "\n\n");
        }
        Log.i("보기", "=======================================");
    }
}
