package management.academic.schoolregister.service;

import lombok.RequiredArgsConstructor;
import management.academic.common.entity.register.FinSchregDivCd;
import management.academic.common.entity.register.MjrCd;
import management.academic.common.entity.register.SustCd;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.dto.MemberSearchCondition;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

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

        if (StringUtils.hasText(memberSearchCondition.getStuNo())) {
            Member.PkEnterAndSeq(memberSearchCondition);
        }
        MemberFormDto memberOne = memberRepository.findByOneCond(memberSearchCondition);
        memberOne.setStuNo(memberSearchCondition.getEnterYear() + memberSearchCondition.getStuNo());   // 조회 후 다시 원래 값으로 돌려놓기
        memberOne.setFinSchregDivCd(FinSchregDivCd.findCodeName(memberOne.getFinSchregDivCd())); // 학적상태 CodeName 구하기
        memberOne.setSustCd(SustCd.findCodeName(memberOne.getSustCd())); // 학과 CodeName 구하기
        memberOne.setMjrCd(MjrCd.findCodeName(memberOne.getMjrCd())); // 전공 CodeName 구하기
//        memberOne.getGender();    // 성별은 어떻게 할까?? 그냥 뿌릴까 말까 고민중
        return memberOne;
    }



    /**
     * <학적 상태 변경>
     */
    @Transactional
    public void statusUpdate(MemberFormDto memberFormDto) {
        Member.memberWhereStuNo(memberFormDto); // 조회가 아닌 일반 input값에서 학번 받아와 추출하기
        Member member = memberRepository.findMember(memberFormDto.getEnterYear(), memberFormDto.getStuNo());
        member.changeStatus(memberFormDto.getFinSchregDivCd());
    }
}
