package management.academic.college.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.schoolregister.entity.BaseEntity;

import javax.persistence.*;

/**
 * <강좌 개설 엔티티>
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenClass
//        extends BaseEntity
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "openClassNo")
    private Long id;

    private String openYear; // 개설년도

    private String openShtmCd; // 개설 학기 코드

    private String lectNo; // 강좌번호

    private String openYn; // 개설여부{enum 타입}

    private String cptnDivCd; // 이수구분 코드

    private Float lctpt; // 학점

    private Integer limitedCnt; // 수강가능 인원 수

    private Integer joinedCnt; // 수강인원 수

    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//

    //===== 엔티티 비지니스 로직 =====//
    public void changeJoinedCnt(){
        // 수강인원 증가
        this.joinedCnt = ++joinedCnt;

        // 수강인원 감소
        this.joinedCnt = --joinedCnt;
    }

}/////
