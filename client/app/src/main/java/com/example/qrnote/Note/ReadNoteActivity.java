package com.example.qrnote.Note;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.qrnote.AppHelper;
import com.example.qrnote.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReadNoteActivity extends AppCompatActivity {

    SharedPreferences tokenStore;

    private Button btn_Save;
    private Button btn_Qr;

    private ImageView imageView_QrImage;

    private EditText editNote_TextArea;
    private EditText editNote_HeadArea;

    private Long teamId = 0L;

    private long backPressTime = 0;

    private boolean qrCode_is_visible=false;

    JSONObject currMemo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_makenote);

        Intent intent = getIntent();
        Memo qrMemo = (Memo) intent.getSerializableExtra("qrMemo");
        String qrcode = qrMemo.getQRcode();
        try {
            currMemo = new JSONObject(qrMemo.getCurrMemo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tokenStore = getSharedPreferences("tokenStore", MODE_PRIVATE);

        btn_Save = findViewById(R.id.button_save);
        btn_Qr = findViewById(R.id.button_qr_show);
        editNote_TextArea = findViewById(R.id.editNote);
        editNote_HeadArea = findViewById(R.id.editNote_Head);
        imageView_QrImage = findViewById(R.id.qr_imageView);

        try {
            editNote_HeadArea.setText(currMemo.getString("title"));
            editNote_TextArea.setText(currMemo.getString("contents"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //imageView_QrImage.setImageBitmap(generateQRCode(qrcode));
        generateQRCode(qrcode);

        btn_Save.setOnClickListener(btnSaveListener);
        btn_Qr.setOnClickListener(btnSaveListener);
    }

    View.OnClickListener btnSaveListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.button_qr_show){
                ImageView qr_image = findViewById(R.id.qr_imageView);
                if (qrCode_is_visible != true) {
                    qr_image.setVisibility(View.VISIBLE);
                    qrCode_is_visible = true;
                }
                else {
                    qr_image.setVisibility(View.INVISIBLE);
                    qrCode_is_visible = false;
                }
                //Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
            }
            else if(v.getId() == R.id.button_save) {
                try {
                    currMemo.put("title", editNote_HeadArea.getText().toString());
                    currMemo.put("contents", editNote_TextArea.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                memoSave(currMemo);
            }
        }

    };

    private void memoSave(JSONObject jsonObject) {
        String token = tokenStore.getString("token", null);
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "http://" + AppHelper.hostUrl + "/memo/modify";
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
                                Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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

    public void generateQRCode(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 100, 100));
            ((ImageView) findViewById(R.id.qr_imageView)).setImageBitmap(bitmap);
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