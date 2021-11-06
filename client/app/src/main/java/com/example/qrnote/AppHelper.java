package com.example.qrnote;

import com.android.volley.RequestQueue;

public class AppHelper {
    //서버 url
    //현재는 로컬에서 사용, 포트포워딩 해줘야댐
    public static String hostUrl = "192.168.0.22:8080";

    public static RequestQueue requestQueue;
}
