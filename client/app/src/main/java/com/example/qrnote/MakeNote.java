package com.example.qrnote;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.ViewGroup;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MakeNote extends Fragment {

    private Button btn_Save;

    private EditText editNote_TextArea;
    private EditText editNote_HeadArea;

    //private File root = android.os.Environment.getExternalStorageDirectory();
    //private String path = root.getAbsolutePath() + "/test.txt";
    //private File file = new File(path);
    private String fileName = "MyMemo.txt";

    private long backPressTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_Save = getView().findViewById(R.id.button_save);
        editNote_TextArea = getView().findViewById(R.id.editNote);
        editNote_HeadArea = getView().findViewById(R.id.editNote_Head);

        // fileName = editNote_HeadArea;

        btn_Save.setOnClickListener(btnSaveListener);

        FileInputStream inputStream = null;

        try {
            inputStream = getContext().openFileInput(fileName);

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

            FileOutputStream outputStream = null;

            try {
                outputStream = getContext().openFileOutput(fileName, MODE_PRIVATE);

                outputStream.write(editNote_TextArea.getText().toString().getBytes());
                outputStream.close();

                Toast.makeText(getContext().getApplicationContext(), "Save Complete", Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    };

    public MakeNote() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_makenote, container, false);
    }

    public void onBackPressed()
    {
        if(System.currentTimeMillis() - backPressTime >= 2000)
        {
            backPressTime = System.currentTimeMillis();
            Toast.makeText(getContext().getApplicationContext(), "백(Back) 버튼을 한 번 더 누르면 종료", Toast.LENGTH_LONG).show();
        }
        else if(System.currentTimeMillis() - backPressTime < 2000)
            getActivity().finish();
    }

}