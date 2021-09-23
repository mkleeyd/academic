package management.academic.schoolregister.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResultForm {

    private List<Long> shtmScoreNo; // 강좌별 성적 등록 PK
    private List<Long> memberId;
    private List<String> stuNo;
    private List<String> midexamScr;
    private List<String> flexmScr;


}
