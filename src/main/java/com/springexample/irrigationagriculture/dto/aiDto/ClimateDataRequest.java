package com.springexample.irrigationagriculture.dto.aiDto;

import java.util.Deque;
import java.util.List;

public class ClimateDataRequest {
    private Deque<Double> temperatureValues;
    private Deque<Double> humidityValues;
    private Deque<Double> soilMoisture;
    private Deque<Double> rain;

    public ClimateDataRequest(Deque<Double> humidityValues, Deque<Double> rain, Deque<Double> soilMoisture, Deque<Double> temperatureValues) {
        this.humidityValues = humidityValues;
        this.rain = rain;
        this.soilMoisture = soilMoisture;
        this.temperatureValues = temperatureValues;
    }

    public Deque<Double> getHumidityValues() {
        return humidityValues;
    }

    public void setHumidityValues(Deque<Double> humidityValues) {
        this.humidityValues = humidityValues;
    }

    public Deque<Double> getRain() {
        return rain;
    }

    public void setRain(Deque<Double> rain) {
        this.rain = rain;
    }

    public Deque<Double> getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Deque<Double> soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public Deque<Double> getTemperatureValues() {
        return temperatureValues;
    }

    public void setTemperatureValues(Deque<Double> temperatureValues) {
        this.temperatureValues = temperatureValues;
    }
}
