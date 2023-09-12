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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    ModelMapper mapper = new ModelMapper();

    private String generateUniqueUserSeq() {
        Set<String> generatedUserIds = new HashSet<>();
        return Stream.generate(this::generateRandomUserId)
                .filter(userId -> {
                    boolean isUnique = !generatedUserIds.contains(userId);
                    if (isUnique) {
                        generatedUserIds.add(userId);
                    }
                    return isUnique;
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unique userSeq cannot be generated"));
    }
    private String generateRandomUserId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    private void fieldsIfPresent(UserModel userModel, RequestPatchUser requestPatchUser) {
        Optional.ofNullable(requestPatchUser.getNickName()).ifPresent(userModel::setNickName);
        Optional.ofNullable(requestPatchUser.getAddress()).ifPresent(userModel::setAddress);
    }

    private void setProfileImageIfPresent(RequestUser user, UserModel userModel) {
        Optional.ofNullable(user.getReqUserIcon())
                .ifPresent(userIcon -> {
                    try {
                        userModel.setUserIcon(userIcon.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Error while converting userIcon to bytes");
                    }
                });
    }

    private void checkEmailDuplication(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new UsernameNotFoundException("Email Duplicate");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public ResponseUser createUser(RequestUser user) {
        UserModel userModel = mapRequestUserToUserModel(user);
        UserEntity userEntity = mapUserModelToUserEntity(userModel);
        checkEmailDuplication(userEntity.getEmail());
        saveUserEntity(userEntity);
        return mapUserModelToResponseUser(userModel);
    }

    @Override
    public ResponseMessage patchUser(String userId, RequestPatchUser requestPatchUser) {
        UserEntity userEntity = findUserByUserId(userId);
        UserModel userModel = mapper.map(userEntity, UserModel.class);
        fieldsIfPresent(userModel, requestPatchUser);
        UserEntity reqPatchEntity = mapUserModelToUserEntity(userModel);
        saveUserEntity(reqPatchEntity);
        return ResponseMessage.SUCCESS;
    }

    @Override
    public ResponseMessage deleteUser(String userId) {
        UserEntity userEntity = findUserByUserId(userId);
        userEntity.setDeleteYn("Y");
        saveUserEntity(userEntity);
        return ResponseMessage.SUCCESS;
    }

    @Override
    public List<UserEntity> getUserByAll() {
        Optional<List<UserEntity>> usersOptional = userRepository.findAllByDeleteYn();
        return usersOptional.orElse(Collections.emptyList());
    }

    @Override
    public UserModel getUserByUserId(String userId) {
        UserEntity userEntity = findUserByUserId(userId);
        UserModel userModel = mapper.map(userEntity, UserModel.class);
        userModel.setOrders(new ArrayList<>());
        return userModel;
    }

    @Override
    public UserModel getUserByEmail(String email) {
        UserEntity userEntity = findUserByEmail(email);
        return mapper.map(userEntity, UserModel.class);
    }

    private void saveUserEntity(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    private UserEntity findUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new NotFoundException("No search User");
        }
        return userEntity;
    }

    private UserEntity findUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return userEntity;
    }


    private UserModel mapRequestUserToUserModel(RequestUser user) {
        UserModel userModel = mapper.map(user, UserModel.class);
        userModel.setUserSeq(generateUniqueUserSeq());
        setProfileImageIfPresent(user, userModel);
        userModel.setUserId(UUID.randomUUID().toString());
        return userModel;
    }

    private UserEntity mapUserModelToUserEntity(UserModel userModel) {
        UserEntity userEntity = mapper.map(userModel, UserEntity.class);
        userEntity.setDeleteYn("N");
        userEntity.setEncryptedPwd(passwordEncoder.encode(userModel.getPwd()));
        return userEntity;
    }

    private ResponseUser mapUserModelToResponseUser(UserModel userModel) {
        UserModel responseUserModel = mapper.map(userModel, UserModel.class);
        return mapper.map(responseUserModel, ResponseUser.class);
    }

}
