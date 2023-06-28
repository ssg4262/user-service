package com.oxog.userservice.service.userSevice.userSeviceImpl;

import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.service.userSevice.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserModel createUser(UserModel userModel) {
        userModel.setUserId(UUID.randomUUID().toString());// 복호화 후 SET
        
        return null;
    }
}
