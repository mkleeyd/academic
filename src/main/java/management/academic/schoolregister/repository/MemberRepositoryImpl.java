package management.academic.schoolregister.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.MemberSearchCondition;
import management.academic.schoolregister.dto.QMemberFormDto;
import org.springframework.util.StringUtils;

import java.util.List;

import static management.academic.schoolregister.entity.QMember.member;

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
                        member.stuNo,
                        member.shysCd,
                        member.shtmCd,
                        member.finSchregDivCd,
                        member.cptnShtmCnt,
                        member.name,
                        member.birthMd,
                        member.email,
                        member.sustCd,
                        member.mjrCd,
                        member.gender.stringValue(),
                        member.address.city,
                        member.address.street,
                        member.address.zipcode
                ))
                .from(member)
                .where(
                        usernameEq(memberSearchCondition.getUsername()),
                        sustCdEq(memberSearchCondition.getSustCd()),
                        shysCdEq(memberSearchCondition.getShysCd())
                )
                .fetch();
        return result;
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.name.eq(username) : null;
        // member.name.eq(username) : 이 부분이 원래는 Querysql 에서 제공하는 "정적 쿼리" 만드는 부분인데 삼항 연산자로 동적으로 동작하도록 한 것
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
                        member.stuNo,
                        member.shysCd,
                        member.shtmCd,
                        member.finSchregDivCd,
                        member.cptnShtmCnt,
                        member.name,
                        member.birthMd,
                        member.email,
                        member.sustCd,
                        member.mjrCd,
                        member.gender.stringValue(),
                        member.address.city,
                        member.address.street,
                        member.address.zipcode
                ))
                .from(member)
                .where(
                        enterYearEq(memberSearchCondition.getEnterYear()),
                        seqEq(memberSearchCondition.getStuNo())
                )
                .fetchOne();
    }

    private BooleanExpression enterYearEq(String enterYear) {
        return StringUtils.hasText(enterYear)? member.enterYear.eq(enterYear) : null;
    }

    private BooleanExpression seqEq(String stuNo) {
        return StringUtils.hasText(stuNo)? member.stuNo.eq(stuNo) : null;
    }


}




























