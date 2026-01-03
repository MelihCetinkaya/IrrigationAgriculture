package com.springexample.irrigationagriculture.entity;

public class UserSelections {

    private int[] arr = new int[4];
    private int number;

    public UserSelections(int[] arr, int number) {
        this.arr = arr;
        this.number = number;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
