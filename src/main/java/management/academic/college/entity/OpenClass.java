package management.academic.college.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.college.dto.CollegeFormDto;
import management.academic.common.entity.college.OpenShtmCd;
import management.academic.schoolregister.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    private String name; // 강좌명

    private String openYear; // 개설년도

    private String openShtmCd; // 강좌 개설학기 코드

    private String lectNo; // 강좌번호

    private String openYn; // 개설여부{enum 타입}

    private String cptnDivCd; // 이수구분 코드

    private String lctpt; // 학점

    private Integer limitedCnt; // 수강가능 인원 수

    private Integer joinedCnt; // 수강인원 수

    public static OpenClass createSubject(CollegeFormDto collegeFormDto) {
        OpenClass openClass = new OpenClass();
        openClass.name = collegeFormDto.getName();
        openClass.openYear = collegeFormDto.getOpenYear();
        openClass.openShtmCd = collegeFormDto.getOpenShtmCd();
        openClass.lectNo = UUID.randomUUID().toString();        // 강좌번호 UUID 사용
        openClass.openYn = collegeFormDto.getOpenYn();
        openClass.cptnDivCd = collegeFormDto.getCptnDivCd();
        openClass.lctpt = collegeFormDto.getLctpt();
        openClass.limitedCnt = collegeFormDto.getLimitedCnt();
        openClass.joinedCnt = 0;  // 자동으로 0 들어갈듯
        return openClass;
    }

    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성) =====//

    //===== 엔티티 비지니스 로직 =====//
    public void changeJoinedCnt(Integer joinedCnt, String gubn){
        // 수강인원 증가
        if (gubn.equals("plus")) this.joinedCnt = ++joinedCnt;
        else this.joinedCnt = --joinedCnt;
        // 수강인원 감소

    }

}/////
