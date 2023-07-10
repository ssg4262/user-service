package com.oxog.userservice.repository;

import com.oxog.userservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.deleteYn = 'N'")
    Optional<List<UserEntity>> findAllByDeleteYn();
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String email);
}
