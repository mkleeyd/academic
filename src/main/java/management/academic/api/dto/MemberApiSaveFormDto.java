package management.academic.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import management.academic.schoolregister.entity.Address;
import management.academic.schoolregister.entity.Gender;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberApiSaveFormDto {

    @NotEmpty(message = "성명은 필수")
    private String name;
    private String birthMd;
    @NotEmpty(message = "이메일은 필수")
    private String email;
    private String sustCd;
    private String mjrCd;
    private Gender gender;
//    private Address address;

}
