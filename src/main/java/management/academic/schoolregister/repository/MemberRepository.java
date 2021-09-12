package management.academic.schoolregister.repository;

import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    @Query("select m from Member m where m.enterYear = :enterYear and m.stuNo = :stuNo")
    Member findMember(@Param("enterYear") String enterYear, @Param("stuNo") String stuNo); // 1명 조회(동적 쿼리)

}
