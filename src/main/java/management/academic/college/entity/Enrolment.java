package management.academic.college.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.schoolregister.entity.BaseEntity;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.StuNoPk;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <수강 신청 엔티티>
 */
@Entity
@Getter
@IdClass(StuNoPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrolment
//        extends BaseEntity
{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrolmentNo")
    private Long id;

    @Id
    @Column(name = "stuNo")
    private String stuNo; // 학번 (여기 필드명과 StuNoPk 복합키 클래스 필드명과 동일해야 한다)

    @Id
    @Column(name = "enterYear")
    private String enterYear; // 입학년도 (여기 필드명과 StuNoPk 복합키 클래스 필드명과 동일해야 한다)


    /**
     * <Member Entity_학생 정보 엔티티에서 가져올 컬럼>
     *  private Long stuNo; // 학번
     *  private String enterYear; // 입학 년도
     *  rivate String shysCd;  // 학년코드
     *  private String shtmCd;  // 학기코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "enterYear", referencedColumnName = "enterYear", insertable = false, updatable = false),
            @JoinColumn(name = "stuNo", referencedColumnName = "stuNo", insertable = false, updatable = false)
    }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    /**
     * <openClassNo Entity_강좌 개설 엔티티에서 가져올 컬럼>
     *  private OpenClass openClass;
     *  private Integer openYear; // 개설년도
     *  private String lectNo; // 강좌번호
     *  private String cptnDivCd; // 이수구분 코드
     *  private Float lctpt; // 학점
     */
    @OneToOne(fetch = FetchType.LAZY) // 하나의 수강신청 데이터는 하나의 동일한 개설 정보와 관계를 맺음
    @JoinColumn(name = "openClassNo")
    private OpenClass openClass;    // 수강 신청 엔티티로

    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//

    //===== 엔티티 비지니스 로직 =====//

}/////