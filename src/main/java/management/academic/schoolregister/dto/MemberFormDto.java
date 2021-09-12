package management.academic.schoolregister.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *  Entity들이 기본 생성자를 PROTECTED 로 하는 이유는 다른곳에서 객체를 생성할 때 경고를 주어 함부로 생성할 수 없도록 막은 것
 *
 *  따라서, DTO 객체는 어디서든 만들 수 있도록 기본 생성자를 public 으로 열어둬야 한다
 */
@Data
@NoArgsConstructor
public class MemberFormDto {

    private String stuNo;

    private String enterYear; // 입학년도(복합키 사용)
    private String shysCd;  // 학년코드
    private String shtmCd;  // 학기코드
    private String finSchregDivCd; // 학적상태코드
    private Integer cptnShtmCnt; // 학기수

    @NotEmpty(message = "성명은 필수입니다")
    private String name;    // 성명
    @NotEmpty(message = "생년월일은 필수입니다")
    private String birthMd; // 생년월일
    @NotEmpty(message = "이메일은 필수입니다")
    private String email; // 이메일
    @NotEmpty(message = "학과는 필수입니다")
    private String sustCd;  // 학과코드(법학, 행정학)
    @NotEmpty(message = "전공은 필수입니다")
    private String mjrCd;   // 전공코드
//    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "성별은 필수입니다")
    private String gender; // 성별
//    private Gender gender;
    @NotEmpty(message = "주소1은 필수입니다")
    private String city; // 주소1
    @NotEmpty(message = "주소2는 필수입니다")
    private String street; // 주소2
    @NotEmpty(message = "주소3은 필수입니다")
    private String zipcode; // 주소3
//    private Address address;

    @QueryProjection
    public MemberFormDto(String stuNo, String shysCd, String shtmCd, String finSchregDivCd, Integer cptnShtmCnt, String name, String birthMd, String email, String sustCd, String mjrCd, String gender, String city, String street, String zipcode) {
        this.stuNo = stuNo;
        this.shysCd = shysCd;
        this.shtmCd = shtmCd;
        this.finSchregDivCd = finSchregDivCd;
        this.cptnShtmCnt = cptnShtmCnt;
        this.name = name;
        this.birthMd = birthMd;
        this.email = email;
        this.sustCd = sustCd;
        this.mjrCd = mjrCd;
        this.gender = gender;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
