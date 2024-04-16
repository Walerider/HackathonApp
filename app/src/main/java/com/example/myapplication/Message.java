package com.example.myapplication;

public class Message {
    private String message;
    private String sender;
    private boolean isBot;

    public Message(String message, String sender, boolean isBot) {
        this.message = message;
        this.sender = sender;
        this.isBot = isBot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", isBot=" + isBot +
                '}';
    }
}
