package com.springexample.irrigationagriculture.dto.aiDto;

import java.util.List;

public class PlantSuitabilityRequest {
    private String plantName;
    private List<Double> temperatureValues;
    private List<Double> humidityValues;
    private List<Double> soilMoisture;
    private List<Double> rain;

    public PlantSuitabilityRequest() {
    }

    public PlantSuitabilityRequest(String plantName, List<Double> temperatureValues,
                                   List<Double> humidityValues, List<Double> soilMoisture,
                                   List<Double> rain) {
        this.plantName = plantName;
        this.temperatureValues = temperatureValues;
        this.humidityValues = humidityValues;
        this.soilMoisture = soilMoisture;
        this.rain = rain;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public List<Double> getTemperatureValues() {
        return temperatureValues;
    }

    public void setTemperatureValues(List<Double> temperatureValues) {
        this.temperatureValues = temperatureValues;
    }

    public List<Double> getHumidityValues() {
        return humidityValues;
    }

    public void setHumidityValues(List<Double> humidityValues) {
        this.humidityValues = humidityValues;
    }

    public List<Double> getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(List<Double> soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public List<Double> getRain() {
        return rain;
    }

    public void setRain(List<Double> rain) {
        this.rain = rain;
    }
}
