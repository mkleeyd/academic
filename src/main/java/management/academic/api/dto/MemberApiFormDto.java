package management.academic.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import management.academic.schoolregister.entity.Address;
import management.academic.schoolregister.entity.Gender;
import management.academic.schoolregister.entity.Member;

import javax.validation.constraints.NotEmpty;

/**
 * Member API 요청을 모두 받아줄 DTO 객체 생성
 */
@Data
@AllArgsConstructor
public class MemberApiFormDto {

    private Long id;
    private String stuNo;   // 일반 서비스에서 중복없는 username과 같은 역할
    private String name;    // 성명
    private String sustCd;  // 학과코드(법학, 행정학)
    private String mjrCd;   // 전공코드
    private String shysCd;  // 학년코드
    private String shtmCd;  // 학기코드
    private String finSchregDivCd; // 학적상태코드
    private String email; // 이메일
    private Gender gender; // 성별 enum 타입
    private Address address; // 주소 임베디드 타입

    public MemberApiFormDto(Member member) {
        this.id = member.getId();
        this.stuNo = member.getStuNo();
        this.name = member.getName();
        this.sustCd = member.getSustCd();
        this.mjrCd = member.getMjrCd();
        this.shysCd = member.getShysCd();
        this.shtmCd = member.getShtmCd();
        this.finSchregDivCd = member.getFinSchregDivCd();
        this.email = member.getEmail();
        this.gender = member.getGender();
        this.address = member.getAddress();
    }
}
