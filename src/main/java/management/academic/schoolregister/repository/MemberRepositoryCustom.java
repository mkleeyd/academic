package management.academic.schoolregister.repository;

import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.MemberSearchCondition;
import management.academic.schoolregister.dto.MemberSubjectDto;
import management.academic.schoolregister.entity.Member;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * 구현체 바로 가기는 Ctrl + Alt + B
     */
    List<MemberFormDto> findAllMember(MemberSearchCondition memberSearchCondition); // all search(동적 쿼리)

    MemberFormDto findByOneCond(MemberSearchCondition memberSearchCondition); // one search(동적 쿼리)

    List<MemberSubjectDto> findSubjects(MemberSearchCondition memberSearchCondition); // 학생별 강좌 가져오기(동적 쿼리)



}
