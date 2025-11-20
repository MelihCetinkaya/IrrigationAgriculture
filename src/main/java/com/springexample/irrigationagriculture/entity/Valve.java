package com.springexample.irrigationagriculture.entity;

import jakarta.persistence.*;

@Entity
public class Valve {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Boolean status;

    private int flowTime;

    @OneToOne
    @JoinColumn(name = "plant_id")
    private PlantHouse plantHouse;

    public Valve(int flowTime, int id, PlantHouse plantHouse, Boolean status) {
        this.flowTime = flowTime;
        this.id = id;
        this.plantHouse = plantHouse;
        this.status = status;
    }

    public Valve() {
    }

    public int getFlowTime() {
        return flowTime;
    }

    public void setFlowTime(int flowTime) {
        this.flowTime = flowTime;
    }


    public PlantHouse getPlantHouse() {
        return plantHouse;
    }

    public void setPlantHouse(PlantHouse plantHouse) {
        this.plantHouse = plantHouse;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
