package management.academic.schoolregister.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.college.entity.Enrolment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <학기별 성적 등록 엔티티>
 */
@Entity
@Getter
@IdClass(StuNoPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShtmScore
//        extends BaseEntity
{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shtmScoreNo")
    private Long id;

    @Id
    @Column(name = "stuNo")
    private String stuNo; // 학번 (여기 필드명과 StuNoPk 복합키 클래스 필드명과 동일해야 한다)

    @Id
    @Column(name = "enterYear")
    private String enterYear; // 입학년도 (여기 필드명과 StuNoPk 복합키 클래스 필드명과 동일해야 한다)

    private Float midexamScr; // 중간고사점수

    private Float flexmScr; // 기말고사점수

    private Float lsrtTtSumScr; // 성적 총합계점수

    private String scrGrdCd; // 점수등급코드

    private Float avrp; // 평점

    /**
     * <Member Entity_학생 정보 엔티티에서 가져올 컬럼>
     * private Long stuNo; // 학번
     * private String enterYear; // 입학 년도
     * private String sustCd;  // 학과코드(법학, 행정학)
     * private String mjrCd;   // 전공코드
     * private String shysCd;  // 학년코드
     * private String shtmCd;  // 학기코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "enterYear", referencedColumnName = "enterYear", insertable = false, updatable = false),
            @JoinColumn(name = "stuNo", referencedColumnName = "stuNo", insertable = false, updatable = false)
    }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;



    /**
     * <Enrolment Entity_수강 신청 엔티티에서 가져올 컬럼>
     * private Integer openYear; // 강좌 개설 년도
     * private String lectNo; // 강좌번호
     * private String tlsnShysCd; // 수강학년기코드
     * private String cptnDivCd; // 이수구분 코드
     * private Float lctpt; // 학점
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "enterYear", referencedColumnName = "enterYear", insertable = false, updatable = false),
            @JoinColumn(name = "stuNo", referencedColumnName = "stuNo", insertable = false, updatable = false)
    }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Enrolment enrolment;
    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//

    //===== 엔티티 비지니스 로직 =====//
}
