package com.example.qrnote;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MakeNote extends AppCompatActivity {

    SharedPreferences tokenStore;

    private Button btn_Save;

    private EditText editNote_TextArea;
    private EditText editNote_HeadArea;

    private Long teamId = 0L;

    //private File root = android.os.Environment.getExternalStorageDirectory();
    //private String path = root.getAbsolutePath() + "/test.txt";
    //private File file = new File(path);
    private String fileName = "MyMemo.txt";

    private long backPressTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_makenote);

        Intent intent = getIntent();
        teamId = intent.getLongExtra("teamId", 0);

        tokenStore = getSharedPreferences("tokenStore", MODE_PRIVATE);

        btn_Save = findViewById(R.id.button_save);
        editNote_TextArea = findViewById(R.id.editNote);
        editNote_HeadArea = findViewById(R.id.editNote_Head);

        // fileName = editNote_HeadArea;

        btn_Save.setOnClickListener(btnSaveListener);

        FileInputStream inputStream = null;

        try {
            inputStream = openFileInput(fileName);

            byte[] data = new byte[inputStream.available()];
            while(inputStream.read(data) != -1) {}

            editNote_TextArea.setText(new String(data));
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) inputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener btnSaveListener = new View.OnClickListener() {
        public void onClick(View v) {
            Map<String, Object> memo = new HashMap<>();
            memo.put("title", editNote_HeadArea.getText().toString());
            memo.put("contents", editNote_TextArea.getText().toString());

            Map<String, Object> data = new HashMap<>();
            data.put("memo", memo);
            data.put("teamId", teamId);
            JSONObject jsonObject = new JSONObject(data);

            memoSave(jsonObject);

            //일단 string 값을 넣는 것으로 구현, 파일은 시간 되면
//            FileOutputStream outputStream = null;
//
//            try {
//                outputStream = openFileOutput(fileName, MODE_PRIVATE);
//
//                outputStream.write(editNote_TextArea.getText().toString().getBytes());
//                outputStream.close();
//
//
//
//                Toast.makeText(getApplicationContext(), "Save Complete", Toast.LENGTH_LONG).show();
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
        }

    };

    private void memoSave(JSONObject jsonObject) {
        String token = tokenStore.getString("token", null);
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "http://" + AppHelper.hostUrl + "/memo/create";
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
                                Toast.makeText(getApplicationContext(), data.getString("qrcode"), Toast.LENGTH_SHORT).show();
                                finish();
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
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("token", token);
                return header;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        AppHelper.requestQueue.add(request);
    }

    public MakeNote() {
        // Required empty public constructor
    }

    public void onBackPressed()
    {
        if(System.currentTimeMillis() - backPressTime >= 2000)
        {
            backPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "백(Back) 버튼을 한 번 더 누르면 종료", Toast.LENGTH_LONG).show();
        }
        else if(System.currentTimeMillis() - backPressTime < 2000)
            finish();
    }

}