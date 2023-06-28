package com.oxog.userservice.service.userSevice.userSeviceImpl;

import com.oxog.userservice.Entity.UserEntity;
import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.repository.UserRepository;
import com.oxog.userservice.service.userSevice.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    //MatchingStrategies.STANDARD : 지능적 매핑
    //MatchingStrategies.STRICT : 필드가 맞아떨어질때 매핑
    //MatchingStrategies.LOOSE : 느슨한 매핑
    ModelMapper mapper = new ModelMapper();
    @Override
    public UserModel createUser(UserModel userModel) {
        userModel.setUserId(UUID.randomUUID().toString());// 복호화 후 SET
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 필드가 맞아떨어질때 매핑
        UserEntity userEntity = mapper.map(userModel , UserEntity.class); // 매칭되는 필드만 변환 하는패턴
        userEntity.setEncryptedPwd("encrypted_password");

        userRepository.save(userEntity);// 테이블로 변환 후 save

        return null;
    }
}
