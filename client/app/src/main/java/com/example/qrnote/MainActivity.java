package com.example.qrnote;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.qrnote.Note.NoteFragment;



import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    // 4개의 메뉴에 들어갈 Fragment들
    private MainFragment mainfragment = new MainFragment();
    private NoteFragment notefragment = new NoteFragment();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_menu);
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
        fragmentManager.beginTransaction().replace(R.id.container, mainfragment).commitAllowingStateLoss();
        //초기 화면 설정

    }


//    public void generateQRCode(String contents) {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 100, 100));
//            ((ImageView) findViewById(R.id.generated_qrcode)).setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Bitmap toBitmap(BitMatrix matrix) {
//        int height = matrix.getHeight();
//        int width = matrix.getWidth();
//        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
//            }
//        }
//        return bmp;
//    }

}