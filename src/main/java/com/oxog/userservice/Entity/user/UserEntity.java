package com.oxog.userservice.Entity.user;

import jakarta.persistence.*;
import lombok.Data;
// db 테이블 변환
@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동 생성
    private Long id;
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동 생성
    private String userSeq;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String nickName;
    @Column(nullable = false, length = 50)
    private String address;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String encryptedPwd;
    @Column(nullable = false, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String deleteYn;
    @Lob // 큰 데이터를 저장하기 위한 애너테이션
    private byte[] userIcon; // 유저 아이콘 (이미지 데이터)
}
