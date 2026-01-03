package com.springexample.irrigationagriculture.dto.aiDto;

public class PlantWateringResponse {

    private String climateCondition;
    private double minValue;
    private double maxValue;

    private int wateringBelowMin;
    private int wateringAtAvg;
    private int wateringAboveMax;


    public PlantWateringResponse(String climateCondition, double minValue, double maxValue, int wateringBelowMin, int wateringAtAvg, int wateringAboveMax) {
        this.climateCondition = climateCondition;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.wateringBelowMin = wateringBelowMin;
        this.wateringAtAvg = wateringAtAvg;
        this.wateringAboveMax = wateringAboveMax;
    }

    public String getClimateCondition() {
        return climateCondition;
    }

    public void setClimateCondition(String climateCondition) {
        this.climateCondition = climateCondition;
    }


    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }


    public int getWateringAboveMax() {
        return wateringAboveMax;
    }

    public void setWateringAboveMax(int wateringAboveMax) {
        this.wateringAboveMax = wateringAboveMax;
    }

    public int getWateringAtAvg() {
        return wateringAtAvg;
    }

    public void setWateringAtAvg(int wateringAtAvg) {
        this.wateringAtAvg = wateringAtAvg;
    }

    public int getWateringBelowMin() {
        return wateringBelowMin;
    }

    public void setWateringBelowMin(int wateringBelowMin) {
        this.wateringBelowMin = wateringBelowMin;
    }
}
