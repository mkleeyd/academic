package management.academic.schoolregister.repository;

import management.academic.api.dto.MemberApiFormDto;
import management.academic.api.dto.MemberApiSearchCondition;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.MemberSearchCondition;
import management.academic.schoolregister.dto.MemberSubjectDto;
import management.academic.schoolregister.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * 구현체 바로 가기는 Ctrl + Alt + B
     */
    List<MemberFormDto> findAllMember(MemberSearchCondition memberSearchCondition); // all search(동적 쿼리)

    MemberFormDto findByOneCond(MemberSearchCondition memberSearchCondition); // one search(동적 쿼리)

    List<MemberSubjectDto> findSubjects(MemberSearchCondition memberSearchCondition); // 학생별 강좌 가져오기(동적 쿼리)


    //======================================== API 공부 ==========================================//

    Page<MemberApiFormDto> findAndSearchApiPagingV7(MemberApiSearchCondition condition, Pageable pageable); // 기본

    Page<MemberApiFormDto> findAndSearchApiPagingV8(MemberApiSearchCondition condition, Pageable pageable); // 심화

}
