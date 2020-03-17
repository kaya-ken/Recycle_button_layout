package com.websarva.wings.android.recycle_button_layout;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeReader extends AppCompatActivity {

    public static Flag mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlag = new Flag();

        setContentView(R.layout.activity_qrcode_reader);

        IntentIntegrator integrator = new IntentIntegrator(QRCodeReader.this);
        integrator.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);

        //読み取り音を消す
        //integrator.setBeepEnabled(false);

        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            //QRコードからIDを取得し，メニュー画面を表示
            Intent intent =new Intent(QRCodeReader.this,MainActivity.class);
            intent.putExtra("ID",result.getContents());
            startActivity(intent);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (mFlag.getFinishFlg()) {
            finish();
        }
    }
}
