package management.academic.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;

    @Builder
    public Member(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

//    등록일시, 등록자, 수정일시, 수정자 따로 AOP 처럼 만들기
//    private LocalDateTime 등록날짜;
//    private LocalDateTime 수정날짜;
//    private String 등록자;
//    private String 수정자;

//    @Id
//    @GeneratedValue
//    @Column(name = "seq")
//    private Long id;
//    private String stuNo;   // 학번
//    private String passWord; // 비밀번호
//    private String name;    // 성명
//    private String birthMd; // 생년월일
//    private String sustCd;  // 학과코드(법학, 행정학)
//    private String mjrCd;   // 전공코드
//    private String shysCd;  // 학년코드
//    private String shtmCd;  // 학기코드
//    private String finSchregDivCd; // 학적상태코드
//    private int cptnShtmCnt; // 학기수
//    private String email; // 이메일
//    @Enumerated(EnumType.STRING)
//    @Column(name = "gender")
//    private Gender gender; // 성별 enum 타입
//    @Embedded
//    private Address address; // 주소 임베디드 타입






}/////
