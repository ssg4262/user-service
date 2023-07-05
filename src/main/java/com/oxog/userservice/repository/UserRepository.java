package com.oxog.userservice.repository;

import com.oxog.userservice.Entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String email);
}
