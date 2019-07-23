package com.example.tp_android.Models;

public class Message {

    private String sender;
    private String receiver;
    private String message;
    private boolean visto;

    public Message(String sender, String receiver, String message, boolean visto) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.visto = visto;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }
}
