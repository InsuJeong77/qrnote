package com.example.qrnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final String initialUrl = AppHelper.hostUrl + "/login";
    private EditText id;
    private EditText password;

    Handler handler = new Handler();
    SharedPreferences tokenStore;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = findViewById(R.id.id_input);
        password = findViewById(R.id.password_input);
        Button btn = findViewById(R.id.login_btn);
        btn.setOnClickListener(new loginBtnClickListner());

        tokenStore = getSharedPreferences("tokenStore", MODE_PRIVATE);
        editor = tokenStore.edit();
    }

    private void intentTo() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class loginBtnClickListner implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String passwordValue = password.getText().toString();
            String idValue = id.getText().toString();


            Map<String, Object> data = new HashMap<>();
            data.put("loginId", idValue);
            data.put("password", passwordValue);
            JSONObject jsonObject = new JSONObject(data);

            if(AppHelper.requestQueue == null){
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            String url = "http://" + AppHelper.hostUrl + "/login";
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject data = response.getJSONObject("data");
                                if (data.getBoolean("success")) {
                                    editor.putString("token", data.getString("token"));
                                    editor.commit();
                                    intentTo();
                                } else {
                                    Toast.makeText(getApplicationContext(),"잘못된 정보 입력했음. 다시하셈", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                finish();
//                            문제발생 종료
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            finish();
//                            문제발생 종료
                        }
                    }
            ) {};
            request.setShouldCache(false);
            AppHelper.requestQueue.add(request);
        }
    }

}
