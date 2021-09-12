package management.academic.schoolregister.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import management.academic.schoolregister.entity.Address;
import management.academic.schoolregister.entity.Gender;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MemberSearchCondition {

    @NotEmpty(message = "학번은 필수입니다.")
    private String stuNo;   // 학번
    private String enterYear;   // 학번
    private String username;    // 성명

    private String sustCd;  // 학과코드(법학, 행정학)
    private String shysCd;  // 학년코드

}/////
