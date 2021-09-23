package management.academic.college.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.schoolregister.entity.BaseEntity;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.ShtmScore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <수강 신청 엔티티>
 */
@Entity
@Getter
//@IdClass(StuNoPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrolment
//        extends BaseEntity
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrolmentNo")
    private Long id;
    /**
     * 밑에 필드 이것들은 여기 엔티티에서 가져고 있어야 할 것 같음
     * 이유는 혹시라도 join으로 계속 쓰면 개설 강좌 엔티티가 변경이 되면 수강 신청 엔티티까지 값이 변경되기 때문에
     * 고유 엔티티는 가지고 있는게 맞는 듯
     *
     * 쉽게 생각해서, join한 엔티티에서 값이 가져올 경우 해당 엔티티를 변경하게 되면 엮여있는 모든 엔티티까지 select할 때 변경된 값으로 되기 때문에
     * 값의 전파를 끊어주면서 설계를 해줘야 한다!!!!!!!!!!!!!!!!!!
     */
    private String openYear; // 개설년도
    private String openShtmCd; // 강좌 개설학기 코드
    private String lectNo; // 강좌번호
    private String cptnDivCd; // 이수구분 코드
    private String lctpt; // 학점

//    private String name; // 강좌명   이거 빼먹었음ㅠㅠ
    
    /**
     * <Member Entity_학생 정보 엔티티에서 가져올 컬럼>
     *  private Long stuNo; // 학번
     *  private String enterYear; // 입학 년도
     *  rivate String shysCd;  // 학년코드
     *  private String shtmCd;  // 학기코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    /**
     * <openClassNo Entity_강좌 개설 엔티티에서 가져올 컬럼>
     *  private OpenClass openClass;
     *  private Integer openYear; // 개설년도
     *  private String lectNo; // 강좌번호
     *  private String cptnDivCd; // 이수구분 코드
     *  private String lctpt; // 학점
     */
    @OneToOne(fetch = FetchType.LAZY) // 하나의 수강신청 데이터는 하나의 동일한 개설 정보와 관계를 맺음
    @JoinColumn(name = "openClassNo")
    private OpenClass openClass;    // 수강 신청 엔티티로


    public static Enrolment createEnrolment(OpenClass openClass, Member member) {
        Enrolment enrolment = new Enrolment();
        enrolment.member = member; // member 객체 자체를 넘기는 것이 포인트!!!
        enrolment.openClass = openClass;
        enrolment.openYear = openClass.getOpenYear(); // 개설년도
        enrolment.openShtmCd = openClass.getOpenShtmCd(); // 강좌 개설학기 코드
        enrolment.lectNo = openClass.getLectNo(); // 강좌번호
        enrolment.cptnDivCd = openClass.getCptnDivCd(); // 이수구분 코드
        enrolment.lctpt = openClass.getLctpt(); // 학점
        openClass.changeJoinedCnt(openClass.getJoinedCnt(), "plus");

        return enrolment;
    }


    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//

    //===== 엔티티 비지니스 로직 =====//

}/////