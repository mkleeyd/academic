package management.academic.schoolregister.repository;

import management.academic.api.dto.MemberApiFormDto;
import management.academic.api.dto.MemberApiSearchCondition;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    @Query("select m from Member m left join fetch m.shtmScoreResult s")
    List<MemberFormDto> findViewMembers();


    //======================================== API 공부 ==========================================//

    @Query(value = "select m from Member m", countQuery = "select count(m) from Member m")
    Page<MemberApiFormDto> findMemberPagingQueryV5(Pageable pageable);

}
