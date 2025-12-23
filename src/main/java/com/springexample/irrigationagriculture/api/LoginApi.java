package com.springexample.irrigationagriculture.api;


import com.springexample.irrigationagriculture.dto.LoginRequest;
import com.springexample.irrigationagriculture.dto.UserDto;
import com.springexample.irrigationagriculture.dto.responseDto.UserToken;
import com.springexample.irrigationagriculture.exception.exceptions.AlreadyRegisteredUsernameException;
import com.springexample.irrigationagriculture.exception.exceptions.PasswordLengthException;
import com.springexample.irrigationagriculture.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginApi {

    private final LoginService loginService;

    public LoginApi(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/authUser")
    public ResponseEntity<UserToken> authUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(loginService.userLogin(loginRequest));
    }

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) throws AlreadyRegisteredUsernameException, PasswordLengthException {
        
    return ResponseEntity.ok(loginService.saveUser(userDto));
    }
}
