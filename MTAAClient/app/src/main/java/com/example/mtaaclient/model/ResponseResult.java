package com.example.mtaaclient.model;

public class ResponseResult {
    private String body;
    private int code;

    public ResponseResult(String body, int code) {
        this.body = body;
        this.code = code;
    }

    public ResponseResult(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }
}
