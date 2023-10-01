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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

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

        //샘플 세팅
        editorU.clear();
//        User user = new User("aaa123", "12345", "현진");
//        editorU.putString(user.getID(), gson.toJson(user));

        ArrayList<User> friends_sample = new ArrayList<>();
        User 혜준 = new User("a", "", "혜준", );
        User 유진 = new User("ab", "", "유진", );
        User 서영 = new User("abc", "", "서영", );
        User 수현 = new User("abcd", "", "수현", );
        User 민정 = new User("abced", "", "민정", );
        User 은별 = new User("abcedf", "", "은별", );

        editorU.apply();

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
        User user = gson.fromJson(DBuser.getString(got_ID, null), User.class);
        MyDiary.현재유저 = user;
        return DBuser.contains(got_ID) && user.getPW().equals(got_PW);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}