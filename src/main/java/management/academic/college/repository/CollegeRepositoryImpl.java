package management.academic.college.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import management.academic.college.dto.CollegeFormDto;
import management.academic.college.dto.QCollegeFormDto;
import management.academic.common.entity.college.OpenShtmCd;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.QMemberFormDto;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.QMember;
import org.springframework.util.StringUtils;

import java.util.List;

import static management.academic.college.entity.QOpenClass.openClass;
import static management.academic.schoolregister.entity.QMember.member;

@RequiredArgsConstructor
public class CollegeRepositoryImpl implements CollegeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * <등록된 모든 강좌 조회>
     */
    @Override
    public List<CollegeFormDto> findAllSubject() {
        return jpaQueryFactory
                .select(new QCollegeFormDto(
                        openClass.id,
                        openClass.name,
                        openClass.openYear,
                        openClass.openShtmCd,
                        openClass.lectNo,
                        openClass.openYn,
                        openClass.cptnDivCd,
                        openClass.lctpt,
                        openClass.limitedCnt,
                        openClass.joinedCnt
                ))
                .from(openClass)
                .fetch();

//        List<DTO> content = results.getResults(); // 만약, QueryResults<DTO> 로 반환받았다면 이렇게 List로 data 반환
    }

    /**
     * 수강 신청 선택할 학생들 조회
     */
    @Override
    public List<MemberFormDto> findMembers() {
        return jpaQueryFactory
                .select(new QMemberFormDto(
                        member.id,
                        member.stuNo,
                        member.enterYear,
                        member.name,
                        member.birthMd,
                        member.sustCd,
                        member.mjrCd,
                        member.shysCd,
                        member.shtmCd,
                        member.finSchregDivCd,
                        member.cptnShtmCnt,
                        member.email,
                        member.gender,
                        member.address
                ))
                .from(member)
                .fetch();
    }

    /**
     * 수강 신청 대상 학생 조회
     */
    @Override
    public Member findMember(Long memberId, String stuNo) {
        return jpaQueryFactory
                .select(member)
                .from(member)
                .where(
                        memberIdEq(memberId),
                        stuNoEq(stuNo)
                )
                .fetchOne();
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? member.id.eq(id) : null;
    }

    private BooleanExpression stuNoEq(String stuNo) {
        return StringUtils.hasText(stuNo) ? member.stuNo.eq(stuNo) : null;
    }
}/////
