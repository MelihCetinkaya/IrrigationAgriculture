package com.springexample.irrigationagriculture.entity;

import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PlantHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private List<Person> person= new ArrayList<>();

    @OneToOne
    private Amounts amounts;

    @OneToOne
    @JoinColumn(name = "valve_id")
    private Valve valve;

    public PlantHouse(List <Person> person, Long id, Amounts amounts, Valve valve) {
        this.person = person;
        this.id = id;
        this.amounts = amounts;
        this.valve = valve;
    }

    public PlantHouse() {
    }


    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Amounts getAmounts() {
        return amounts;
    }

    public void setAmounts(Amounts amounts) {
        this.amounts = amounts;
    }

    public void setValues(Amounts amounts) {
        this.amounts = amounts;
    }

    public Valve getValve() {
        return valve;
    }

    public void setValve(Valve valve) {
        this.valve = valve;
    }
}
