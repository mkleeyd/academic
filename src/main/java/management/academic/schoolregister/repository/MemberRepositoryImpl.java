package management.academic.schoolregister.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import management.academic.api.dto.MemberApiFormDto;
import management.academic.api.dto.MemberApiSearchCondition;
import management.academic.college.entity.Enrolment;
import management.academic.schoolregister.dto.*;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.QShtmScore;
import management.academic.schoolregister.entity.ShtmScore;
import net.bytebuddy.description.annotation.AnnotationList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static management.academic.college.entity.QEnrolment.enrolment;
import static management.academic.college.entity.QOpenClass.openClass;
import static management.academic.schoolregister.entity.QMember.member;
import static management.academic.schoolregister.entity.QShtmScore.shtmScore;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * <학생 모두 조회-동적 쿼리>
     * @param memberSearchCondition
     * @return
     */
    @Override
    public List<MemberFormDto> findAllMember(MemberSearchCondition memberSearchCondition) {
        List<MemberFormDto> result = queryFactory
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
                .where(
                        memberIdEq(memberSearchCondition.getId()),
                        stuNoEq(memberSearchCondition.getStuNo()),
                        shysCdEq(memberSearchCondition.getShysCd()),
                        sustCdEq(memberSearchCondition.getSustCd())
                )
                .fetch();
        return result;
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? member.id.eq(id) : null;
    }

    private BooleanExpression stuNoEq(String stuNo) {
        return StringUtils.hasText(stuNo) ? member.stuNo.eq(stuNo) : null;
    }

    private BooleanExpression sustCdEq(String sustCd) {
        return StringUtils.hasText(sustCd) ? member.sustCd.eq(sustCd) : null;
        // member.sustCd.eq(sustCd) : 이 부분이 원래는 Querysql 에서 제공하는 "정적 쿼리" 만드는 부분인데 삼항 연산자로 동적으로 동작하도록 한 것
    }

    private BooleanExpression shysCdEq(String shysCd) {
        return StringUtils.hasText(shysCd) ? member.shysCd.eq(shysCd) : null;
        // member.shysCd.eq(shysCd) : 이 부분이 원래는 Querysql 에서 제공하는 "정적 쿼리" 만드는 부분인데 삼항 연산자로 동적으로 동작하도록 한 것
    }

    /**
     * <search 학생 1명 조회-동적 쿼리>
     * @param memberSearchCondition
     * @return
     */
    @Override
    public MemberFormDto findByOneCond(MemberSearchCondition memberSearchCondition) {
        return queryFactory
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
                .where(
                        memberIdEq(memberSearchCondition.getId()),
                        stuNoEq(memberSearchCondition.getStuNo())
                )
                .fetchOne();
    }

    /**
     * 학생별 수강 신청 강좌 가져오기
     * 이렇게 selcet 안에 직접 넣어주면 DTO로 변환할 필요 없이 바로 가져오기 가능
     *
     * 그냥 enrolment로 가져오고 lect join 해서 가져오면 DTO로 변환 해줘야 하고 N+1 이슈도 발생함
     */
    @Override
    public List<MemberSubjectDto> findSubjects(MemberSearchCondition memberSearchCondition) {

        // 지연로딩 엔티티를 배치 사이즈로 2번에 걸쳐 처리하는 방법#1
        // 지연로딩 엔티티를 fetchJoin으로 한번에 처리하는 방법#2
        List<ShtmScore> shtmScores = queryFactory
                .select(shtmScore)
                .from(shtmScore)
                .leftJoin(shtmScore.member, member)
                .leftJoin(shtmScore.enrolment, enrolment)
                .leftJoin(shtmScore.enrolment.openClass, openClass)
//                .leftJoin(shtmScore.member, member).fetchJoin() // 각 join 마다 fetchJoin 붙여줘야 해당 엔티티를 즉시로딩으로 가져옴
//                .leftJoin(shtmScore.enrolment, enrolment).fetchJoin() // 각 join 마다 fetchJoin 붙여줘야 해당 엔티티를 즉시로딩으로 가져옴
                .where(
                        memberIdEq(memberSearchCondition.getId())
                )
                .fetch();
        return shtmScores.stream() // List<Enrolment> 를 List<MemberSubjectDto> 로 변환 후 반환
                .map(s -> new MemberSubjectDto(s, s.getMember(), s.getEnrolment(), s.getEnrolment().getOpenClass()))
                .collect(Collectors.toList());

// QMemberSubjectDto 를 이용해서 바로 변환된 객체를 반환받는게 더 좋을수도 있음
// 위에서 map() 을통해 변환 할 때 보면 결국은 임의의 생성자 똑같이 생성해서 사용하는 것을 보면 메모리는 똑같이 들고 오히려 변환 하는 리소스가 더 들듯

//        return queryFactory
//                .select(new QMemberSubjectDto(
//                        enrolment.id,
//                        enrolment.member.stuNo,     // 여기 부분을 enrolment.member 이렇게만 가져오면 Member 컬럼 다 들고옴
//                        enrolment.openClass.name,   // 여기 부분을 enrolment.openClass 이렇게만 가져오면 OpenClass 컬럼 다 들고옴
//                        enrolment.openYear,
//                        enrolment.openShtmCd,
//                        enrolment.lectNo,
//                        enrolment.cptnDivCd,
//                        enrolment.lctpt
//                ))
//                .from(enrolment)
//                .leftJoin(enrolment.member, member) // 이거 필요한가? 위에서 다 가져올텐데??
//                .leftJoin(enrolment.openClass, openClass) // 이거 필요한가? 위에서 다 가져올텐데??
//                .where(
//                        memberIdEq(memberSearchCondition.getId()),
//                        stuNoEq(memberSearchCondition.getStuNo())
//                )
//                .fetch();
    }

    //======================================== API 공부 ==========================================//

    // 근데 이렇게 단순한 동적 쿼리라면 원래는 @Query()에 작성하고 끝내도 된다
    @Override
    public Page<MemberApiFormDto> findAndSearchApiPagingV7(MemberApiSearchCondition condition, Pageable pageable) {
        QueryResults<Member> result = queryFactory
                .select(member)
                .from(member)
                .where(
                        memberIdEq(condition.getId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();// 컨텐츠 쿼리, 카운트 쿼리 각각 날림

        List<MemberApiFormDto> content = result.getResults().stream() // 데이터 부분 반환
                .map(member1 -> new MemberApiFormDto(member1)).collect(Collectors.toList());
        long total = result.getTotal(); // total 카운트 반환

        return new PageImpl<>(content, pageable, total); // 순서 중요
    }

    @Override
    public Page<MemberApiFormDto> findAndSearchApiPagingV8(MemberApiSearchCondition condition, Pageable pageable) {
        // 컨텐츠 쿼리, 카운트 쿼리 각각 날림 ( 카운트 쿼리를 분리하여 무거운 쿼리가 2번 나가지 않고 total 쿼리는 가볍게 나가게 하기 위해 )
        List<Member> fetchContent = queryFactory
                .select(member)
                .from(member)
                .where(
                        memberIdEq(condition.getId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<MemberApiFormDto> content = fetchContent.stream().map(m -> new MemberApiFormDto(m)).collect(Collectors.toList());

        JPAQuery<Member> countQuery = queryFactory
                .select(member)
                .from(member)
                .where(
                        memberIdEq(condition.getId())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }
}/////




























