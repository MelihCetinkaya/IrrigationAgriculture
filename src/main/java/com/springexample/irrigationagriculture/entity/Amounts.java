package com.springexample.irrigationagriculture.entity;

import jakarta.persistence.*;

@Entity
public class Amounts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private PlantHouse plantHouse;

    private Double temperature;
    private Boolean tempIsActive;
    private Double humidity;
    private Boolean humIsActive;
    private String weather;
    private Boolean wthIsActive;

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Boolean getHumIsActive() {
        return humIsActive;
    }

    public void setHumIsActive(Boolean humIsActive) {
        this.humIsActive = humIsActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlantHouse getPlantHouse() {
        return plantHouse;
    }

    public void setPlantHouse(PlantHouse plantHouse) {
        this.plantHouse = plantHouse;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getTempIsActive() {
        return tempIsActive;
    }

    public void setTempIsActive(Boolean tempIsActive) {
        this.tempIsActive = tempIsActive;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Boolean getWthIsActive() {
        return wthIsActive;
    }

    public void setWthIsActive(Boolean wthIsActive) {
        this.wthIsActive = wthIsActive;
    }
}
