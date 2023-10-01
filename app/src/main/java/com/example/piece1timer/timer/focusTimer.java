package com.example.piece1timer.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.piece1timer.R;

public class focusTimer extends AppCompatActivity {
    long time_left;
    TextView timer_tv;
    int code_result;
    boolean 진행중 = true, 타이머정상종료 = false;

    Intent intent = new Intent();
    CountDownTimer fCountDownTimer;
    Button state_btn, cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_focus);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //xml 연결
        timer_tv = findViewById(R.id.timer_tv);//타이머 숫자 timer_tv
        state_btn = findViewById(R.id.play);//잠시멈춤 관리. state_btn
        cancel_btn = findViewById(R.id.cancel);

        // 기능 설정
        state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!진행중) {//=> 진행중으로
                    진행();
                }
                else {//멈췄을 때
                    일시정지();
                }
            }
        });

        time_left = getIntent().getLongExtra("남은시간", 0);//getIntent = 현재 자기 자신을 호출한 인텐트를 불러옴.

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fCountDownTimer != null) {
                    code_result = 1;//1 중간에 아예 타이머를 취소해버림.
                    setResult(code_result, intent);
                    finish();
                }
            }
        });
    }

    private void 일시정지() {
        state_btn.setText("▶");
        timer_tv.setTextColor(Color.LTGRAY);
        진행중 = false;
        fCountDownTimer.cancel();
    }

    private void 진행() {
        state_btn.setText("||");
        진행중 = true;
        timer_tv.setTextColor(Color.WHITE);
        fCountDownTimer = new CountDownTimer(time_left, 1000) {//세팅한 시간, 시간 내려가는 간격
            @Override
            public void onTick(long millisUntilFinished) {
                update((int)millisUntilFinished/1000);
                time_left = millisUntilFinished;
            }

            @Override
            public void onFinish() {//액티비티를 종료하면서 원래 액티비티로 돌아가야함. 그러면서 결과값으로, "끝까지 작동됨. totalTime에 넣어주쇼." 해야 함.
                타이머정상종료 = true;
                code_result = 0;
                setResult(code_result, intent);
                finish();
            }
        }.start();
    }

    //onCreate()내 사용되는 메서드들
    public void update(int progress) {//seekbar 남은 시간이랑, 남은 시간 텍스트 부분 업데이트
        int minutes = progress/60;
        int seconds = progress%60;
        timer_tv.setText(String.format("%02d", minutes) +" : "+String.format("%02d", seconds));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //상태 받아서 설정
        String 상태 = getIntent().getStringExtra("진행상태");
        switch(상태){
            case  "처음시작":
            case  "재시작":
                진행();
                break;

            case  "잠시멈춤":
                일시정지();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT&&타이머정상종료!=true) {//비정상 종료
            intent.putExtra("남은시간", time_left);
            if (진행중){
                code_result = 2;//화면이 돌아가서 집중모드에서 원래 타이머로 돌아감. 타이머 상태 = 진행중
                setResult(code_result, intent);
            }
            else {
                code_result = 3;//화면이 돌아가서 집중모드에서 원래 타이머로 돌아감. 타이머 상태 = 멈춤
                setResult(code_result, intent);
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();//인텐트 받으면 무조건 실행
        fCountDownTimer.cancel();
    }
}