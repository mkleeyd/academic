package management.academic.schoolregister.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberSearchCondition {

    private Long id;
    @NotEmpty(message = "학번은 필수입니다.")
    private String stuNo;   // 학번
    private String username;    // 성명

    private String sustCd;  // 학과코드(법학, 행정학)
    private String shysCd;  // 학년코드

}/////
