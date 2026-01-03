package com.springexample.irrigationagriculture.entity;

import jakarta.persistence.*;

@Entity
public class NotificationTools {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean tempMail = true;
    private boolean tempTelegram = false;
    private boolean tempSocket = true;

    private boolean humMail = false;
    private boolean humTelegram = true;
    private boolean humSocket = true;

    private boolean soilMail = false;
    private boolean soilTelegram = true;
    private boolean soilSocket = true;

    private boolean rainMail = true;
    private boolean rainTelegram = true;
    private boolean rainSocket = false;

    private boolean levelMail = true;
    private boolean levelTelegram = true;
    private boolean levelSocket = true;


    public boolean isHumMail() {
        return humMail;
    }

    public void setHumMail(boolean humMail) {
        this.humMail = humMail;
    }

    public boolean isHumSocket() {
        return humSocket;
    }

    public void setHumSocket(boolean humSocket) {
        this.humSocket = humSocket;
    }

    public boolean isHumTelegram() {
        return humTelegram;
    }

    public void setHumTelegram(boolean humTelegram) {
        this.humTelegram = humTelegram;
    }

    public boolean isLevelMail() {
        return levelMail;
    }

    public void setLevelMail(boolean levelMail) {
        this.levelMail = levelMail;
    }

    public boolean isLevelSocket() {
        return levelSocket;
    }

    public void setLevelSocket(boolean levelSocket) {
        this.levelSocket = levelSocket;
    }

    public boolean isLevelTelegram() {
        return levelTelegram;
    }

    public void setLevelTelegram(boolean levelTelegram) {
        this.levelTelegram = levelTelegram;
    }

    public boolean isRainMail() {
        return rainMail;
    }

    public void setRainMail(boolean rainMail) {
        this.rainMail = rainMail;
    }

    public boolean isRainSocket() {
        return rainSocket;
    }

    public void setRainSocket(boolean rainSocket) {
        this.rainSocket = rainSocket;
    }

    public boolean isRainTelegram() {
        return rainTelegram;
    }

    public void setRainTelegram(boolean rainTelegram) {
        this.rainTelegram = rainTelegram;
    }

    public boolean isSoilMail() {
        return soilMail;
    }

    public void setSoilMail(boolean soilMail) {
        this.soilMail = soilMail;
    }

    public boolean isSoilSocket() {
        return soilSocket;
    }

    public void setSoilSocket(boolean soilSocket) {
        this.soilSocket = soilSocket;
    }

    public boolean isSoilTelegram() {
        return soilTelegram;
    }

    public void setSoilTelegram(boolean soilTelegram) {
        this.soilTelegram = soilTelegram;
    }

    public boolean isTempMail() {
        return tempMail;
    }

    public void setTempMail(boolean tempMail) {
        this.tempMail = tempMail;
    }

    public boolean isTempSocket() {
        return tempSocket;
    }

    public void setTempSocket(boolean tempSocket) {
        this.tempSocket = tempSocket;
    }

    public boolean isTempTelegram() {
        return tempTelegram;
    }

    public void setTempTelegram(boolean tempTelegram) {
        this.tempTelegram = tempTelegram;
    }
}
