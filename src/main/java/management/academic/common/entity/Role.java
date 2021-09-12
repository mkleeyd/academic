package management.academic.common.entity;

import lombok.*;

@Getter
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
//@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 기본 생성자를 생성
//@RequiredArgsConstructor // final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를  생성
public enum Role {
//    ADMIN, MEMBER;
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private String value;

}
