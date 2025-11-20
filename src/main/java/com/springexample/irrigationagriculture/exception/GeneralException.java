package com.springexample.irrigationagriculture.exception;

public class GeneralException extends  Exception{

    public GeneralException(){
        super("An error occurred while processing your request");
    }
}
