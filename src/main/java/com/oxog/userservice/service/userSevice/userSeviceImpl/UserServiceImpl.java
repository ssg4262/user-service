package com.oxog.userservice.service.userSevice.userSeviceImpl;

import com.oxog.userservice.Entity.UserEntity;
import com.oxog.userservice.model.UserModel;
import com.oxog.userservice.model.responseModel.order.ResponseOrder;
import com.oxog.userservice.repository.UserRepository;
import com.oxog.userservice.service.userSevice.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    //MatchingStrategies.STANDARD : 지능적 매핑
    //MatchingStrategies.STRICT : 필드가 맞아떨어질때 매핑
    //MatchingStrategies.LOOSE : 느슨한 매핑
    ModelMapper mapper = new ModelMapper();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email); //이메일로 조회

        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(),userEntity.getEncryptedPwd(),
                true, true , true , true ,
                new ArrayList<>());
    }

    @Override
    public UserModel createUser(UserModel userModel) {
        userModel.setUserId(UUID.randomUUID().toString());// 복호화 후 SET
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 필드가 맞아떨어질때 매핑
        UserEntity userEntity = mapper.map(userModel , UserEntity.class); // 매칭되는 필드만 변환 하는패턴
        // 이메일 중복 체크
        UserEntity chkDup = userRepository.findByEmail(userEntity.getEmail());
        if (chkDup != null) throw new UsernameNotFoundException("Email Duplicate");
        //
        userEntity.setEncryptedPwd(passwordEncoder.encode(userModel.getPwd())); // 암호 복호화
        userRepository.save(userEntity);// 테이블로 변환 후 save

        return mapper.map(userModel, UserModel.class);
    }

    @Override
    public UserModel getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UsernameNotFoundException("User Not Found");

        UserModel userModel = mapper.map(userEntity, UserModel.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userModel.setOrders(orders);

        return userModel;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserModel getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        UserModel userModel = mapper.map(userEntity,UserModel.class);
        List<ResponseOrder> orders = new ArrayList<>();
        userModel.setOrders(orders);
        return userModel;
    }

}
