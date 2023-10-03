package com.example.piece1timer.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.piece1timer.R;
import com.example.piece1timer.school.Friend;
import com.example.piece1timer.school.User;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class splash extends AppCompatActivity {

    SharedPreferences DBuser;
    SharedPreferences.Editor editorU;
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        DBuser = getSharedPreferences("DBuser", Activity.MODE_PRIVATE);
//        editorU = DBuser.edit();
//
//        Log.i("보기", "스플래시 들어옴.");
//
//        /*
//        7개 강의 세팅
//        강의명 = "강의명(분반)"으로 입력.
//        1. 데이터베이스(103) 김창수 || 수(2) 9:00 ~ 11:00 || 웅비관 103
//        2. 컴퓨터프로그래밍(108) 김영봉 || 화(1) 14:00 ~ 17:00 || 창의관 205
//        3. 컴퓨터그래픽스(105) 신철권 || 금(4) 11:30 ~ 13:00 || 미래관 201
//        4. 자료구조(206) 정목동 || 화(1) 10:00 ~ 12:00 || 나래관 105
//        5. 알고리즘(207) 최은호 || 목(3) 11:00 ~ 13:00 || 웅비관 108
//        6. 임베디드시스템(109) 김민경 || 수(2) 15:30 ~ 17:00 || 웅비관 201
//        7. 정보보호론(203) 조승연 || 월(0) 9:00 ~ 10:30 || 웅비관 203
//
//        교양
//        1. 현대생활과패션 안장혁 ||
//        2. 유아행동과발달 박철홍 ||
//        3. 삶과죽음의철학 양근숙 ||
//        4. 부산해양역사 정향미 ||
//        5. 현대미술산책 장원두 ||
//        6. 법률영화산책 이은숙 ||
//        7. 여행의기술 민혜연 ||
//        8. 미디어사진학 서유진 ||
//        9. 감성심리학 김남희 ||
//        10. 신화와종교 김명자 ||
//         */
//        Schedule 현대생활과패션 = setSchedule("현대생활과패션", "안장혁", "위드센터 2층", 2, 13,0,15,0);
//
//        Schedule 유아행동과발달 = setSchedule("유아행동과발달", "박철홍", "가온관 202", 0, 14,0,16,30);
//
//        Schedule 삶과죽음의철학 = setSchedule("삶과죽음의철학", "양근숙", "한솔관 103", 3, 15, 0, 16, 30);
//
//        Schedule 부산해양역사 = setSchedule("부산해양역사", "정향미", "세종 2관 101", 2, 14, 0, 16, 0);
//
//        Schedule 현대미술산책 = setSchedule("현대미술산책", "장원두", "디자인관 202", 2, 12, 0, 14, 0);
//
//        Schedule 법률영화산책 = setSchedule("법률영화산책", "이은숙", "충무관 103", 2, 14, 30, 16,0);
//
//        Schedule 여행의기술 = setSchedule("여행의기술", "민혜연", "환경해영관 201", 1, 11, 0, 12, 30);
//
//        Schedule 미디어사진학 = setSchedule("미디어사진학", "서유진", "경영관 103", 4, 14,0,16,0);
//
//        Schedule 감성심리학 = setSchedule("감성심리학", "김남희", "공학2관 105", 4, 15, 0, 17, 0);
//
//        Schedule 신화와종교 = setSchedule("신화와종교", "김명자", "동원장보고관 201", 3, 10, 0, 12, 0);
//
//
//        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
//
//        Schedule 데이터베이스 = new Schedule();
//        데이터베이스.setClassTitle("데이터베이스(103)");
//        데이터베이스.setProfessorName("김창수");
//        데이터베이스.setDay(2);
//        데이터베이스.setStartTime(new Time(9,0));
//        데이터베이스.setEndTime(new Time(11,0));
//        데이터베이스.setClassPlace("웅비관 103");
//
//        Schedule 컴퓨터프로그래밍 = new Schedule();
//        컴퓨터프로그래밍.setClassTitle("컴퓨터프로그래밍(108)");
//        컴퓨터프로그래밍.setProfessorName("김영봉");
//        컴퓨터프로그래밍.setDay(1);
//        컴퓨터프로그래밍.setStartTime(new Time(14,0));
//        컴퓨터프로그래밍.setEndTime(new Time(17,0));
//        컴퓨터프로그래밍.setClassPlace("창의관 205");
//
//        Schedule 컴퓨터그래픽스 = new Schedule();
//        컴퓨터그래픽스.setClassTitle("컴퓨터그래픽스(105)");
//        컴퓨터그래픽스.setProfessorName("신철권");
//        컴퓨터그래픽스.setDay(4);
//        컴퓨터그래픽스.setStartTime(new Time(11,30));
//        컴퓨터그래픽스.setEndTime(new Time(13,0));
//        컴퓨터그래픽스.setClassPlace("미래관 201");
//
//        Schedule 자료구조 = new Schedule();
//        자료구조.setClassTitle("자료구조(206)");
//        자료구조.setProfessorName("정목동");
//        자료구조.setDay(1);
//        자료구조.setStartTime(new Time(10,0));
//        자료구조.setEndTime(new Time(12,0));
//        자료구조.setClassPlace("나래관 105");
//
//        Schedule 알고리즘 = new Schedule();
//        알고리즘.setClassTitle("알고리즘(207)");
//        알고리즘.setProfessorName("최은호");
//        알고리즘.setDay(3);
//        알고리즘.setStartTime(new Time(11,0));
//        알고리즘.setEndTime(new Time(13,0));
//        알고리즘.setClassPlace("웅비관 108");
//
//        Schedule 임베디드시스템 = new Schedule();
//        임베디드시스템.setClassTitle("임베디드시스템(109)");
//        임베디드시스템.setProfessorName("김민경");
//        임베디드시스템.setDay(2);
//        임베디드시스템.setStartTime(new Time(15,30));
//        임베디드시스템.setEndTime(new Time(17,0));
//        임베디드시스템.setClassPlace("웅비관 201");
//
//        Schedule 정보보호론 = new Schedule();
//        정보보호론.setClassTitle("정보보호론(203)");
//        정보보호론.setProfessorName("조승연");
//        정보보호론.setDay(0);
//        정보보호론.setStartTime(new Time(9,0));
//        정보보호론.setEndTime(new Time(10,30));
//        정보보호론.setClassPlace("웅비관 203");
//
//        schedules.add(데이터베이스);
//        schedules.add(컴퓨터프로그래밍);
//        schedules.add(컴퓨터그래픽스);
//        schedules.add(자료구조);
//        schedules.add(알고리즘);
//        schedules.add(임베디드시스템);
//        schedules.add(정보보호론);
//
//        Log.i("보기", "스케줄 리스트 7개 다 만듦");
//
//        //원하는 정렬 결과
//        //혜준, 수현, 서영, 민정, 은별, 유진 > 혜준, 다은, 수현, 서영, 민정, 은별, 유진
//
//        TimetableView 혜준스 = findViewById(R.id.hyejun);//7
//        ArrayList<Schedule> schedules혜준 = new ArrayList<>();
//        for (int i=0; i<7; i++){
//            schedules혜준.add(schedules.get(i));
//        }
//        schedules혜준.add(유아행동과발달);
//        혜준스.add(schedules혜준);
//
//        TimetableView 유진스 = findViewById(R.id.yujin);//1
//        ArrayList<Schedule> schedules유진 = new ArrayList<Schedule>();
//        for (int i=0; i<1; i++){
//            schedules유진.add(schedules.get(i));
//        }
//        schedules유진.add(현대미술산책);
//        schedules유진.add(법률영화산책);
//        유진스.add(schedules유진);
//
//        TimetableView 서영스 = findViewById(R.id.seoyoung);//4
//        ArrayList<Schedule> schedules서영 = new ArrayList<Schedule>();
//        for (int i=0; i<4; i++){
//            schedules서영.add(schedules.get(i));
//        }
//        schedules서영.add(미디어사진학);
//        서영스.add(schedules서영);
//
//        TimetableView 수현스 = findViewById(R.id.suhyeon);//5
//        ArrayList<Schedule> schedules수현 = new ArrayList<Schedule>();
//        for (int i=0; i<5; i++){
//            schedules수현.add(schedules.get(i));
//        }
//        schedules수현.add(현대생활과패션);
//        수현스.add(schedules수현);
//
//        TimetableView 민정스 = findViewById(R.id.minjeong);//3
//        ArrayList<Schedule> schedules민정 = new ArrayList<Schedule>();
//        for (int i=0; i<3; i++){
//            schedules민정.add(schedules.get(i));
//        }
//        schedules민정.add(부산해양역사);
//        schedules민정.add(감성심리학);
//        schedules민정.add(신화와종교);
//        민정스.add(schedules민정);
//
//        TimetableView 은별스 = findViewById(R.id.silver_star);//2
//        ArrayList<Schedule> schedules은별 = new ArrayList<Schedule>();
//        for (int i=0; i<2; i++){
//            schedules은별.add(schedules.get(i));
//        }
//        schedules은별.add(삶과죽음의철학);
//        schedules은별.add(여행의기술);
//        은별스.add(schedules은별);
//
//        TimetableView 추가_다은스 = findViewById(R.id.add_daeun);//6
//        ArrayList<Schedule> schedules추가_다은 = new ArrayList<Schedule>();
//        for (int i=0; i<6; i++){
//            schedules추가_다은.add(schedules.get(i));
//        }
//        추가_다은스.add(schedules추가_다은);
//
//        Log.i("보기", "애들마다 timetable 다 짜줌");
//
//
//        Friend 혜준 = new Friend(321,"혜준", R.drawable.profile_zzanggu_cake);
//        혜준.setTimetable_info(혜준스.createSaveData());
//        Log.i("보기", 혜준스.createSaveData());
//        Friend 유진 = new Friend(234,"유진", R.drawable.profile_suzi_wink);
//        유진.setTimetable_info(유진스.createSaveData());
//        Friend 서영 = new Friend(351, "서영", R.drawable.profile_yuri);
//        서영.setTimetable_info(서영스.createSaveData());
//        Friend 수현 = new Friend(983, "수현", R.drawable.profile_hoon);
//        수현.setTimetable_info(수현스.createSaveData());
//        Friend 민정 = new Friend(592,"민정", R.drawable.profile_maenggu);
//        민정.setTimetable_info(민정스.createSaveData());
//        Friend 은별 = new Friend(781, "은별", R.drawable.profile_suzi_xmas);
//        은별.setTimetable_info(은별스.createSaveData());
//        Friend 추가_다은 = new Friend(991, "다은", R.drawable.profile_cheolsu);
//        추가_다은.setTimetable_info(추가_다은스.createSaveData());
//
//        Log.i("보기", "friend 객체로 생성함.");
//
//        ArrayList<Integer> my_friend_unique_nums = new ArrayList<>();
//        my_friend_unique_nums.add(혜준.getUnique_num());
//        my_friend_unique_nums.add(유진.getUnique_num());
//        my_friend_unique_nums.add(서영.getUnique_num());
//        my_friend_unique_nums.add(수현.getUnique_num());
//        my_friend_unique_nums.add(민정.getUnique_num());
//        my_friend_unique_nums.add(은별.getUnique_num());
//
//        editorU.clear();//1회성 코드. 1번 하고 지워야 함.
//        User user = new User(123, "aaa123", "12345", "현진");
//        editorU.putString(user.getUnique_num()+"", user.toJson().replace("&quot;", ""));
//        editorU.putString(혜준.getUnique_num()+"", gson.toJson(혜준));
//        editorU.putString(유진.getUnique_num()+"", gson.toJson(유진));
//        editorU.putString(서영.getUnique_num()+"", gson.toJson(서영));
//        editorU.putString(수현.getUnique_num()+"", gson.toJson(수현));
//        editorU.putString(민정.getUnique_num()+"", gson.toJson(민정));
//        editorU.putString(은별.getUnique_num()+"", gson.toJson(은별));
//        editorU.putString(추가_다은.getUnique_num()+"", gson.toJson(추가_다은));
//        Log.i("보기", "내 친구목록에도 올리고, DB에도 user로 올려줌.");
//        editorU.apply();
//
//        User 나 = new User(DBuser.getString(123+"", "없음 메롱"));
//        나.setList_friends(my_friend_unique_nums);
//        String json = 나.toJson();
//        editorU.putString(123+"", json);
//        Log.i("보기", "splash화면에서 넣은 나\n"+json);
//        editorU.apply();
    }

//    public Schedule setSchedule(String lecture, String professor, String place, int 요일, int sh, int sm, int eh, int em){
//        Schedule schedule = new Schedule();
//        schedule.setClassTitle(lecture);
//        schedule.setProfessorName(professor);
//        schedule.setClassPlace(place);
//        schedule.setDay(요일);
//        schedule.setStartTime(new Time(sh, sm));
//        schedule.setEndTime(new Time(eh, em));
//        return  schedule;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        //핸들러 1. 애니메이션
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}