package com.oxog.userservice.controller;

import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.model.requestModel.RequestUser;
import com.oxog.userservice.model.responseModel.ResponseUser;
import com.oxog.userservice.service.userSevice.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    ModelMapper mapper = new ModelMapper();

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user){
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserModel userModel = mapper.map(user , UserModel.class);
        userService.createUser(userModel);
        ResponseUser responseUser = mapper.map(userModel, ResponseUser.class); // userModel을 ResponseUser.class 로 받기
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
