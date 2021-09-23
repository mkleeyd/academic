package management.academic.schoolregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import management.academic.common.entity.register.FinSchregDivCd;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.MemberSearchCondition;
import org.hibernate.annotations.BatchSize;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
 *
 * <N+1은 지연로딩 때문에 발생하는 것!!! 따라서, fetch join으로 즉시로딩처럼 다 가져와서 처리하면 됨>
 */
@Entity
@Getter
//@IdClass(StuNoPk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member
//        extends BaseEntity    // 값은 직접 넣어줘야 하네 컬럼만 만들어줌
        implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    private String stuNo;   // 일반 서비스에서 중복없는 username과 같은 역할

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

    /**
     * @JsonIgnore 사용 이유
     *   - 양방향 연결 관계 이기 떄문에 API 호출하면 무한루프 빠지는 것을 방지하기 위해 양쪽 중 한 곳에(보통 깊게 들어가서) @JsonIgnore 붙여줌
     *   - 어차피 한쪽에서 해당 객체에 접근에서 값을 가져오면 다른 쪽에서는 똑같은 데이터를 가져올 필요가 없기 때문에 이렇게 한쪽 막아주는 것임
     *
     * 해결방법
     *   - DTO 사용 : 위와 같은 상황이 발생하게 된 주 원인은 '양방향 맵핑'이기도 하지만,
     *     더 정확하게는 entity 자체를 response로 리턴한데에 있다. entity 자체를 return 하지 말고,
     *     dto 객체를 만들어 필요한 데이터만 옮겨담아 client로 리턴하면 순환참조와 관련된 문제는 애초에 방어할수 있다.
     */

    @OneToMany(mappedBy = "member") // "여기에선 값을 CRUD 하지 말아라" 의미
    private List<ShtmScore> shtmScore = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ShtmScoreResult> shtmScoreResult = new ArrayList<>();


    //===== 연관관계 편의 메서드(양방향 연관관계일 경우에만 작성_여기는 안해줘도 되고 값을 넣는 곳에만 해주면 됨) =====//

    //===== 엔티티 비지니스 로직 =====//
    public static Member createMember(MemberFormDto memberFormDto){
        Member member = new Member();

        // 여기 중복 있을 수 있으니까 방법 찾아야 함
        // 방법은 PK도 항상 같이 조회할 때 사용하는 것
        member.stuNo = (Integer.toString(LocalDateTime.now().getYear())+(int)(Math.random() * 9999 + 1));

        member.enterYear = Integer.toString(LocalDateTime.now().getYear());
        member.name = Optional.ofNullable(memberFormDto.getName()).orElse("이름 없음");
        member.birthMd = Optional.ofNullable(memberFormDto.getBirthMd()).orElse("생년월일 없음");
        member.sustCd = Optional.ofNullable(memberFormDto.getSustCd()).orElse("학과 없음");
        member.mjrCd = Optional.ofNullable(memberFormDto.getMjrCd()).orElse("전공 없음");
        member.shysCd = "1";
        member.shtmCd = "1";
        member.finSchregDivCd = FinSchregDivCd.FinSchregDivCd001.getCode();
        member.cptnShtmCnt = 1;
        member.email = Optional.ofNullable(memberFormDto.getEmail()).orElse("이메일 없음");
        member.gender = memberFormDto.getGender();
        member.address = new Address(memberFormDto.getAddress().getCity(),memberFormDto.getAddress().getStreet(), memberFormDto.getAddress().getZipcode());
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



    /**
     * 학적 상태 값 변경
     */
    public void changeStatus(String finSchregDivCd) {
        this.finSchregDivCd = finSchregDivCd;
    }

}/////
