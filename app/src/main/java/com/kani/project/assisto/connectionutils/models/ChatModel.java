package com.kani.project.assisto.connectionutils.models;

/**
 * Created by root on 1/10/16.
 */
public class ChatModel {


    boolean isResponse;
    String message;
    int i;

    public boolean isResponse() {
        return isResponse;
    }

    public void setResponse(boolean response) {
        isResponse = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
