package management.academic.dto;

import lombok.*;
import management.academic.entity.Member;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String password;

    // Member 객체로 만들어서 반환
    public Member toEntity(){
        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }

    @Builder
    public MemberDto(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

}
