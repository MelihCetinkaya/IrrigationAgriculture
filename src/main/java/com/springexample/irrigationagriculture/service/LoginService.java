package com.springexample.irrigationagriculture.service;


import com.springexample.irrigationagriculture.dto.LoginRequest;
import com.springexample.irrigationagriculture.dto.UserDto;
import com.springexample.irrigationagriculture.dto.responseDto.UserToken;
import com.springexample.irrigationagriculture.entity.User;
import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.entity.enums.Role;
import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.exception.exceptions.AlreadyRegisteredUsernameException;
import com.springexample.irrigationagriculture.exception.exceptions.PasswordLengthException;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.repository.UserRepo;
import com.springexample.irrigationagriculture.service.otherServices.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UserRepo userRepo;
    private final PersonRepo personRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public LoginService(JwtService jwtService, UserRepo userRepo, PersonRepo personRepo, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.personRepo = personRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserToken userLogin(LoginRequest loginRequest){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        Person person = (Person)personRepo.findByUsername(loginRequest.getUsername()).orElseThrow();
        String token = (String) jwtService.generateToken(person);

        return new UserToken.Builder(token).build();
    }

    @Transactional
    public String saveUser(UserDto userDto) throws AlreadyRegisteredUsernameException, PasswordLengthException {

        userRepo.findByUsername(userDto.getUsername()).ifPresent(u -> {
            throw new AlreadyRegisteredUsernameException();
        });

        if(userDto.getPassword().length()<6 || userDto.getPassword().length()>15 ){
            throw new PasswordLengthException();
        }

        User user1 = new User();
        user1.setName(userDto.getName());
        user1.setUsername(userDto.getUsername());
        user1.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user1.setRole(Role.ROLE_USER);
        user1.setCommand(Boolean.FALSE);
        user1.setTimeZone(TimeZone.Tfree);
        user1.setTimeZone2(TimeZone.Tfree);

        userRepo.save(user1);

        return "user is added";
    }

}
