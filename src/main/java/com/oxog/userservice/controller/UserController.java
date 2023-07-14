package com.oxog.userservice.controller;

import com.oxog.userservice.Entity.user.UserEntity;
import com.oxog.userservice.messageEnum.ResponseMessage;
import com.oxog.userservice.model.responseModel.userDto.UserModel;
import com.oxog.userservice.model.requestModel.user.RequestPatchUser;
import com.oxog.userservice.model.requestModel.user.RequestUser;
import com.oxog.userservice.model.responseModel.user.ResponseUser;
import com.oxog.userservice.service.userSevice.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;
    ModelMapper mapper = new ModelMapper();

    @GetMapping("/status")
    public String status(){
        return String.format("local server.port="+env.getProperty("local.server.port")
                + "token secret ="+env.getProperty("token.secret")
        );
    }

    @PostMapping("/users") // 회원가입
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){

        ResponseUser responseUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users") // 전체 유저 조회
    public ResponseEntity<List<ResponseUser>> getUsers(){
        List<ResponseUser> result = new ArrayList<>();
        List<UserEntity> userList = userService.getUserByAll();

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

    @GetMapping("/users/email/{email}") // 이메일 조회
    public ResponseEntity<ResponseUser> getUserByEmail(@PathVariable("email") String email){
        UserModel userModel = userService.getUserByEmail(email);
        ResponseUser responseUser = mapper.map(userModel,ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PatchMapping("/users/{userId}") // 회원정보 수정
    public ResponseEntity<ResponseMessage> patchUser(@PathVariable("userId") String userId , @RequestBody RequestPatchUser requestPatchUser){
        ResponseMessage message = userService.patchUser(userId,requestPatchUser);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/users/delete/{userId}") // 회원정보 삭제
    public ResponseEntity<ResponseMessage> deletedUser(@PathVariable("userId") String userId){
        ResponseMessage message = userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
