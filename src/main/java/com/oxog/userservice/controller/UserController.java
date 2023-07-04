package com.oxog.userservice.controller;

import com.oxog.userservice.Entity.UserEntity;
import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.model.requestModel.RequestUser;
import com.oxog.userservice.model.responseModel.user.ResponseUser;
import com.oxog.userservice.service.userSevice.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/user-service")
public class UserController {

    @Autowired
    private UserService userService;

    ModelMapper mapper = new ModelMapper();

    @PostMapping("/users") // 회원가입
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserModel userModel = mapper.map(user , UserModel.class);
        userService.createUser(userModel);
        ResponseUser responseUser = mapper.map(userModel, ResponseUser.class); // userModel을 ResponseUser.class 로 받기
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users") // 전체 유저 조회
    public ResponseEntity<List<ResponseUser>> getUsers(){
        List<ResponseUser> result = new ArrayList<>();
        Iterable<UserEntity> userList = userService.getUserByAll();

        userList.forEach( resultList -> {
            result.add(mapper.map(resultList,ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/id/{userId}") // 아이디 조회
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserModel userModel = userService.getUserByUserId(userId);
        ResponseUser responseUser = mapper.map(userModel,ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<ResponseUser> getUserByEmail(@PathVariable("email") String email){
        UserModel userModel = userService.getUserByEmail(email);
        ResponseUser responseUser = mapper.map(userModel,ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
}
