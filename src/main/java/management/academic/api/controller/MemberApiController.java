package management.academic.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import management.academic.api.dto.MemberApiFormDto;
import management.academic.api.dto.MemberApiSaveFormDto;
import management.academic.api.dto.MemberApiSearchCondition;
import management.academic.api.dto.MemberApiUpdateFormDto;
import management.academic.schoolregister.entity.Address;
import management.academic.schoolregister.entity.Gender;
import management.academic.schoolregister.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <공부 할 내용들>
 *   1. CRUD 먼저 작업
 *   2. 다음 DTO + fetch join 사용하여 조회 최적화 및 라이프 사이클 맞추기(API 스펙은 자주 바뀌기 대문에 별도의 라이프 사이클을 가지는게 좋음)
 *   3. 페이징 처리 하기
 *   4. junit API 테스트 해보고 Spring REST Docs 사용하기
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

//    @PostConstruct
//    public void initializing() {
//        for (int i = 0; i < 100; i++) {
//            Member user = Member.builder()
//                    .username("User " + i)
//                    .address("Korea")
//                    .age(i)
//                    .build();
//            userRepository.save(user);
//        }
//    }


    /**
     * <v1_조회 : Return 객체 + DTO 객체 + wrapper Class 데이터 감싸기 이용>
     *
     *  - 모든 필드 받는 "All 생성자" 필요함
     *  - 아!!? Wrapper 클래스로 감싸기 때문에 List<객체> 반환이 아닌 객체 1개 자체만 반환해줘도 안에 데이터가 다 들어있네ㅋㅋㅋ
     *  - 쉽게 생각해서 Wrapper Class 안에 컬렉션의 List가 들어있는 꼴
     */
    @GetMapping("/api/v1/members")
    public MemberReturnV1 membersV1(){  
        List<MemberApiFormDto> members = memberService.findMembersApiV1();
        return new MemberReturnV1(members, members.size());
    }

    /**
     * <v1.1_조회 : Return 객체 + DTO 객체>
     *
     *  - wrapper Class 없이 "일반 클래스"인 Return 객체를 List 형태 그대로 반환하기
     *  - 이렇게 하려면 한번 더 Return 객체로 변환해줘야 할 필요가 있네
     *  - 이게 더 손해인게 일단 Map 변환이 2번 일어남( 엔티티 -> DTO -> Return )
     *  - 따라서 cost가 많이 발생하기 떄문에 차차리 위에 방법이 훨씬 효율적이고 유연함
     */
    @GetMapping("/api/v1.1/members")
    public List<MemberReturnV1_1> membersV1_1(){  // 한명만 반환할게 아닌데 이렇게 해야 되나?
        List<MemberApiFormDto> members = memberService.findMembersApiV1();
        List<MemberReturnV1_1> collect = members.stream()
                .map(m -> new MemberReturnV1_1(m))
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * <v2_등록 : Return 객체 + DTO 객체 + 파라미터>
     *
     *  - @NotEmpty, @RequestBody, @Valid <== DTO 관련
     *  - 등록할 땐 View단에 맞는 API 스펙과 동일한 DTO를 생성하고 DTO에 Validation을 걸어서 필수값들 확인 후 등록 수행
     *  - 핵심 로직 엔티티의 필드들은 바뀔 경우가 생기게 되면 영향이 크므로 따로 DTO 만들어서 사용하는 것
     *  - 반환은 무조건 Json 형태로 뿌릴 수 있는 객체 또는 컬렉션 형태로 반환해 줘야 한다(Id, value 형태) ??? 아닌듯
     *  - 그냥 Long 반환해도 될듯?? 있다가 테스트
     *      - 객체를 반환했을 때 <=== 이렇게 반환해야 맞다
     *          {
     *              "id": 3
     *          }
     *
     *      - Long 타입을 그냥 반환 했을 때 <=== 이렇게 보내면 '4' 라는 값의 의미를 알수가 없음 (반드시 객체 또는 컬렉션을 반환해줘야 함)
     *          4
     *
     *  - View단 데이터를 받아와 서버단과 연동되어 매핑되는 DTO들은 모두 Setter, 기본 생성자는 필수고 가지고 있어야 한다!!!!
     *
     *  --- 제일 중요한 것!!!
     *   - 현재 나는 JWT 를 사용중이기 때문에 값을 저장하거나 업데이트하거나 삭제하는 form 태그와 엮어서 하는 작업들은 토큰이 필요함!!!!
     *   - 이거 아직 해결 못했음 일단 모든 url 통과하도록 코드 수정해놓고 진도 나간 후 나중에 다시 해보자(내가 짠 시큐리티가 문제가 있어서 더 어려운듯)
     */
    @PostMapping("/api/v2/member/save")
    public MemberReturnV23 membersV2(@RequestBody @Valid MemberApiSaveFormDto memberApiSaveFormDto){
        System.out.println("memberApiSaveFormDto = " + memberApiSaveFormDto.toString());
        Long newMemberId = memberService.newMemberSaveApiV2(memberApiSaveFormDto);
        return new MemberReturnV23(newMemberId);
    }

    /**
     * <v3_수정 : Return 객체 + DTO 객체 + 파라미터>
     *
     *  - 싱글톤이라고 물리적으로 1개의 객체를 공유하는 개념이 아니라 트랜잭션 안에서는 각각의 객체마다 동일성을 보장해줌
     */
    @PutMapping("/api/v3/member/update/{id}")
    public MemberReturnV23 membersV3(@PathVariable("id") Long memberId,
                                     @RequestBody @Valid MemberApiUpdateFormDto memberApiUpdateFormDto){
        Long updateResult = memberService.updateMemberApiV3(memberId, memberApiUpdateFormDto);
        return new MemberReturnV23(updateResult);
    }

    
    /**
     * <v4_조회(페이징) : Form DTO + spring-data-jpa 기본 메서드 이용한 페이징>
     *
     *  - spring-data-jpa에서 제공하는 Page 인터페이스를 이용한 가장 단순한 예제
     *  - 파라미터를 통항 동적인 페이징 처리가 아닌 정적인 페이징 처리로 단순한 예제
     *  - 여기서 중요한 것은 DTO로 변환할 때 stream()은 빼고 그냥 map()만 써서 그대로 Page 타입으로 받아야 하는 것 중요!!!
     *  - 페이징 관련 Controller는 반환할 때는 Return 객체 사용하는 것이 아니라 FormDTO 그대로 반환하는 것이다!!!!!!!!!!!!!!!!!!!!!
     *  - @JsonIgnore : 양방향 연결 관계 이기 떄문에 API 호출하면 무한루프 빠지는 것을 방지하기 위해 양쪽 중 한 곳에(보통 깊게 들어가서) @JsonIgnore 붙여줌
     *      - 깊은 곳이라는 것은 메인으로 조회하는 엔티티가 가지고 있는 참조 필드에서 F4를 눌러 쭉 타고 들어가서 @JsonIgnore 붙인다는 뜻
     *
     *  - @PageableDefault 어노테이션을 선언해 주고 page에 대한 설정을 할 수 있다.
     *      - size : 한 페이지에 담을 모델의 수를 정할 수 있다.
     *      - sort : 정렬의 기준이 되는 속성을 정할 수 있다.
     *      - direction : 오름차순과 내림차순 중 기준을 선택한다.
     *      - Pageable pageable : PageableDefault 값을 갖고 있는 변수를 선언한다.
     *
     */
    @GetMapping("/api/v4/member/paging")
    public Page<MemberApiFormDto> membersV4(@PageableDefault(size = 15, sort = "id") Pageable pageable) {
        Page<MemberApiFormDto> result = memberService.findMembersApiV4(pageable);
        return result;
    }


    /**
     * <v5_조회(페이징) : Form DTO + spring-data-jpa @Query 페이징>
     *
     *  - 페이징 관련 Controller는 반환할 때는 Return 객체 사용하는 것이 아니라 FormDTO 그대로 반환하는 것이다!!!!!!!!!!!!!!!!!!!!!
     *  - @JsonIgnore : 양방향 연결 관계 이기 떄문에 API 호출하면 무한루프 빠지는 것을 방지하기 위해 양쪽 중 한 곳에(보통 깊게 들어가서) @JsonIgnore 붙여줌
     *      - 깊은 곳이라는 것은 메인으로 조회하는 엔티티가 가지고 있는 참조 필드에서 F4를 눌러 쭉 타고 들어가서 @JsonIgnore 붙인다는 뜻
     *
     */
    @GetMapping("/api/v5/member/paging")
    public Page<MemberApiFormDto> membersV5(@PageableDefault(size = 7, sort = "id") Pageable pageable) {
        Page<MemberApiFormDto> result = memberService.findMembersApiV5(pageable);
        return result;
    }

    /**
     * <v6_조회(페이징) : Form DTO + 파라미터 + spring-data-jpa 기본 메서드 페이징>
     *
     *  - 파라미터를 받아 사용하는 동적 페이징 처리 예제
     *  - 여기서 중요한 것은 DTO로 변환할 때 stream()은 빼고 그냥 map()만 써서 그대로 Page 타입으로 받아야 하는 것 중요!!!
     *  - 페이징 관련 Controller는 반환할 때는 Return 객체 사용하는 것이 아니라 FormDTO 그대로 반환하는 것이다!!!!!!!!!!!!!!!!!!!!!
     *  - @JsonIgnore : 양방향 연결 관계 이기 떄문에 API 호출하면 무한루프 빠지는 것을 방지하기 위해 양쪽 중 한 곳에(보통 깊게 들어가서) @JsonIgnore 붙여줌
     *      - 깊은 곳이라는 것은 메인으로 조회하는 엔티티가 가지고 있는 참조 필드에서 F4를 눌러 쭉 타고 들어가서 @JsonIgnore 붙인다는 뜻
     *
     */
    @GetMapping("/api/v6/member/paging")
    public Page<MemberApiFormDto> membersV6(
            @RequestParam(value = "offset", defaultValue = "0") int offset,   // 페이지 번호(0이면 맨 처음 페이지부터)
            @RequestParam(value = "limit", defaultValue = "100") int limit) { // 페이지 번호당 보여줄 모델 개수(보통 20개씩)
        // 아래 이게 핵심
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "id"));
        Page<MemberApiFormDto> result = memberService.findMembersApiV6(pageRequest);
        return result;
    }


    /**
     * <v7_조회(페이징) : Form DTO + Condition 객체 + spring-data-JPA + Querydsl + 페이징>
     *
     *  - View단에서 검색 동적 쿼리 와 페이징 초기값을 받아 뿌려주는 예제
     *  - 페이징을 반환할 때는 Return 객체 사용하는 것이 아니라 FormDTO 그대로 반환하는 것이다!!!!!!!!!!!!!!!!!!!!!
     *  - @JsonIgnore : 양방향 연결 관계 이기 떄문에 API 호출하면 무한루프 빠지는 것을 방지하기 위해 양쪽 중 한 곳에(보통 깊게 들어가서) @JsonIgnore 붙여줌
     *      - 깊은 곳이라는 것은 메인으로 조회하는 엔티티가 가지고 있는 참조 필드에서 F4를 눌러 쭉 타고 들어가서 @JsonIgnore 붙인다는 뜻
     *
     *  - http://localhost:8080/api/v7/member/paging?id=7&page=0&size=2 ( 파라미터 순서는 상관 없음. )
     *  - 그리고 이렇게 위에 파라미터처럼 하면 1명만 조회되어서 값이 제대로 안나오는 것처럼 보임
     */
    @GetMapping("/api/v7/member/paging")
    public Page<MemberApiFormDto> membersV7(MemberApiSearchCondition condition, Pageable pageable){
        Page<MemberApiFormDto> result = memberService.findAndSearchMembersApiV7(condition, pageable);
        return result;
    }

    /**
     * <v8_조회(페이징) : Form DTO + Condition 객체 + spring-data-JPA + Querydsl + 페이징 심화>
     *
     *  - 페이징을 반환할 때는 Return 객체 사용하는 것이 아니라 FormDTO 그대로 반환하는 것이다!!!!!!!!!!!!!!!!!!!!!
     *  - @JsonIgnore : 양방향 연결 관계 이기 떄문에 API 호출하면 무한루프 빠지는 것을 방지하기 위해 양쪽 중 한 곳에(보통 깊게 들어가서) @JsonIgnore 붙여줌
     *      - 깊은 곳이라는 것은 메인으로 조회하는 엔티티가 가지고 있는 참조 필드에서 F4를 눌러 쭉 타고 들어가서 @JsonIgnore 붙인다는 뜻
     *
     *  - http://localhost:8080/api/v8/member/paging?id=7&page=0&size=2 ( 파라미터 순서는 상관 없음. )
     *  - 그리고 이렇게 위에 파라미터처럼 하면 1명만 조회되어서 값이 제대로 안나오는 것처럼 보임
     */
    @GetMapping("/api/v8/member/paging")
    public Page<MemberApiFormDto> membersV8(MemberApiSearchCondition condition, Pageable pageable){
        Page<MemberApiFormDto> result = memberService.findAndSearchMembersApiV8(condition, pageable);
        return result;
    }

//====================================================================================================================//

    // v1 리턴 객체
    @Data
    @AllArgsConstructor
    class MemberReturnV1<T>{

        private T data;
        private int count;
    }/////

    // v1.1 리턴 객체
    @Data
    class MemberReturnV1_1{
        private Long id;
        private String stuNo;   // 일반 서비스에서 중복없는 username과 같은 역할
        private String name;    // 성명
        private String sustCd;  // 학과코드(법학, 행정학)
        private String mjrCd;   // 전공코드
        private String shysCd;  // 학년코드
        private String shtmCd;  // 학기코드
        private String finSchregDivCd; // 학적상태코드
        private String email; // 이메일
        private Gender gender; // 성별 enum 타입
        private Address address; // 주소 임베디드 타입

        public MemberReturnV1_1(MemberApiFormDto members) {
            this.id = members.getId();
            this.stuNo = members.getStuNo();
            this.name = members.getName();
            this.sustCd = members.getSustCd();
            this.mjrCd = members.getMjrCd();
            this.shysCd = members.getShysCd();
            this.shtmCd = members.getShtmCd();
            this.finSchregDivCd = members.getFinSchregDivCd();
            this.email = members.getEmail();
            this.gender = members.getGender();
            this.address = members.getAddress();
        }
    }/////

    // v2, v3 리턴 객체
    @Data
    @AllArgsConstructor
    class MemberReturnV23 {
        private Long id;
    }

}/////
