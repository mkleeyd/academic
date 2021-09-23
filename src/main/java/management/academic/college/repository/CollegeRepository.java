package management.academic.college.repository;


import management.academic.college.dto.CollegeFormDto;
import management.academic.college.entity.OpenClass;
import management.academic.schoolregister.dto.MemberFormDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollegeRepository extends JpaRepository<OpenClass, Long>, CollegeRepositoryCustom {

}
