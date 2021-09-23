package management.academic.college.repository;

import management.academic.college.dto.CollegeFormDto;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.entity.Member;

import java.util.List;

public interface CollegeRepositoryCustom {

    List<CollegeFormDto> findAllSubject();  // [수강신청]등록된 모든 강좌 조회

    List<MemberFormDto> findMembers(); // [수강신청]등록된 모든 학생 조회
    
    Member findMember(Long memberId, String stuNo); // [수강신청]등록할 학생 조회

}
