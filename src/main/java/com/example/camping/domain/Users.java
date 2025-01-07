package com.example.camping.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Data
@Entity
public class Users {
	

    @Id
    @Column(nullable = false)
    private String userId;          // 유저 아이디(primary key)
    
    private String password;        // 유저 비밀번호
    private String name;            // 유저 이름
    private String email;           // 유저 이메일
    private String phone;           // 유저 전화번호
    private String address;         // 유저 주소
	
	// 생일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;          

    // 나이 계산
    public int getAge() {
        if (this.birthday == null) {
            return 0;
        }
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
    
    public enum Gender {
        MALE("M"), FEMALE("F");

        private final String code;

        Gender(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static Gender fromCode(String code) {
            for (Gender gender : Gender.values()) {
                if (gender.code.equalsIgnoreCase(code)) {
                    return gender;
                }
            }
            throw new IllegalArgumentException("Invalid gender code: " + code);
        }
    }
    
    
    // 성별
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; 
    
    // 자동으로 시간을 적용
    @CreationTimestamp              
    @Column(updatable = false)
    private OffsetDateTime regdate;
    
    // 프로필 이미지
    @Column(columnDefinition = "varchar2(255) default 'images/profileimg.png'")
    private String profileImage; 
    
    // Role Enum 추가
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
    
    // 유저 상태
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER; 
}
