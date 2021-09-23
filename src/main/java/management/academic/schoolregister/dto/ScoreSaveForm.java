package management.academic.schoolregister.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScoreSaveForm {

    private List<Long> shtmScoreNo; // 수강신청 강좌 번호
    private List<Long> memberId;  // 학적 마스터 PK
    private List<String> stuNo;   // 학번
    private List<String> midexamScr; // 중간고사점수
    private List<String> flexmScr; // 기말고사점수



}
