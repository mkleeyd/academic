package management.academic.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberApiUpdateFormDto {

    @NotEmpty(message = "성명은 필수로 바꾸는 테스트임")
    private String name;
    private String email;
    private String sustCd;

}
