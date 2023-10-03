package com.example.piece1timer.school;

import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.piece1timer.DrawerBaseActivity;
import com.example.piece1timer.R;
import com.example.piece1timer.databinding.ActivitySchoolMainBinding;
import com.example.piece1timer.diary.MyDiary;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SchoolMain extends DrawerBaseActivity {
    SharedPreferences DBlecture, DBuser;
    SharedPreferences.Editor editorL,editorU;
    Gson gson = new GsonBuilder().create();

    ActivitySchoolMainBinding activitySchoolMainBinding;

    ArrayList<Schedule> all_schedules = new ArrayList<>(), deleted_schedules = new ArrayList<>(), changed_schedules = new ArrayList<>();

    String[] 요일들 = new String[] {"월", "화", "수", "목", "금", "토", "일"};

    boolean clicked_create=false, clicked_enter=false, clicked_info=false;
    Friend 현재친구;

    //recyclerview
    RecyclerAdapterFriends fAdapter;
    RecyclerAdapterFriends.OnItemClickListener fListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySchoolMainBinding = ActivitySchoolMainBinding.inflate(getLayoutInflater());
        setContentView(activitySchoolMainBinding.getRoot());
        allocateActivityTitle("강의 시간표");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DBlecture = getSharedPreferences("DB", MODE_PRIVATE);
        editorL = DBlecture.edit();

        DBuser = getSharedPreferences("DBuser", MODE_PRIVATE);
        editorU = DBuser.edit();

        load();

        activitySchoolMainBinding.midTerm.setCursorVisible(false);
        activitySchoolMainBinding.finalTerm.setCursorVisible(false);

        fAdapter = new RecyclerAdapterFriends();
        fAdapter.setSP(DBuser, editorU, gson);
        fAdapter.setList_friend();
        activitySchoolMainBinding.recyclerviewFriends.setAdapter(fAdapter);
        activitySchoolMainBinding.recyclerviewFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fListener = new RecyclerAdapterFriends.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (clicked_info){
                    clicked_info = false;
                    setVisibility(clicked_info);
                }
                현재친구 = fAdapter.list_friend.get(pos);
                activitySchoolMainBinding.timeTable.load(현재친구.timetable_info);
                activitySchoolMainBinding.timetableOwner.setText("- "+현재친구.name);
                activitySchoolMainBinding.nowTable.setImageResource(현재친구.getProfile());
                activitySchoolMainBinding.btnAddLecture.setText("겹치는 강의 보기");
                activitySchoolMainBinding.midTerm.setEnabled(false);
                //강의 추가 버튼 바꾸기.
            }
        };
        fAdapter.setOnItemClickListener(fListener);

        setVisibility(clicked_info);

        activitySchoolMainBinding.btnAddLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activitySchoolMainBinding.btnAddLecture.getText().toString().equals("강의 추가")){
                    Intent intent = new Intent(getApplicationContext(), school_add_lecture.class);
                    startActivity(intent);
                }else {//겹치는 강의목록 보기
                    ArrayList<String> arr_together = getTogetherList(MyDiary.현재유저, 현재친구);
                    AlertDialog.Builder msgBuilder = new AlertDialog.Builder(v.getContext())
                            .setTitle("나랑 겹치는 강의 "+getTogetherCnt(arr_together)+"개")
                            .setMessage(getTogetherString(arr_together))
                            .setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                    AlertDialog msgDlg = msgBuilder.create();
                    msgDlg.show();
                }

            }
        });

        activitySchoolMainBinding.timeTable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                if (!clicked_info){
                    clicked_info = true;
                    setVisibility(false);
                }

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
                activitySchoolMainBinding.professorName.setText(sc.getProfessorName().replace("\n", "")+ " 교수님");
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
                editorL.putString(lecture_name, json);
                editorL.apply();

                InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        setInform();

        View v = findViewById(R.id.my_profile);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySchoolMainBinding.timeTable.load(MyDiary.현재유저.timetable_info);
                activitySchoolMainBinding.timetableOwner.setText("- 나");
                if (MyDiary.현재유저.getUri()!=null) activitySchoolMainBinding.nowTable.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));
                activitySchoolMainBinding.btnAddLecture.setText("강의 추가");

                if (clicked_info){
                    clicked_info = false;
                    setVisibility(clicked_info);
                }
            }
        });

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

    ArrayList<String> temp_together = new ArrayList<>();
    public ArrayList<String> getTogetherList(User user, Friend friend) {
        temp_together.clear();
        ArrayList<String> 친구거 = lectureNames(friend);
        ArrayList<String> 내거 = lectureNames(user);
        for (String name : 내거){
            if (친구거.contains(name)) temp_together.add(name);
        }

        return temp_together;
    }
    public ArrayList<String> lectureNames(User user){
        String json = user.timetable_info;
        ArrayList<String> titles = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("sticker");
            for (int i=0; i<arr.length(); i++){
                JSONObject obj2 = arr.getJSONObject(i);
                JSONArray arr2 = obj2.getJSONArray("schedule");
                JSONObject obj3 = arr2.getJSONObject(0);
                titles.add(obj3.getString("classTitle"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return titles;
    }
    public ArrayList<String> lectureNames(Friend friend){
        String json = friend.timetable_info;
        ArrayList<String> titles = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("sticker");
            JSONObject obj2 = arr.getJSONObject(0);
            JSONArray arr2 = obj2.getJSONArray("schedule");
            for (int i=0; i<arr2.length();i++){
                JSONObject obj3 = arr2.getJSONObject(i);
                titles.add(obj3.getString("classTitle"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return titles;
    }

    public int getTogetherCnt(ArrayList<String> arr) {
        return arr.size();
    }


    public String getTogetherString(ArrayList<String> arr) {
        String 내용 = "";
        for (String str : arr){
            내용+="- "+str+"\n";
        }
        return 내용;
    }

    public void reset() {
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
            MyDiary.현재유저.setTimetable_info(json);
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
            activitySchoolMainBinding.myProfile.myProfileImage.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));
        }
        activitySchoolMainBinding.myProfile.myName.setText("나");
    }

    public void setVisibility(boolean on){
        if (on){
            activitySchoolMainBinding.lectureName.setVisibility(View.VISIBLE);
            activitySchoolMainBinding.professorName.setVisibility(View.VISIBLE);
            activitySchoolMainBinding.time.setVisibility(View.VISIBLE);
            activitySchoolMainBinding.midTerm.setVisibility(View.VISIBLE);
            activitySchoolMainBinding.finalTerm.setVisibility(View.VISIBLE);
            activitySchoolMainBinding.btnSaveTermDate.setVisibility(View.VISIBLE);
        }
        else {
            activitySchoolMainBinding.lectureName.setVisibility(View.INVISIBLE);
            activitySchoolMainBinding.professorName.setVisibility(View.INVISIBLE);
            activitySchoolMainBinding.time.setVisibility(View.INVISIBLE);
            activitySchoolMainBinding.midTerm.setVisibility(View.INVISIBLE);
            activitySchoolMainBinding.finalTerm.setVisibility(View.INVISIBLE);
            activitySchoolMainBinding.btnSaveTermDate.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        load();
        setInform();
        if (MyDiary.현재유저.getUri()!=null) activitySchoolMainBinding.nowTable.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));
    }
}