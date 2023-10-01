package com.example.piece1timer.diary;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piece1timer.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class diary_write extends AppCompatActivity {
    TextView year_month, YearMonth;
    ImageButton arrange, time_stamp, btn_done, btn_cancel;
    ImageView todayMood;
    EditText content, feeling;
    LinearLayout diary;
    int cnt_arr_change = 0;
    int mood_name = 0;
    String file_title, 현재날짜;//현재날짜 = 현재 작성 중인 일기의 날짜
    RecyclerItemDiary written_diary;
    int CODE;

    SharedPreferences.OnSharedPreferenceChangeListener dListener;

    //Toolbar toolbar;

    //date관련
    TextView date;
    Calendar cal = Calendar.getInstance();
    //

    int 현재 = MyDiary.BASIC_MOOD_NAME_POSTION;
    int size = RecyclerAdapterEmotion.list_emotion.size();
    RecyclerItemEmotion emotion;
    SimpleDateFormat format_time_stamp = new SimpleDateFormat("hh:mm aaa", Locale.KOREAN);
    String 감정desc;

    int mood_name_chosen = 0;
    String 감정desc_chosen = "";

    SharedPreferences DBdiary = getSharedPreferences("DBdiary", Activity.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);
        Log.i("스크롤 테스트", "저장전 전체 다이어리 데이터 개수 "+MyDiary.mList.size());
        //xml 연결
        date = findViewById(R.id.DayDay);
        year_month = findViewById(R.id.year_month);
        YearMonth = findViewById(R.id.YearMonth);
        content = findViewById(R.id.content);
        arrange = findViewById(R.id.arrange);
        time_stamp = findViewById(R.id.time_stamp);
        btn_done = findViewById(R.id.btn_done);
        btn_cancel = findViewById(R.id.btn_cancel);
        diary = findViewById(R.id.diary);
        feeling = findViewById(R.id.today_emotion);
        todayMood = findViewById(R.id.feel_image);

        Intent intent = getIntent();
        CODE = intent.getIntExtra("CODE", -1);
        if (CODE==MyDiary.CODE_EDIT){//일기 목록 > 수정으로 넘어왔을 때
            content.setText(intent.getStringExtra("내용"));
        }
        dListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                MyDiary.setMList();
            }
        };

        DBdiary.registerOnSharedPreferenceChangeListener(dListener);
        //공통부분
            //날짜
        현재날짜 = intent.getStringExtra("yyyyMMdd");
        YearMonth.setText(MyDiary.getYearMonth(현재날짜));
        date.setText(MyDiary.getDayDay(현재날짜));//14 일

            //기분
        mood_name = intent.getIntExtra("mood_name", 0);
        todayMood.setImageDrawable(ContextCompat.getDrawable(this, mood_name));

        감정desc = intent.getStringExtra("감정desc");
        feeling.setText(감정desc);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                현재날짜 = year+String.format("%02d", month+1)+String.format("%02d", dayOfMonth);
                YearMonth.setText(MyDiary.getYearMonth(현재날짜));
                date.setText(MyDiary.getDayDay(현재날짜));

                if (DBdiary.contains(현재날짜)){//이 날짜에 쓴 일기가 있으면 > 수정
                    //연결
                    String[] diary = DBdiary.getString(현재날짜, "저장된 값 없음").split("\\|");

                    SharedPreferences DBemotion = getSharedPreferences("DBemotion", Activity.MODE_PRIVATE);
                    mood_name =  Integer.valueOf(DBemotion.getString(diary[0], "그런 키 없음").split(",")[0]);
                    todayMood.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), mood_name));

                    감정desc = diary[1];//감정
                    feeling.setText(감정desc);

                    content.setText(diary[2]);//내용

                    //삭제
                    //MyDiary.editorD.remove(현재날짜); 덮어쓰기니까!
                }
                else {//이 날짜에 쓴 일기가 없으면 > 생성 (reset. 틀 비우기)
                    reset();
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        if (CODE==MyDiary.CODE_START_NEW&&DBdiary.contains(현재날짜)){
            mood_name_chosen = getIntent().getIntExtra("mood_name", -1);
            감정desc_chosen = getIntent().getStringExtra("감정desc");
            datePickerDialog.show();
        }

        arrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt_arr_change++;
                if (cnt_arr_change%3==1){
                    content.setGravity(Gravity.START|Gravity.CENTER_HORIZONTAL);
                }
                else if (cnt_arr_change%3==2){
                    content.setGravity(Gravity.END|Gravity.CENTER_HORIZONTAL);
                }
                else {
                    content.setGravity(Gravity.CENTER);
                }
            }
        });

        time_stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText(content.getText().toString()+" "+time_stamp());
                content.setSelection(content.getText().length());
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //이미지 일기 저장
                //file_title = new SimpleDateFormat("yyyyMMHHmmss", Locale.KOREAN).format(new Date())+"_capture";
                //request_capture(diary);


                //저장 후 보기 화면으로 이동.
                saveDiary();//문제X.근데 그럼 왜 다시 켰을 때 없는 거지?

                Intent intent = new Intent(diary_write.this, diary_list.class);
                int pos = -1;
                for (RecyclerItemDiary diary : MyDiary.mList){
                    if (diary.yyyyMMdd.equals(written_diary.yyyyMMdd)) {
                        pos = MyDiary.mList.indexOf(diary);
                        break;
                    }
                }
                intent.putExtra("CODE", MyDiary.CODE_END_WRITE);
                intent.putExtra("pos", pos);
                startActivity(intent);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diary_quit_writing quit_wrting = new diary_quit_writing();
                quit_wrting.show(getSupportFragmentManager(), quit_wrting.TAG_EVENT_DIALOG);
            }
        });


        todayMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotion = RecyclerAdapterEmotion.list_emotion.get(현재%size);

                mood_name = emotion.mood_name;
                todayMood.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), mood_name));

                감정desc = emotion.감정desc;
                feeling.setText(감정desc);
                현재++;
            }
        });

//        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle("일기쓰기");
    }

    public void saveDiary() {
        written_diary = new RecyclerItemDiary(this, 현재날짜, mood_name, 감정desc, content.getText().toString());
        MyDiary.editorD.putString(written_diary.yyyyMMdd,written_diary.toOneString());//로컬DB 저장 완료
        MyDiary.editorD.apply();
    }

    private void reset() {
        if (CODE == MyDiary.CODE_START_NEW){
            mood_name = mood_name_chosen;
            감정desc = 감정desc_chosen;
        }
        else {
            mood_name = MyDiary.BASIC_MOOD_NAME;//이모지
            감정desc = "평온해";//감정
        }


        todayMood.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), mood_name));
        현재=0;
        feeling.setText(감정desc);
        content.setText(" ");//내용
    }

    public String time_stamp () {
        String stamp = format_time_stamp.format(cal.getTime());
        return stamp;
    }

    @Override
    protected void onStop() {
        super.onStop();
        reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (diary_quit_writing.quit_dialog_opened) saveDiary();
    }
}