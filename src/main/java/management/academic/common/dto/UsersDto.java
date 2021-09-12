package management.academic.common.dto;

import lombok.*;
import management.academic.common.entity.Users;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class UsersDto {

    private String username;
    private String password;

    // Member 객체로 만들어서 반환
    public Users toEntity(){
        return Users.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Builder
    public UsersDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
