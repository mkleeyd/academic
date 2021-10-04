package management.academic.schoolregister.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import management.academic.schoolregister.entity.Address;
import management.academic.schoolregister.entity.Gender;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.ShtmScoreResult;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *  Entity들이 기본 생성자를 PROTECTED 로 하는 이유는 다른곳에서 객체를 생성할 때 경고를 주어 함부로 생성할 수 없도록 막은 것
 *
 *  따라서, DTO 객체는 어디서든 만들 수 있도록 기본 생성자를 public 으로 열어둬야 한다
 *  
 *  <원래 MemberViewFormDto는 validation을 해줘야 하기 때문에 컬럼 하나하나씩 view단에 맞게 써줘야 한다. 각각 만들어줌>
 *      - 밑에 필드처럼 해주면 됨
 *
 *  이런식으로 모든 객체를 DTO에 담아서 뿌려주면 됨
 *
 *  <SearchCondition의 경우 동적 쿼리별로 만들어주면 됨>
 *      
 *  <returnForm의 경우 반환해주는 것을 객체로 한번에 반환해줌>
 *      - 예: private Member member;
 *           private ShtmScore shtmScore;
 *           private ShtmScoreResult shtmScoreResult;
 *
 * <총 필요한 TempCalss>
 *  - MemberViewFormDto(필요할 때마다 생성) : 패키지 Dto 클래스
 *  - MemberSearchCondition() : 동적 쿼리 클래스
 *  - MemberReturnForm(패키지당 1개만 생성) : 반환 클래스
 */
@Data
@NoArgsConstructor
public class MemberFormDto {

    private Long id;

    private String stuNo;   // 학번 (내가 직접 채번 해줌)

    private String enterYear; // 입학년도(복합키 사용)

    @NotEmpty(message = "성명은 필수입니다")
    private String name;    // 성명

    @NotEmpty(message = "생년월일은 필수입니다")
    private String birthMd; // 생년월일

    @NotEmpty(message = "학과는 필수입니다")
    private String sustCd;  // 학과코드(법학, 행정학)

    @NotEmpty(message = "전공은 필수입니다")
    private String mjrCd;   // 전공코드

    private String shysCd;  // 학년코드

    private String shtmCd;  // 학기코드

    private String finSchregDivCd; // 학적상태코드

    private Integer cptnShtmCnt; // 학기수

    @NotEmpty(message = "이메일은 필수입니다")
    private String email; // 이메일

    @NotNull(message = "성별은 필수입니다")
    private Gender gender; // 성별 enum 타입

    private Address address; // 주소 임베디드 타입

//    private ShtmScoreResult shtmScoreResult; // 이렇게 해서 취득학점 가져와도 됨

    private String acqLctpt; // 취득학점(중간 + 기말 더하기)

    private String acqLctptAvgAvrp; // 취득학점 평균평점( 중간+기말/2 )

    @Builder
    public MemberFormDto(Long id, String stuNo, String enterYear, String name, String birthMd, String sustCd, String mjrCd, String shysCd, String shtmCd, String finSchregDivCd, Integer cptnShtmCnt, String email, Gender gender, Address address, String acqLctpt, String acqLctptAvgAvrp) {
        this.id = id;
        this.stuNo = stuNo;
        this.enterYear = enterYear;
        this.name = name;
        this.birthMd = birthMd;
        this.sustCd = sustCd;
        this.mjrCd = mjrCd;
        this.shysCd = shysCd;
        this.shtmCd = shtmCd;
        this.finSchregDivCd = finSchregDivCd;
        this.cptnShtmCnt = cptnShtmCnt;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.acqLctpt = acqLctpt;
        this.acqLctptAvgAvrp = acqLctptAvgAvrp;
    }

    @QueryProjection
    public MemberFormDto(Long id, String stuNo, String enterYear, String name, String birthMd, String sustCd, String mjrCd, String shysCd, String shtmCd, String finSchregDivCd, Integer cptnShtmCnt, String email, Gender gender, Address address) {
        this.id = id;
        this.stuNo = stuNo;
        this.enterYear = enterYear;
        this.name = name;
        this.birthMd = birthMd;
        this.sustCd = sustCd;
        this.mjrCd = mjrCd;
        this.shysCd = shysCd;
        this.shtmCd = shtmCd;
        this.finSchregDivCd = finSchregDivCd;
        this.cptnShtmCnt = cptnShtmCnt;
        this.email = email;
        this.gender = gender;
        this.address = address;
    }



    @QueryProjection
    public MemberFormDto(Long id, String stuNo, String name, String sustCd, String mjrCd, String shysCd, String shtmCd, String acqLctpt, String acqLctptAvgAvrp) {
        this.id = id;
        this.stuNo = stuNo;
        this.name = name;
        this.sustCd = sustCd;
        this.mjrCd = mjrCd;
        this.shysCd = shysCd;
        this.shtmCd = shtmCd;
        //        this.shtmScoreResult = shtmScoreResult;
        this.acqLctpt = acqLctpt;
        this.acqLctptAvgAvrp = acqLctptAvgAvrp;
    }

    /**
     * stream()을 이용한 List<Member> ===> List<MemberFormDto> 로 변환
     */
    public MemberFormDto(Member member){
        this.id = member.getId();
        this.stuNo = member.getStuNo();
        this.name = member.getName();
        this.sustCd = member.getSustCd();
        this.mjrCd = member.getMjrCd();
        this.shysCd = member.getShysCd();
        this.shtmCd = member.getShtmCd();
    }
}
