package com.example.piece1timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.example.piece1timer.calendar.MainActivity;
import com.example.piece1timer.diary.MainDiary;
import com.example.piece1timer.school.SchoolMain;
import com.example.piece1timer.setting.Setting;
import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activity_container);
        container.addView(view);

        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.nav_calendar){
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0,0);
        }
        else if (item.getItemId() == R.id.nav_diary){
            startActivity(new Intent(this, MainDiary.class));
            overridePendingTransition(0,0);
        }
        else if (item.getItemId() == R.id.nav_school){
            startActivity(new Intent(this, SchoolMain.class));
            overridePendingTransition(0,0);
        }
        else if (item.getItemId() == R.id.nav_setting){
            startActivity(new Intent(this, Setting.class));
            overridePendingTransition(0,0);
        }

        return false;
    }

    public void allocateActivityTitle (String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_switch, menu);
// ⭐((EditText)v).setInputType();
//        MenuItem toolbar_switch = menu.findItem(R.id.cal_switch);
//        toolbar_switch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                onOptionsItemSelected(toolbar_switch);
//                return false;
//            }
//        });
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
    public void allocateActivityTitleCal (String titleString) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(titleString);
        //actionBar.setCustomView(R.layout.switch_mode);
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.switch_mode, null);
//        Switch mode = view.findViewById(R.id.mode);
//
//        //모드 체인지
//        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    Log.i("상태", "Checked");
//                }
//                else {
//                    Log.i("상태", "!Checked");
//                }
//                if (isChecked) {
//                    activityMainBinding.diary.setVisibility(View.VISIBLE);
//                    activityMainBinding.timer.setVisibility(View.GONE);
//
//                    activityMainBinding.modeDiary.setVisibility(View.VISIBLE);
//                    activityMainBinding.modeTodo.setVisibility(View.GONE);
//                }
//                else {
//                    activityMainBinding.diary.setVisibility(View.GONE);
//                    activityMainBinding.timer.setVisibility(View.VISIBLE);
//
//                    activityMainBinding.modeDiary.setVisibility(View.GONE);
//                    activityMainBinding.modeTodo.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

}