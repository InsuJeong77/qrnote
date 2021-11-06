package com.example.qrnote.Note;

import org.json.JSONObject;

import java.io.Serializable;

public class Memo implements Serializable {
    private Long id;
    private String title;
    private String currMemo;
    private String qrcode;

    public Memo(Long id, String title, String currMemo, String qrcode) {
        this.id = id;
        this.title = title;
        this.currMemo = currMemo;
        this.qrcode = qrcode;
    }

    public Memo(String currMemo, String qrcode) {
        this.currMemo = currMemo;
        this.qrcode = qrcode;
    }
    public String getTitle() {
        return title;
    }
    public Long getId() {
        return id;
    }
    public String getCurrMemo() { return currMemo; }
    public String getQRcode() {
        return qrcode;
    }
}

