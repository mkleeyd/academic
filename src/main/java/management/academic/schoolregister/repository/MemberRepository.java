package management.academic.schoolregister.repository;

import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    @Query("select m from Member m left join fetch m.shtmScoreResult s")
    List<MemberFormDto> findViewMembers();

}
