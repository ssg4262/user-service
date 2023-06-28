package com.oxog.userservice.controller;

import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.model.requestModel.RequestUser;
import com.oxog.userservice.service.userSevice.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    ModelMapper mapper = new ModelMapper();

    @PostMapping("")
    public String createUser(@RequestBody RequestUser user){
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserModel userModel = mapper.map(user , UserModel.class);
        userService.createUser(userModel);
        return "success";
    }
}
