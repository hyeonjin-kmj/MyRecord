package com.example.piece1timer.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.piece1timer.DrawerBaseActivity;
import com.example.piece1timer.databinding.ActivitySettingBinding;
import com.example.piece1timer.diary.MyDiary;

public class Setting extends DrawerBaseActivity {

    ActivitySettingBinding activitySettingBinding;
    SharedPreferences DBuser;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());
        allocateActivityTitle("환경설정");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DBuser = getSharedPreferences("DBuser", MODE_PRIVATE);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                setInform();
            }
        };
        DBuser.registerOnSharedPreferenceChangeListener(listener);


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
        if (MyDiary.현재유저.getUri()!=null) activitySettingBinding.profileImage.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));
        activitySettingBinding.name.setText(MyDiary.현재유저.getName());
    }
}