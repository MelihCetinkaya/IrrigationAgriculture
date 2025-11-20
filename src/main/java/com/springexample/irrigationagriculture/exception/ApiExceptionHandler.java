package com.springexample.irrigationagriculture.exception;

import com.springexample.irrigationagriculture.exception.exceptions.AlreadyRegisteredUsernameException;
import com.springexample.irrigationagriculture.exception.exceptions.PasswordLengthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public String iaException(){
        return "wrong parameter";
    }

    @ExceptionHandler({AlreadyRegisteredUsernameException.class})
    public String usernameAlreadyRegistered(){
        return  "username already exists";
    }

    @ExceptionHandler({PasswordLengthException.class})
    public String passwordLength(){ return "Your password length must be between 6 and 15 characters";}

}
