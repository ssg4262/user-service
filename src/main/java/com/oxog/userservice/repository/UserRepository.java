package com.oxog.userservice.repository;

import com.oxog.userservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.deleteYn = 'N'")
    Optional<List<UserEntity>> findAllByDeleteYn();
    @Query("SELECT u FROM UserEntity u WHERE u.userId = :userId AND u.deleteYn = 'N'")
    UserEntity findByUserId(String userId);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.deleteYn = 'N'")
    UserEntity findByEmail(String email);

}
