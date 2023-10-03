package com.example.piece1timer.setting;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.GnssAntennaInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piece1timer.R;
import com.example.piece1timer.diary.MyDiary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class setting_edit_profile extends AppCompatActivity {
    SharedPreferences DBuser;
    SharedPreferences.Editor editorU;

    Button btn_change_profile_image, btn_logout, btn_leave, btn_done_edit_profile;
    ImageView profile_image;
    TextView pw_again;
    ActivityResultLauncher<Intent> launcher;
    String uri;
    EditText name, id, pw, pw_check;

    Intent intent_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit_profile);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DBuser = getSharedPreferences("DBuser", Activity.MODE_PRIVATE);
        editorU = DBuser.edit();

        intent_out = new Intent(this, login.class);

        profile_image = findViewById(R.id.profile_image);
        btn_change_profile_image = findViewById(R.id.btn_change_profile_image);
        btn_logout = findViewById(R.id.btn_log_out);
        btn_leave = findViewById(R.id.btn_leave);
        btn_done_edit_profile = findViewById(R.id.btn_done_edit_profile);
        name = findViewById(R.id.name);
        id = findViewById(R.id.ID);
        pw = findViewById(R.id.PW);
        pw_check = findViewById(R.id.PW_check);
        pw_again = findViewById(R.id.pw_again);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent_pic = result.getData();
                        uri = intent_pic != null ? String.valueOf(intent_pic.getData()) : null;
                        getContentResolver().takePersistableUriPermission(Uri.parse(uri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        profile_image.setImageURI(Uri.parse(uri));
                    }
                }
        );

        btn_change_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                launcher.launch(intent);
            }
        });
        //text 내용 초기화(ID, PW, 이름, URI)
        id.setText(MyDiary.현재유저.getID());
        name.setText(MyDiary.현재유저.getName());
        if (MyDiary.현재유저.getUri()!=null) profile_image.setImageURI(Uri.parse(MyDiary.현재유저.getUri()));

        btn_done_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw.getText().toString()!="" && !pw.getText().toString().equals(pw_check.getText().toString())){
                    pw_again.setVisibility(View.VISIBLE);
                }
                else {
                    //text 내용 저장
                    if (!MyDiary.현재유저.getID().equals(id.getText().toString())) MyDiary.현재유저.setID(id.getText().toString());
                    if (!pw.getText().toString().isEmpty()) MyDiary.현재유저.setPW(pw.getText().toString());
                    if (!MyDiary.현재유저.getName().equals(name.getText().toString())) MyDiary.현재유저.setName(name.getText().toString());
                    if (uri!=null) MyDiary.현재유저.setUri(uri);

                    editorU.putString(MyDiary.현재유저.getUnique_num()+"", MyDiary.현재유저.toJson());
                    Log.i("보기", "편집한 user = "+MyDiary.현재유저.toJson());
                    editorU.apply();
                    finish();
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(v.getContext())
                        .setTitle("")
                        .setMessage("로그아웃하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                startActivity(intent_out);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss();
                            }
                        });
                AlertDialog msgDlg = msgBuilder.create();
                msgDlg.show();
            }
        });
        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(v.getContext())
                        .setTitle("한 번 삭제된 정보는 되돌릴 수 없습니다. ")
                        .setMessage("정말 떠나실 건가요?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                startActivity(intent_out);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss();
                            }
                        });
                AlertDialog msgDlg = msgBuilder.create();
                msgDlg.show();
            }
        });
    }
}