package com.example.piece1timer.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.piece1timer.DrawerBaseActivity;
import com.example.piece1timer.databinding.ActivitySettingBinding;
import com.example.piece1timer.diary.MyDiary;
import com.example.piece1timer.school.User;

public class Setting extends DrawerBaseActivity {

    ActivitySettingBinding activitySettingBinding;
    SharedPreferences DBuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());
        allocateActivityTitle("환경설정");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DBuser = getSharedPreferences("DBuser", MODE_PRIVATE);


        activitySettingBinding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), setting_edit_profile.class);
                startActivity(intent);
            }
        });
        setInform();
    }

    public void setInform(){
        if (MyDiary.현재유저.getUri()!=null) {
            Log.i("보기", "uri가 눌이니? "+MyDiary.현재유저.getUri());
            activitySettingBinding.profileImage.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));
        }
        activitySettingBinding.name.setText(MyDiary.현재유저.getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("보기", "onStart");
        MyDiary.현재유저 = new User(DBuser.getString(MyDiary.현재유저.getUnique_num()+"", "없음 메롱"));
        setInform();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("보기", "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("보기", "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i("보기", "onStop");
    }

}