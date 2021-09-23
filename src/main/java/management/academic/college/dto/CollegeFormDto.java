package management.academic.college.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CollegeFormDto {

    private Long id;

    @NotEmpty(message = "강좌명은 필수입니다.")
    private String name; // 강좌명
    
    @NotEmpty(message = "개설년도는 필수입니다.")
    private String openYear; // 개설년도

    @NotEmpty(message = "개설학기는 필수입니다.")
    private String openShtmCd; // 개설 학기 코드

    private String lectNo; // 강좌번호

    private String openYn; // 개설여부{enum 타입}

    @NotEmpty(message = "이수구분은 필수입니다.")
    private String cptnDivCd; // 이수구분 코드

    @NotNull(message = "학점은 필수입니다.")
    private String lctpt; // 학점

    @NotNull(message = "수강가능 인원은 필수입니다.")
    private Integer limitedCnt; // 수강가능 인원 수

    private Integer joinedCnt; // 수강인원 수

    @QueryProjection
    public CollegeFormDto(Long id, String name, String openYear, String openShtmCd, String lectNo, String openYn, String cptnDivCd, String lctpt, Integer limitedCnt, Integer joinedCnt) {
        this.id = id;
        this.name = name;
        this.openYear = openYear;
        this.openShtmCd = openShtmCd;
        this.lectNo = lectNo;
        this.openYn = openYn;
        this.cptnDivCd = cptnDivCd;
        this.lctpt = lctpt;
        this.limitedCnt = limitedCnt;
        this.joinedCnt = joinedCnt;
    }
}/////
