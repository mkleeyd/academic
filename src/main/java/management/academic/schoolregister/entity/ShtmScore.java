package management.academic.schoolregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.college.entity.Enrolment;
import management.academic.schoolregister.dto.ScoreSaveForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <학기별 성적 등록 엔티티>
 */

/** @IdClass(StuNoPk.class)
 *     @Id
 *     @Column(name = "stuNo")
 *     private String stuNo; // 학번 (여기 필드명과 StuNoPk 복합키 클래스 필드명과 동일해야 한다)
 *
 *     @Id
 *     @Column(name = "enterYear")
 *     private String enterYear; // 입학년도 (여기 필드명과 StuNoPk 복합키 클래스 필드명과 동일해야 한다)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShtmScore
//        extends BaseEntity
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shtmScoreNo")
    private Long id;

    private String midexamScr; // 중간고사점수

    private String flexmScr; // 기말고사점수

//    정말 상세한 성적은 나중에 리팩토링 하면서 추가하자
//    private String lsrtTtSumScr; // 성적 총합계점수 (비지니스 메서드 생성)
//    private String scrGrdCd; // 점수등급코드 (비지니스 메소드 생성)
//    private String avrp; // 평점 (비지니스 메서드 생성)

    /**
     * <Member Entity_학생 정보 엔티티에서 가져올 컬럼>
     * private Long stuNo; // 학번
     * private String enterYear; // 입학 년도
     * private String sustCd;  // 학과코드(법학, 행정학)
     * private String mjrCd;   // 전공코드
     * private String shysCd;  // 학년코드
     * private String shtmCd;  // 학기코드
     */
    @JsonIgnore // 보통 부모 테이블인 PK가 있는 Member를 더 많이 조회할테니 여기 자식 쪽에 걸어줌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;


    /**
     * <Enrolment Entity_수강 신청 엔티티에서 가져올 컬럼>
     * private Integer openYear; // 강좌 개설 년도
     * private String lectNo; // 강좌번호
     * private String cptnDivCd; // 이수구분 코드
     * private String lctpt; // 학점
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrolmentNo")
    private Enrolment enrolment;

    //===== 엔티티 비지니스 로직 =====//
    public static ShtmScore createShtmScore(Member member, Enrolment enrolment) {
        ShtmScore shtmScore = new ShtmScore();
        shtmScore.member = member;
        member.getShtmScore().add(shtmScore);   // 연관관계에도 값 넣어줌(양방향) 솔직히 필요없음(Key를 가진 쪽에만 정확히 넣어주면 됨)
        shtmScore.enrolment = enrolment;
        shtmScore.midexamScr = "0";
        shtmScore.flexmScr = "0";
        return shtmScore;
    }

    /**
     * 중간, 기말 점수 업데이트
     */
    public void updateScore(String midexamScr, String flexmScr) {
        this.midexamScr = midexamScr;
        this.flexmScr = flexmScr;
    }


    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//


}
