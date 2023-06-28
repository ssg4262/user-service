package com.oxog.userservice.service.userSevice;

import com.oxog.userservice.Entity.UserEntity;
import com.oxog.userservice.model.UserModel;

public interface UserService {
    UserModel createUser(UserModel userModel);

    UserModel getUserByUserId(String userId);// 아이디로 유저검색

    Iterable<UserEntity> getUserByAll();// 전체 모든유저 데이터
}
