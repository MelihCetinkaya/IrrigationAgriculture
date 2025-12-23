package com.springexample.irrigationagriculture.entity;

import jakarta.persistence.*;

@Entity
public class NotificationTools {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean mailActivity = true;
    private boolean telegramActivity = false;
    private boolean webSocketActivity = true;

    public int getId() {
        return id;
    }

    public boolean getMailActivity() {
        return mailActivity;
    }

    public void setMailActivity(boolean mailActivity) {
        this.mailActivity = mailActivity;
    }

    public boolean getTelegramActivity() {
        return telegramActivity;
    }

    public void setTelegramActivity(boolean telegramActivity) {
        this.telegramActivity = telegramActivity;
    }

    public boolean getWebSocketActivity() {
        return webSocketActivity;
    }

    public void setWebSocketActivity(boolean webSocketActivity) {
        this.webSocketActivity = webSocketActivity;
    }


}
