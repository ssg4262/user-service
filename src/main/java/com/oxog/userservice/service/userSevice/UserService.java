package com.oxog.userservice.service.userSevice;

import com.oxog.userservice.Entity.UserEntity;
import com.oxog.userservice.messageEnum.ResponseMessage;
import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.model.requestModel.RequestPatchUser;
import com.oxog.userservice.model.requestModel.RequestUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserModel createUser(UserModel userModel);

    UserModel getUserByUserId(String userId);// 아이디로 유저검색

    Iterable<UserEntity> getUserByAll();// 전체 모든유저 데이터

    UserModel getUserByEmail(String email);

    ResponseMessage patchUser(String userId, RequestPatchUser requestPatchUser);
}
