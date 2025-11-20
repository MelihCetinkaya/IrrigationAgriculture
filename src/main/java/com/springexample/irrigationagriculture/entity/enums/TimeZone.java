package com.springexample.irrigationagriculture.entity.enums;

public enum TimeZone {

    Tfree(1000),
    T0(0),
    T4(4),
    T8(8),
    T12(12),
    T16(16),
    T20(20);

    private final int value;


    TimeZone(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
