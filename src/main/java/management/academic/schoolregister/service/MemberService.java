package management.academic.schoolregister.service;

import lombok.RequiredArgsConstructor;
import management.academic.api.dto.MemberApiFormDto;
import management.academic.api.dto.MemberApiSaveFormDto;
import management.academic.api.dto.MemberApiSearchCondition;
import management.academic.api.dto.MemberApiUpdateFormDto;
import management.academic.common.entity.register.FinSchregDivCd;
import management.academic.common.entity.register.MjrCd;
import management.academic.common.entity.register.SustCd;
import management.academic.schoolregister.dto.*;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.ShtmScore;
import management.academic.schoolregister.entity.ShtmScoreResult;
import management.academic.schoolregister.repository.MemberRepository;
import management.academic.schoolregister.repository.ShtmScoreRepository;
import management.academic.schoolregister.repository.ShtmScoreResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final ShtmScoreRepository shtmScoreRepository;
    
    private final ShtmScoreResultRepository shtmScoreResultRepository;

    /**
     * <학생 등록>
     * @param memberFormDto
     * @return
     */
    @Transactional
    public String newMemberSave(MemberFormDto memberFormDto) {
        Member member = Member.createMember(memberFormDto);
        Member result = memberRepository.save(member);
        return result.getEnterYear() + result.getStuNo();
    }

    /**
     * <모든 학생 조회>
     */
    public List<MemberFormDto> findAllMember(MemberSearchCondition memberSearchCondition){
        // data jpa 인터페이스를 가져왔는데 그 안에 커스텀 인터페이스 안에 있는 저장소에 접근 가능한거 가능한겨??
        // 인터페이스로 접근하며 자동으로 구현체로 접근된다
        return memberRepository.findAllMember(memberSearchCondition);
    }

    /**
     * 학생 1명 조회
     * @param memberSearchCondition
     * @return
     */
    public MemberFormDto findOne(MemberSearchCondition memberSearchCondition) {

//        if (StringUtils.hasText(memberSearchCondition.getStuNo())) {
//            Member.PkEnterAndSeq(memberSearchCondition);
//        }
        // 원래라면 히든 값을 넘겨받은 memberId로 findById 조회해야 하는데 이렇게하면 Member 객체를 반환해주므로 귀찮아짐
        // 따라서 그냥 커스텀 인터페이스를 통해서 받는게 속편함
        // 하지만 select 조회 할 때는 받은 memberId 값 사용하기!!
        // Ctrl + B == 호출한 곳으로 가기
        // Ctrl + Alt + B == 구현한 곳으로 가기
        MemberFormDto memberOne = memberRepository.findByOneCond(memberSearchCondition);

        // 아래부분 어떻게 고쳐야 할지 나중에 생각해보자
        memberOne.setFinSchregDivCd(FinSchregDivCd.findCodeName(memberOne.getFinSchregDivCd())); // 학적상태 CodeName 구하기
        memberOne.setSustCd(SustCd.findCodeName(memberOne.getSustCd())); // 학과 CodeName 구하기
        memberOne.setMjrCd(MjrCd.findCodeName(memberOne.getMjrCd())); // 전공 CodeName 구하기
        memberOne.getGender();    // 성별은 어떻게 할까?? 그냥 뿌릴까 말까 고민중
        return memberOne;
    }



    /**
     * <학적 상태 변경>
     *
     * 엔티티 안에 참조할 객체의 key 필드를 넣어 두는 것이 아니라 객체를 넣어둠으로써
     * 처음 한번 조회 후 바로 객체에 참조할 수 있는 것이 가장 큰 장점
     * (만약, 참조 객체의 key 필드만 가지고 있다면 key 필드로 한번 더 DB 조회 해야 하는 비효율이 있음)
     *
     * 근데, 업데이트 하니까 모든 컬럼들 다 업데이트 치는데 이거 어떻게 특정 컬럼만 업데이트 하게 하지?????????????????????
     */
    @Transactional
    public void statusUpdate(MemberFormDto memberFormDto) {
//        memberRepository.updateStatus(memberFormDto.getFinSchregDivCd(), memberFormDto.getStuNo()); // 이렇게 업데이트 직접 하지 말라고 했음
        Member member = memberRepository.findById(memberFormDto.getId()).get();
        member.changeStatus(memberFormDto.getFinSchregDivCd());
    }

    /**
     * <학적 모든 학생 조회 모달창>
     *
     * 이번에는 그냥 map을 통해 변환해서 사용해봄(커스텀 만들지 않고)
     * 대신, MemberFormDto 객체안에 파라미터로 Member 객체받아줄 임의의 생성자 필요하고 파라미터로 Member 객체 넘겨야함
     * 
     */
    public List<MemberFormDto> findMembers() {
        List<Member> findMembers = memberRepository.findAll();
        List<MemberFormDto> members = findMembers.stream()
                .map(member -> new MemberFormDto(member))   // MemberFormDto 객체에 임의의 생성자 필요하고 파라미터로 Member 객체 넘겨야함
                .collect(Collectors.toList());
        return members;
    }

    /**
     * <학생별 수강 신청 강좌 가져오기>
     *     
     * TODO: N+1 테스트 서비스
     *
     *  Key값이 다른 개설 강좌별로 조회가 각각 나감
     *  예: 강좌가 5개 있고 5개 다 수강신청 하고 여기서 조회 한다면 5개의 key값이 다 다르니 수강신청한 강좌 가져오기 위해 5번 쿼리가 나감
     *
     * 하지만!!! 여기서 글로벌로 default_batch_fetch_size: 100 를 설정해 놓으니 지연 로딩된 것들을 자동으로 인식해서 key를 묶어서 한번에 In 쿼리로 처리해줌
     * (지연 로딩 엔티티를 인식하여 key값들을 한번에 묶어 IN 쿼리로 날려줌)
     * 대신, 지연로딩 엔티티 별로 쿼리가 나가게 됨. 지연 로딩 엔티티 Join이 4개가 있다면 1 + 4번 더 나가는 것이다
     *
     * 여기는 Fetch Join으로 한번에 가져오는게 좋음 지금 보면 join 갯수만큼 쿼리가 더 나감(지금 메인 테이블 빼고 3번 나감)
     */
    public List<MemberSubjectDto> memberFindSubject(MemberSearchCondition memberSearchCondition) {
        List<MemberSubjectDto> memberSubjects = memberRepository.findSubjects(memberSearchCondition);
//        // N+1 테스트 반복문
        for (MemberSubjectDto memberSubject : memberSubjects) {
//            System.out.println("memberSubject.getMember().getStuNo() = " + memberSubject.getMember().getStuNo());
//            System.out.println("memberSubject.getOpenClass().getName() = " + memberSubject.getOpenClass().getName());
//            System.out.println("memberSubject.getShtmScore().getId() ====> " + memberSubject.getShtmScore().getId());
        }
        return memberSubjects;
    }

    /**
     * <학생 강좌별 중간, 기말 점수 등록>
     *
     * @Transactional 이거 안붙이니까 select만 되고 update 안나감 ㅋㅋㅋㅋㅋㅋ
     *
     */
    @Transactional
    public void scoreSaveOrUpdate(ScoreSaveForm scoreSaveForm) {
        // 강좌별 성적 등록 테이블에 값이 있다면 update
        for (int i = 0; i < scoreSaveForm.getShtmScoreNo().size(); i++) {
            System.out.println("수강신청한 강좌수만큼 엔티티 생성하는지 확인 ====> " + i);
            ShtmScore shtmScore = shtmScoreRepository.findById(scoreSaveForm.getShtmScoreNo().get(i)).get();
            shtmScore.updateScore(scoreSaveForm.getMidexamScr().get(i), scoreSaveForm.getFlexmScr().get(i));
            shtmScoreRepository.flush();
        }
    }

    /**
     * <성적 산출 등록>
     */
    @Transactional
    public void scoreReslutSave(ScoreResultForm scoreResultForm) {
        Member member = memberRepository.findById(scoreResultForm.getMemberId().get(0)).get();
        Float scoreSum = 0f;
        for (int i = 0; i < scoreResultForm.getShtmScoreNo().size(); i++) {
            scoreSum += Float.parseFloat(scoreResultForm.getMidexamScr().get(i)) + Float.parseFloat(scoreResultForm.getFlexmScr().get(i));
        }
        String result = String.valueOf(Optional.ofNullable(scoreSum/2).orElse(0f));  // 0이면 나누기 안되는것도 체크해야됨 원래
        String sum = String.valueOf(scoreSum);
        ShtmScoreResult shtmScoreResult = ShtmScoreResult.createShtmScoreResult(member, sum, result);
        shtmScoreResultRepository.save(shtmScoreResult);
    }

    /**
     * <성적 통계 조회>
     *
     * @Query("select m from Member m left join fetch m.shtmScoreResult s") 통해서 조회 후
     * MemberFormDto 에 담아서 반환하려고 했는데 취득학점, 평균평점 2개 컬럼 값이 null 로 나옴
     * fetch로 다 가져온 다음 MemberFormDto에 자동으로 필드 매핑되어 값이 들어가는게 아닌건가??
     * TODO: 취득학점, 평균평점 컬럼값 null로 되는거 나중에 해결하기
     */
    public List<MemberFormDto> scoreViewMembers() {
        List<MemberFormDto> viewMembers = memberRepository.findViewMembers();
        for (MemberFormDto viewMember : viewMembers) {
            System.out.println("viewMember = " + viewMember.toString());
            System.out.println("getAcqLctpt = " + viewMember.getAcqLctpt());
            System.out.println("getAcqLctptAvgAvrp = " + viewMember.getAcqLctptAvgAvrp());
        }
        return viewMembers;
    }

//========================================== API 관련 비지니스 로직 ==================================================//
    public List<MemberApiFormDto> findMembersApiV1() {
        return memberRepository.findAll().stream().map(member -> new MemberApiFormDto(member)).collect(Collectors.toList());
    }

    @Transactional
    public Long newMemberSaveApiV2(MemberApiSaveFormDto memberApiSaveFormDto) {
        // 원래는 이렇게 해야 하는데 설계가 살짝 잘못되어서 하나의 DTO만 받도록 되었음
        // 파라미터 부분을 인터페이스를 받도록 하고 구현체들을 넣는 방식으로 디자인 패턴을 바꾸면 될것 같지만 지금은 일단 하나 만들어서 함
        Member member = Member.createMemberApiV2(memberApiSaveFormDto);
        Member result = memberRepository.save(member);
        return result.getId();
    }

    @Transactional
    public Long updateMemberApiV3(Long memberId, MemberApiUpdateFormDto memberApiUpdateFormDto) {
        Member member = memberRepository.findById(memberId).get(); // 원래 api에서는 이렇게 꺼내면 안됨 머가 호출될지 모르니까
//        member.changeInfoApi(member, memberApiUpdateFormDto); // 이렇게 조회한 엔티티 자체를 넘겨서 안에서 member. 으로 값을 세팅해도 된다
        // 밑에 보면 member. 으로 update 메서드 실행하여 작업하는데 이게 중요함!!!
        // member 엔티티는 이미 한번 조회해서 1차캐시에 등록되어 있고 누군지도 알고 있음
        // 그렇기 때문에 member. 으로 싱글톤인 Member 엔티티에 들어가서 this.필드명 으로 update 처리해도 
        // 여기서 member 엔티티를 통해서 접근했기 때문에 어떤 Id인지 알고 있음
        return member.changeInfoApi(memberApiUpdateFormDto);
    }

    public Page<MemberApiFormDto> findMembersApiV4(Pageable pageable) {
        // 여기서 중요한 것은 DTO로 변환할 때 stream()은 빼고 그냥 map()만 써서 그대로 Page 타입으로 받아야 하는 것 중요!!!
        Page<MemberApiFormDto> result = memberRepository.findAll(pageable).map(m -> new MemberApiFormDto(m));
        return result;
    }

    public Page<MemberApiFormDto> findMembersApiV5(Pageable pageable) {
        Page<MemberApiFormDto> result = memberRepository.findMemberPagingQueryV5(pageable);
        return result;
    }

    public Page<MemberApiFormDto> findMembersApiV6(PageRequest pageRequest) {
        Page<MemberApiFormDto> result = memberRepository.findAll(pageRequest).map(m -> new MemberApiFormDto(m));
        return result;
    }

    public Page<MemberApiFormDto> findAndSearchMembersApiV7(MemberApiSearchCondition condition, Pageable pageable) {
        Page<MemberApiFormDto> result = memberRepository.findAndSearchApiPagingV7(condition, pageable);
        return result;
    }

    public Page<MemberApiFormDto> findAndSearchMembersApiV8(MemberApiSearchCondition condition, Pageable pageable) {
        Page<MemberApiFormDto> result = memberRepository.findAndSearchApiPagingV8(condition, pageable);
        return result;
    }
}/////
