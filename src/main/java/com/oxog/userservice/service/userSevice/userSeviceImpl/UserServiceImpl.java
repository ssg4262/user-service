package com.oxog.userservice.service.userSevice.userSeviceImpl;

import com.oxog.userservice.Entity.user.UserEntity;
import com.oxog.userservice.messageEnum.ResponseMessage;
import com.oxog.userservice.model.responseModel.userDto.UserModel;
import com.oxog.userservice.model.requestModel.user.RequestPatchUser;
import com.oxog.userservice.model.requestModel.user.RequestUser;
import com.oxog.userservice.model.responseModel.order.ResponseOrder;
import com.oxog.userservice.model.responseModel.user.ResponseUser;
import com.oxog.userservice.repository.user.UserRepository;
import com.oxog.userservice.service.userSevice.UserService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public ResponseUser createUser(RequestUser user) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 필드가 맞아떨어질때 매핑
        UserModel userModel = mapper.map(user , UserModel.class);
        userModel.setUserId(UUID.randomUUID().toString());// 복호화 후 SET

        UserEntity userEntity = mapper.map(userModel , UserEntity.class); // 매칭되는 필드만 변환 하는패턴
        // 이메일 중복 체크
        UserEntity chkDup = userRepository.findByEmail(userEntity.getEmail());
        if (chkDup != null) throw new UsernameNotFoundException("Email Duplicate");
        //
        userEntity.setDeleteYn("N");
        userEntity.setEncryptedPwd(passwordEncoder.encode(userModel.getPwd())); // 암호 복호화
        userRepository.save(userEntity);// 테이블로 변환 후 save

        UserModel responseUserModel = mapper.map(userModel, UserModel.class);

        return mapper.map(responseUserModel, ResponseUser.class);
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
    public List<UserEntity> getUserByAll() {
        Optional<List<UserEntity>> usersOptional = userRepository.findAllByDeleteYn();
        return usersOptional.orElse(Collections.emptyList());
    }

    @Override
    public UserModel getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserModel userModel = mapper.map(userEntity,UserModel.class);
        return userModel;
    }

    @Override
    public ResponseMessage patchUser(String userId, RequestPatchUser requestPatchUser) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)throw new NotFoundException("No search User");

        UserModel userModel = mapper.map(userEntity, UserModel.class);

        // 업데이트할 필드를 람다식을 사용하여 설정 ifPresent = 값이 있을경우만 업데이트
        Optional.ofNullable(requestPatchUser.getNickName()).ifPresent(userModel::setNickName);
        Optional.ofNullable(requestPatchUser.getAddress()).ifPresent(userModel::setAddress);

        UserEntity reqPatchEntity = mapper.map(userModel, UserEntity.class);
        userRepository.save(reqPatchEntity);
        return ResponseMessage.SUCCESS;
    }

    @Override
    public ResponseMessage deleteUser(String userId) { // 회원정보 삭제
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new NotFoundException("No search User");
        userEntity.setDeleteYn("Y");
        userRepository.save(userEntity);
        return ResponseMessage.SUCCESS;
    }

}
