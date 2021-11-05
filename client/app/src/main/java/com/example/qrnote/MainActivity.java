package com.example.qrnote;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //fragment 선언
    Fragment mainfragment;
    Fragment notefragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragment 생성자 만들기 정인수 바보
        mainfragment = new MainFragment();
        notefragment = new NoteFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainfragment).commit();
        //초기 화면 설정

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
    }
}