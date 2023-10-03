package com.example.piece1timer.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.piece1timer.R;
import com.example.piece1timer.calendar.MainActivity;
import com.example.piece1timer.diary.MyDiary;
import com.example.piece1timer.school.User;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;

public class login extends AppCompatActivity {
    TextInputEditText input_id, input_pw;
    Button btn_login;
    TextView login_fail;

    SharedPreferences DBuser;
    SharedPreferences.Editor editorU;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        input_id = findViewById(R.id.input_ID);
        input_pw = findViewById(R.id.input_PW);
        btn_login = findViewById(R.id.btn_login);
        login_fail = findViewById(R.id.login_fail);

        DBuser = getSharedPreferences("DBuser", Activity.MODE_PRIVATE);
        editorU = DBuser.edit();

        gson = new GsonBuilder().create();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String got_ID = input_id.getText().toString();
                String got_PW = input_pw.getText().toString();
                if (login_succeed(got_ID, got_PW)){//정보가 있다면
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else {
                    login_fail.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public boolean login_succeed(String got_ID, String got_PW) {
        if (got_ID==null&&got_PW==null) return false;//입력이 안 들어왔을 때

        Map<String, ?> all_users = DBuser.getAll();
        User user;
        for (String key : all_users.keySet()){
            user = new User(DBuser.getString(key, "없음 메롱"));

            if (user.getID()==null) continue;
            if (user.getID().equals(got_ID)&&user.getPW().equals(got_PW)) {
                MyDiary.현재유저 = user;
                //Log.i("보기", "로그인 성공 : "+MyDiary.현재유저.getName()+", "+MyDiary.현재유저.getFriend_unqs().toString());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}