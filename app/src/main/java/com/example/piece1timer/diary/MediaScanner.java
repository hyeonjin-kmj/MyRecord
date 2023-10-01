package com.example.piece1timer.diary;

import android.content.Context;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class MediaScanner {
    /*
    MediaScannerConnection (스캔 = 콘텐츠 프로바이더에 직접 반영(원래 재부팅 시 자동반영))
    앱이 미디어 스캐너 서비스로 새로 생성된 미디어 파일을 전달할 수 있게 해줌.
    미디어 스캐너 서비스 : 전달받은 미디어 파일의 메타 데이터를 읽어 "미디어 콘텐츠 제공자"에 파일을 추가함.
    uri

    => 결국 레이아웃을 이미지로 저장한 후, 바로 확인하려면
    1. 레이아웃 -> 이미지로 저장 = canvas, bitmap
    2. 이미지 -> media scanner로 바로 갤러리에 반영. = media Scanner
    */
    private Context context;

    private String path;

    private MediaScannerConnection mMediaScanner;
    private MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;

    public static MediaScanner newInstance(Context context) {
        return new MediaScanner(context);
    }

    public MediaScanner(Context context) {
        this.context = context;
    }

    public void mediaScanning(String path) {

        if (mMediaScanner == null) {
            mMediaScannerClient = new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    mMediaScanner.scanFile(path, null);
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    mMediaScanner.disconnect();
                }
            };
            mMediaScanner = new MediaScannerConnection(context, mMediaScannerClient);
        }

        this.path = path;
        mMediaScanner.connect();
    }
}
