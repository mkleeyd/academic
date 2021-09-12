package management.academic.schoolregister.entity;

import lombok.*;
import management.academic.common.entity.register.FinSchregDivCd;
import management.academic.common.entity.register.MjrCd;
import management.academic.common.entity.register.SustCd;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.MemberSearchCondition;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <학생 정보 엔티티>
 *
 * 테이블 설계 단계에서 이미 master(Field) <-- slave(List) 가 정해지고 PK, FK가 정해진다
 * 이것을 토대로 Entity를 설계한다
 * 이 때, Master쪽이 One이 되고, Slave쪽이 Many가 된다
 * 
 * 연관관계 == 참조관계
 *
 *
 * Entity들이 기본 생성자를 PROTECTED 로 하는 이유는 처음에 스프링 이 실행될 때 싱글톤으로 한번만 객체를 생성하고 이후에는
 * 다른곳에서 객체를 생성할 수 없도록 막은 것
 *
 * 따라서, DTO 객체는 어디서든 만들 수 있도록 기본 생성자를 public 으로 열어둬야 한다
 */
@Entity
@Getter
@IdClass(StuNoPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member
//        extends BaseEntity    // 값은 직접 넣어줘야 하네 컬럼만 만들어줌
        implements Serializable {

    @Column(name = "stuNo")
    private String stuNo;

    @Id
    @Column(name = "enterYear")
    private String enterYear; // 입학년도(복합키 사용)

    private String name;    // 성명

    private String birthMd; // 생년월일

    private String sustCd;  // 학과코드(법학, 행정학)

    private String mjrCd;   // 전공코드

    private String shysCd;  // 학년코드

    private String shtmCd;  // 학기코드

    private String finSchregDivCd; // 학적상태코드

    private Integer cptnShtmCnt; // 학기수

    private String email; // 이메일

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별 enum 타입

    @Embedded
    private Address address; // 주소 임베디드 타입

    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성_여기는 안해줘도 되고 값을 넣는 곳에만 해주면 됨) =====//

    //===== 엔티티 비지니스 로직 =====//
    public static Member createMember(MemberFormDto memberFormDto){
        Member member = new Member();
        member.name = memberFormDto.getName();
        member.enterYear = String.valueOf(LocalDateTime.now().getYear());
        System.out.println("LocalDateTime.now().getYear() = " + LocalDateTime.now().getYear());
        member.birthMd = memberFormDto.getBirthMd();
        // 학과
        System.out.println("memberFormDto.getSustCd() ====>" + memberFormDto.getSustCd());
        System.out.println("memberFormDto.getSustCd() ====>" + memberFormDto.getMjrCd());
        if (memberFormDto.getSustCd().equals("SUSTCD001")) {
            member.sustCd = SustCd.SUSTCD001.getCode(); // 경찰법학
        }
        else {
            member.sustCd = SustCd.SUSTCD002.getCode();
        }
        // 전공
        if (memberFormDto.getMjrCd().equals("MJRCD001")) {
            member.mjrCd = MjrCd.MJRCD001.getCode(); // 경찰법학
        }
        else if(memberFormDto.getMjrCd().equals("MJRCD002")){
            member.mjrCd = MjrCd.MJRCD002.getCode(); // 경찰법학
        }
        else if(memberFormDto.getMjrCd().equals("MJRCD003")){
            member.mjrCd = MjrCd.MJRCD003.getCode(); // 경찰법학
        }
        else{
            member.mjrCd = MjrCd.MJRCD004.getCode(); // 경찰법학
        }
        // 학년
        member.shysCd = "1";
        // 학기
        member.shtmCd = "1";
        // 학적 상태
        member.finSchregDivCd = FinSchregDivCd.FinSchregDivCd001.getCode();
        // 이수학기수
        member.cptnShtmCnt = 1;
        // 이메일
        member.email = memberFormDto.getEmail();
        // 성별
        if (memberFormDto.getGender().equals("1")) {
            member.gender = Gender.MAN;
        }else{
            member.gender = Gender.WOMAN;
        }
        // 주소
        member.address = new Address(memberFormDto.getCity(), memberFormDto.getStreet(), memberFormDto.getZipcode());
        return member;
    }

    public void addCptnShtmCnt(Integer cptnShtmCnt){
        this.cptnShtmCnt = cptnShtmCnt + 1;
    }

    /**
     * static은 메서드(static) 영역에 객체 자체가 주소값이 정적으로 할당받아 저장됨)
     * 일반 객체는 new 키워드로 Heap 영역에 주소값이 동적으로 할당 됨(선언부 == stack영역, new 할당 == Heap 영역 ==>> Heap에 등록된 객체의 메모리 주소 값을 Stack 영역에 보관하고 찾아간다)
     * static은 서비스 실행 시 딱 한번 정적 주소에 올려놓고 사용(클래스에 고정되어진 멤버(변수, 메서드)
     * static을 품은 객체.static 변수 또는 메서드 하면 어디서든 동일한 정적 주소값으로 접근 가능
     * static 메서드에선 일반멤버 접근 불가
     * - 일반멤버는 new로 주소를 할당받지 않았기 때문에 주소가 주소값이 없기 때문에 접근 불가(정확히 말하면 그냥 주소 할당을 안받아서 접근이 불가능한 것)
     *
     * static 멤버를 품은 객체를 new로 주소를 할당해주면 static을 품은 객체의 지역변수에도 접근 가능(대신 그놈은 지역변수로 공유 개념 아님)
     * <메모리 할당 == 주소 할당>
     *
     */
    public static MemberSearchCondition PkEnterAndSeq(MemberSearchCondition memberSearchCondition) {
        /**
         * 파라미터 stuNo에서 앞에 4자리 추출하여 EnterYear에 저장하기
         */
        if (StringUtils.hasText(memberSearchCondition.getStuNo())) {
            String enterYear = memberSearchCondition.getStuNo().substring(0,4);
            String seq = memberSearchCondition.getStuNo().substring(enterYear.length(), memberSearchCondition.getStuNo().length());
            memberSearchCondition.setEnterYear(enterYear);
            memberSearchCondition.setStuNo(seq);
            return memberSearchCondition;
        }
        return memberSearchCondition;
    }


    public static MemberFormDto memberWhereStuNo(MemberFormDto memberFormDto) {
        if (StringUtils.hasText(memberFormDto.getStuNo())) {
            String enterYear = memberFormDto.getStuNo().substring(0,4);
            String seq = memberFormDto.getStuNo().substring(enterYear.length(), memberFormDto.getStuNo().length());
            memberFormDto.setEnterYear(enterYear);
            memberFormDto.setStuNo(seq);
            return memberFormDto;
        }
        return memberFormDto;
    }

    /**
     * 학적 상태 값 변경
     */
    public void changeStatus(String finSchregDivCd) {
        this.finSchregDivCd = finSchregDivCd;
    }

}/////
