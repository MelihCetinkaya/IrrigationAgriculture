package com.springexample.irrigationagriculture.entity;

import jakarta.persistence.*;

@Entity
public class Amounts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private PlantHouse plantHouse;

    private double waterValue;
    private double soilValue;
    private double tempValue;
    private double humValue;
    private double wthValue;

    @Column(nullable = false)
    private boolean waterIsActive = true;
    private boolean soilIsActive = true;
    private boolean tempIsActive = true;
    private boolean humIsActive = true;
    private boolean wthIsActive = false;

    private double minTemp = 0.0;
    private double maxTemp = 10.0;
    private double minHum = 0.0;
    private double maxHum = 10.0;
    private double minWth = 0.0;
    private double maxWth = 10.0;
    private double minSoil = 0.0;
    private double maxSoil = 10.0;
    private double minWater = 0.0;
    private double maxWater = 10.0;

    private int minTempTime = 1;
    private int maxTempTime = 3;
    private int minHumTime = 1;
    private int maxHumTime = 3;
    private int minWthTime = 1;
    private int maxWthTime = 3;
    private int minSoilTime = 1;
    private int maxSoilTime = 3;
    private int minWaterTime = 1;
    private int maxWaterTime = 3;
    private int midTempTime = 1;
    private int midHumTime = 3;
    private int midWthTime = 1;
    private int midSoilTime = 3;
    private int midWaterTime = 1;


    public double getHumValue() {
        return humValue;
    }

    public void setHumValue(double humValue) {
        this.humValue = humValue;
    }

    public double getSoilValue() {
        return soilValue;
    }

    public void setSoilValue(double soilValue) {
        this.soilValue = soilValue;
    }

    public double getTempValue() {
        return tempValue;
    }

    public void setTempValue(double tempValue) {
        this.tempValue = tempValue;
    }

    public double getWaterValue() {
        return waterValue;
    }

    public void setWaterValue(double waterValue) {
        this.waterValue = waterValue;
    }

    public double getWthValue() {
        return wthValue;
    }

    public void setWthValue(double wthValue) {
        this.wthValue = wthValue;
    }

    public boolean getHumIsActive() {
        return humIsActive;
    }

    public void setHumIsActive(boolean humIsActive) {
        this.humIsActive = humIsActive;
    }

    public int getId() {
        return id;
    }

    public PlantHouse getPlantHouse() {
        return plantHouse;
    }

    public void setPlantHouse(PlantHouse plantHouse) {
        this.plantHouse = plantHouse;
    }


    public boolean getSoilIsActive() {
        return soilIsActive;
    }

    public void setSoilIsActive(boolean soilIsActive) {
        this.soilIsActive = soilIsActive;
    }

    public boolean getTempIsActive() {
        return tempIsActive;
    }

    public void setTempIsActive(boolean tempIsActive) {
        this.tempIsActive = tempIsActive;
    }

    public boolean getWaterIsActive() {
        return waterIsActive;
    }

    public void setWaterIsActive(boolean waterIsActive) {
        this.waterIsActive = waterIsActive;
    }

    public boolean getWthIsActive() {
        return wthIsActive;
    }

    public void setWthIsActive(boolean wthIsActive) {
        this.wthIsActive = wthIsActive;
    }

    public double getMaxHum() {
        return maxHum;
    }

    public void setMaxHum(double maxHum) {
        this.maxHum = maxHum;
    }

    public double getMaxSoil() {
        return maxSoil;
    }

    public void setMaxSoil(double maxSoil) {
        this.maxSoil = maxSoil;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMaxWater() {
        return maxWater;
    }

    public void setMaxWater(double maxWater) {
        this.maxWater = maxWater;
    }

    public double getMaxWth() {
        return maxWth;
    }

    public void setMaxWth(double maxWth) {
        this.maxWth = maxWth;
    }

    public double getMinHum() {
        return minHum;
    }

    public void setMinHum(double minHum) {
        this.minHum = minHum;
    }

    public double getMinSoil() {
        return minSoil;
    }

    public void setMinSoil(double minSoil) {
        this.minSoil = minSoil;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMinWater() {
        return minWater;
    }

    public void setMinWater(double minWater) {
        this.minWater = minWater;
    }

    public double getMinWth() {
        return minWth;
    }

    public void setMinWth(double minWth) {
        this.minWth = minWth;
    }

    public int getMaxHumTime() {
        return maxHumTime;
    }

    public void setMaxHumTime(int maxHumTime) {
        this.maxHumTime = maxHumTime;
    }

    public int getMaxSoilTime() {
        return maxSoilTime;
    }

    public void setMaxSoilTime(int maxSoilTime) {
        this.maxSoilTime = maxSoilTime;
    }

    public int getMaxTempTime() {
        return maxTempTime;
    }

    public void setMaxTempTime(int maxTempTime) {
        this.maxTempTime = maxTempTime;
    }

    public int getMaxWaterTime() {
        return maxWaterTime;
    }

    public void setMaxWaterTime(int maxWaterTime) {
        this.maxWaterTime = maxWaterTime;
    }

    public int getMaxWthTime() {
        return maxWthTime;
    }

    public void setMaxWthTime(int maxWthTime) {
        this.maxWthTime = maxWthTime;
    }

    public int getMinHumTime() {
        return minHumTime;
    }

    public void setMinHumTime(int minHumTime) {
        this.minHumTime = minHumTime;
    }

    public int getMinSoilTime() {
        return minSoilTime;
    }

    public void setMinSoilTime(int minSoilTime) {
        this.minSoilTime = minSoilTime;
    }

    public int getMinTempTime() {
        return minTempTime;
    }

    public void setMinTempTime(int minTempTime) {
        this.minTempTime = minTempTime;
    }

    public int getMinWaterTime() {
        return minWaterTime;
    }

    public void setMinWaterTime(int minWaterTime) {
        this.minWaterTime = minWaterTime;
    }

    public int getMinWthTime() {
        return minWthTime;
    }

    public void setMinWthTime(int minWthTime) {
        this.minWthTime = minWthTime;
    }

    public int getMidHumTime() {
        return midHumTime;
    }

    public void setMidHumTime(int midHumTime) {
        this.midHumTime = midHumTime;
    }

    public int getMidSoilTime() {
        return midSoilTime;
    }

    public void setMidSoilTime(int midSoilTime) {
        this.midSoilTime = midSoilTime;
    }

    public int getMidTempTime() {
        return midTempTime;
    }

    public void setMidTempTime(int midTempTime) {
        this.midTempTime = midTempTime;
    }

    public int getMidWaterTime() {
        return midWaterTime;
    }

    public void setMidWaterTime(int midWaterTime) {
        this.midWaterTime = midWaterTime;
    }

    public int getMidWthTime() {
        return midWthTime;
    }

    public void setMidWthTime(int midWthTime) {
        this.midWthTime = midWthTime;
    }
}
