package com.example.piece1timer.timer;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piece1timer.R;
import com.example.piece1timer.calendar.RecyclerAdapterTimer;
import com.example.piece1timer.calendar.RecyclerItemTimer;
import com.example.piece1timer.diary.MyDiary;
import com.example.piece1timer.diary.RecyclerItemDiary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class timer extends AppCompatActivity {
    //선언
    int set_time;
    long time_left;
    SeekBar timer_sb;
    TextView timer_tv, todayTotal;
    Button start_btn, cancel_btn;
    CountDownTimer countDownTimer;
    boolean time_set = false;
    boolean come_back = false;
    boolean 방향변화 = false;
    String 상태 = "";
    String 현재날짜;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent != null){
                        if (result.getResultCode()==0) {타이머완료();}
                        else if (result.getResultCode()==1) reset();//설정했던 타이머가 취소됨.
                        else {//진행 중이던 타이머가 화면 방향 바뀌면서 넘어옴.
                            time_left = intent.getLongExtra("남은시간",0);
                            if (result.getResultCode()==3){//잠시 멈춤 상태로 넘어옴.
                                update((int)time_left/1000);
                            }
                            else if (result.getResultCode()==2){//진행 중인 상태로 넘어옴.
                                if (countDownTimer != null) 재시작();
                                else {첫시작();}
                            }
                        }
                    }
                }
            }
    );

    public static SharedPreferences DBtimer;
    public static SharedPreferences.Editor editorTimer;
    SharedPreferences.OnSharedPreferenceChangeListener listenerTimer;

    RecyclerView recyclerView;
    RecyclerAdapterTimer adapterTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //레이아웃과 연결
        timer_sb = findViewById(R.id.timer_sb);
        timer_tv = findViewById(R.id.timer_tv);
        start_btn = findViewById(R.id.play);
        cancel_btn = findViewById(R.id.cancel);
        todayTotal = findViewById(R.id.todayTotal);



        DBtimer = getSharedPreferences("DBtimer", Activity.MODE_PRIVATE);
        editorTimer = DBtimer.edit();

        listenerTimer = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                todayTotal.setText(String.format("%02d", DBtimer.getInt(현재날짜(), 0)/60)+"시간 "+String.format("%02d", DBtimer.getInt(현재날짜(), 0))+"분");

            }
        };
        DBtimer.registerOnSharedPreferenceChangeListener(listenerTimer);


        recyclerView = findViewById(R.id.recyclerview_timer);
        adapterTimer = new RecyclerAdapterTimer(getList());
        recyclerView.setAdapter(adapterTimer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        //editorTimer.putInt("20230901", 156);
        //editorTimer.putInt("20230903", 360);
        //editorTimer.putInt("20230910", 120);
        //editorTimer.apply();

        todayTotal.setText(String.format("%02d", DBtimer.getInt(현재날짜(), 0)/60)+"시간 "+String.format("%02d", DBtimer.getInt(현재날짜(), 0))+"분");
        //기능 정의
        timer_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {//SeekBar에서의 사용자 행동에 따른 변화
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //진행 중, 텍스트 부분 바꿈.
                initialize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //SeekBar를 처음 터치했을 때 발생
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                set_time = seekBar.getProgress();
                time_set = true;
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (상태.equals("")) 상태 = "처음시작";
                if (time_set) {
                    switch(상태){
                        case  "처음시작"://최초 한 번 실행
                            첫시작();
                            break;

                        case  "잠시멈춤"://계속 실행
                            잠시멈춤(); break;

                        case  "재시작":
                            재시작(); break;
                    }
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!상태.equals("")&&time_set) {
                    countDownTimer.cancel();
                    reset();
                }
            }
        });
    }

    String key;
    int value;
    ArrayList<RecyclerItemTimer> list_times = new ArrayList<>();
    private ArrayList<RecyclerItemTimer> getList() {
        list_times.clear();
        Map<String, ?> gotTs = DBtimer.getAll();
        Iterator<String> ite = gotTs.keySet().iterator();//iterator = 순회자.
        while (ite.hasNext()){
            key = ite.next();
            value = (Integer) gotTs.get(key);
            list_times.add(new RecyclerItemTimer(key, value));
        }
        sorting();

        return list_times;
    }

    public void sorting(){
        Collections.sort(list_times, new Comparator<RecyclerItemTimer>() {
            @Override
            public int compare(RecyclerItemTimer o1, RecyclerItemTimer o2) {
                if (Integer.valueOf(o1.yyyyMMdd).compareTo(Integer.valueOf(o2.yyyyMMdd)) < 0) return -1;
                return 1;
            }
        });
    }

    private String 현재날짜() {
        현재날짜 = MyDiary.format_yyyyMMdd.format(Calendar.getInstance().getTime());
        return 현재날짜;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (come_back) {
            countDownTimer.cancel();
            countDownTimer = new CountDownTimer(timer_sb.getProgress()*1000*60, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    update((int)millisUntilFinished/1000);
                    time_left = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    타이머완료();
                }
            }.start();
            come_back = false;
        }
    }

    @Override
    protected void onStop() {//잠시멈춤이 사용자가 의도 = onPause만으로 바뀌었는데 사용자가 가만히 뒀거나, 아니면 버튼이 눌린 경우
        super.onStop();
        come_back = true;
        if (countDownTimer!=null)countDownTimer.cancel();
        countDownTimer = new CountDownTimer(timer_sb.getProgress()*1000*60, 1000) {//세팅한 시간, 시간 내려가는 간격
            @Override
            public void onTick(long millisUntilFinished) {
                    time_left = millisUntilFinished;
                }
                @Override
                public void onFinish() {
                    타이머완료();
                }
            }.start();
    }

    @Override
    protected void onDestroy() {//화면 회전 -> 집중모드로 활동 넘어가고, 여기서는 멈춤 상태로 계속 유지.
        super.onDestroy();
        if (time_set && (상태.equals("처음시작")||상태.equals("재시작"))){
            countDownTimer.cancel();
        }
        reset();
    }

    //onCreate()에서 사용되는 메서드 정의

    //처음
    public void initialize(int progress) {//progress = (맨 처음에 seekBar 조정할 때) 1 ~ 60까지의 분
        timer_sb.setProgress(progress);
        timer_tv.setText(String.format("%02d", progress) +" : 00");
    }

    public void update(int progress) {//seekbar 남은 시간이랑, 남은 시간 텍스트 부분 업데이트
        int minutes = progress/60;
        int seconds = progress%60;

        if (seconds==59) timer_sb.setProgress(minutes);
        timer_tv.setText(String.format("%02d", minutes) +" : "+String.format("%02d", seconds));
    }

    //끝
    public void 집중모드타이머로_넘어감(){
        잠시멈춤();
        Intent intent = new Intent(getApplicationContext(), focusTimer.class);
        intent.putExtra("남은시간", time_left);
        intent.putExtra("진행상태", 상태);//상태= 처음시작, 재시작 & 잠시멈춤
        launcher.launch(intent);
    }

    public void reset() {
        set_time = 0;
        time_left = 0;
        timer_sb.setProgress(15);
        timer_tv.setText("15:00");
        timer_tv.setTextColor(Color.BLACK);
        start_btn.setText("▶");
        timer_sb.setEnabled(true);
        countDownTimer.cancel();
        time_set = false;
        come_back = false;
        상태="";
    }

    public String getMessage(){
        String message = "";
        message = DBtimer.getInt(현재날짜(), 0) < 60 ? "오늘 총 " + String.format("%02d",DBtimer.getInt(현재날짜(), 0))+"분 집중했어요." :
                "오늘 총 "+ String.format("%02d", DBtimer.getInt(현재날짜(), 0)/60)+"시간 "
                +String.format("%02d", DBtimer.getInt(현재날짜(), 0)%60)+"분 집중했어요.";

        return message;
    }

    //타이머 상태별 동작
    private void 첫시작() {
        timer_sb.setEnabled(false);
        start_btn.setText("||");
        상태 = "잠시멈춤";//다음상태로 설정.
        //타이머 생성 및 시작
        countDownTimer = new CountDownTimer(timer_sb.getProgress()*1000*60, 1000) {//세팅한 시간, 시간 내려가는 간격
            @Override
            public void onTick(long millisUntilFinished) {
                update((int)millisUntilFinished/1000);
                time_left = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                타이머완료();
            }
        }.start();//타이머 스타트 = 내부적으로 핸들러 호출 -> onTick -> 1초 뒤 메시지 전달 -> messageQue에서 처리 = onTick -> 1초 뒤 처리 일케 되는 거. || 핸들러->메인스레드의 looper에 메세지 전달 하기 때문에 메인 스레드, 즉 이 액티비티가 살아있는 한 타이머는 유지된다.
    }

    public void 잠시멈춤(){
        start_btn.setText("▶");
        timer_tv.setTextColor(Color.LTGRAY);
        상태 = "재시작";
        countDownTimer.cancel();
    }

    public void 재시작(){
        start_btn.setText("||");
        timer_tv.setTextColor(Color.BLACK);
        상태 = "잠시멈춤";
        countDownTimer = new CountDownTimer(time_left, 1000) {//세팅한 시간, 시간 내려가는 간격
            @Override
            public void onTick(long millisUntilFinished) {
                update((int)millisUntilFinished/1000);
                time_left = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                타이머완료();
            }
        }.start();
    }

    private void 타이머완료() {
        editorTimer.putInt(현재날짜(), DBtimer.getInt(현재날짜(), 0)+set_time);
        editorTimer.apply();
        Toast.makeText(timer.this.getApplicationContext(), getMessage(), Toast.LENGTH_LONG).show();
        reset();
    }

    //방향 변화 시, focus 타이머 나오게하기
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && time_set) {//타이머 돌아가고 있을 때만 화면 전환 시, 집중 타이머로 동작.
            집중모드타이머로_넘어감();
        }
    }
}