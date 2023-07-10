package com.oxog.userservice.service.userSevice;

import com.oxog.userservice.Entity.user.UserEntity;
import com.oxog.userservice.messageEnum.ResponseMessage;
import com.oxog.userservice.model.responseModel.userDto.UserModel;
import com.oxog.userservice.model.requestModel.user.RequestPatchUser;
import com.oxog.userservice.model.requestModel.user.RequestUser;
import com.oxog.userservice.model.responseModel.user.ResponseUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    ResponseUser createUser(RequestUser user);// 회원가입

    UserModel getUserByUserId(String userId);// 아이디로 유저검색

    List<UserEntity> getUserByAll();// 전체 모든유저 데이터

    UserModel getUserByEmail(String email); // 이메일로 찾기

    ResponseMessage patchUser(String userId, RequestPatchUser requestPatchUser); // 유저정보 수정

    ResponseMessage deleteUser(String userId);
}
