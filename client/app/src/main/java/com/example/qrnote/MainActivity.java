package com.example.qrnote;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import android.widget.ImageView;



import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public String qr_read_data;

    //barcodeLauncher
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "Scanned");
                    Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    qr_read_data = result.getContents();
                }
            });


    //fragment 선언
    Fragment mainfragment;
    Fragment notefragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragment 생성자 만들기
        mainfragment = new MainFragment();
        notefragment = new NoteFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainfragment).commit();
        //초기 화면 설정

        //bottomNavigator
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.main_tab:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, mainfragment).commit();
                                return true;
                            case R.id.note_tab:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, notefragment).commit();
                                return true;

                        }
                        return false;
                    }
                }
        );
//
//        try {
//            Button b = findViewById(R.id.note_create_button);
//            b.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        } catch (NullPointerException e) {
//            System.out.println("noting input");
//        }
    }

    public void ClickMakeNoteButton(View v) {
        setContentView(R.layout.fragment_makenote);
    }
    // click qr button event
    public void ClickQRScanButton(View v){
        scanBarcode(v);
    }

    public void ClickQRGenerateButton(View v){
        System.out.println("asdfsdf");
        generateQRCode("dddd");
    }

    public void scanBarcode(View view) {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setPrompt("QR코드를 스캔하세요");
        barcodeLauncher.launch(options);
    }

    public void generateQRCode(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 100, 100));
            ((ImageView) findViewById(R.id.generated_qrcode)).setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

}