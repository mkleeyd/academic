package management.academic.schoolregister.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import management.academic.college.entity.Enrolment;
import management.academic.college.entity.OpenClass;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.ShtmScore;

/**
 * 학적 에서 수강 신청 정보 라이프 사이클을 가진 Dto 생성
 */
@Data
@NoArgsConstructor
public class MemberSubjectDto {

    private ShtmScore shtmScore;    // 강좌별 성적 등록 엔티티 PK
    private Member member; // memberId, stuNo 필드만 가져오기
    private Enrolment enrolment;

    private OpenClass openClass;

    @QueryProjection
    public MemberSubjectDto(ShtmScore shtmScore, Member member, Enrolment enrolment, OpenClass openClass) {
        this.shtmScore = shtmScore;
        this.member = member;
        this.enrolment = enrolment;
        this.openClass = openClass;
    }
}
