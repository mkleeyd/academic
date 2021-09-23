package management.academic.schoolregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

/**
 * <학기별 성적 산출 결과 엔티티>
 */
@Entity
@Getter
//@IdClass(StuNoPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShtmScoreResult
//        extends BaseEntity
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shtmScoreResultNo")
    private Long id;

    private String acqLctpt; // 취득학점(중간 + 기말 더하기)

    private String acqLctptAvgAvrp; // 취득학점 평균평점( 중간+기말/2 )

//    private String rank; // 석차 (Stream으로 처리)

//    private String acqLctptAvrp; // 취득학점 평점 (간소화하기 위해서 사용 안함)



    /**
     * <Member Entity_학생 정보 엔티티에서 가져올 컬럼>
     * private Long stuNo; // 학번
     * private String enterYear; // 입학 년도
     * private String sustCd;  // 학과코드(법학, 행정학)
     * private String mjrCd;   // 전공코드
     * rivate String shysCd;  // 학년코드
     * private String shtmCd;  // 학기코드
     */
    @JsonIgnore // 보통 부모 테이블인 PK가 있는 Member를 더 많이 조회할테니 여기 자식 쪽에 걸어줌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;


    public static ShtmScoreResult createShtmScoreResult(Member member, String sum, String result) {
        ShtmScoreResult shtmScoreResult = new ShtmScoreResult();
        shtmScoreResult.acqLctpt =  sum;
        shtmScoreResult.acqLctptAvgAvrp = result;
        shtmScoreResult.member = member;
        member.getShtmScoreResult().add(shtmScoreResult); // 연관관계에도 값 넣어줌(양방향) 솔직히 필요없음(Key를 가진 쪽에만 정확히 넣어주면 됨)
        return shtmScoreResult;
    }
    // F4 로 연관관계 타고 들어가면서 충분히 강좌별 성적 등록 엔티티 조회 할 수 있으니 성적 등록 엔티티  추가 하지 않음

    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//

    //===== 엔티티 비지니스 로직 =====//

}
