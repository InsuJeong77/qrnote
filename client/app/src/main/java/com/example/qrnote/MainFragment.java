package com.example.qrnote;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qrnote.Note.Memo;
import com.example.qrnote.Note.ReadNoteActivity;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    SharedPreferences tokenStore;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button qrBtn = (Button) view.findViewById(R.id.qr_scan_button);
        qrBtn.setOnClickListener(qrBtnClickListener);

        tokenStore = getActivity().getSharedPreferences("tokenStore", MODE_PRIVATE);
        token = tokenStore.getString("token", null);

        return view;
    }
    View.OnClickListener qrBtnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            scanBarcode(v);
        }

    };


    public void scanBarcode(View view) {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setPrompt("QR 코드를 스캔하세요");
        barcodeLauncher.launch(options);
    }

    //barcodeLauncher
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() != null) {
                    noteReadByQR(result.getContents());
//                    qr_read_data = result.getContents();
//                    TextView qr_textview = (TextView) findViewById(R.id.qr_textView);
//                    qr_textview.setText(qr_read_data);
                }
            });

    private void noteReadByQR(String qrcode) {
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        String url = "http://" + AppHelper.hostUrl + "/memo/read/qr?qrcode=" + qrcode;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            Memo memo = new Memo(data.toString(), qrcode);
                            readMemo(memo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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

    private void readMemo(Memo qrMemo) {
        Intent intent = new Intent(getActivity(), ReadNoteActivity.class);
        intent.putExtra("qrMemo", qrMemo);
        startActivity(intent);
    }

}