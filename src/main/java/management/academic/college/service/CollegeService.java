package management.academic.college.service;

import lombok.RequiredArgsConstructor;
import management.academic.college.dto.CollegeFormDto;
import management.academic.college.entity.Enrolment;
import management.academic.college.entity.OpenClass;
import management.academic.college.repository.CollegeRepository;
import management.academic.college.repository.EnrolmentRepository;
import management.academic.schoolregister.dto.MemberFormDto;
import management.academic.schoolregister.entity.Member;
import management.academic.schoolregister.entity.ShtmScore;
import management.academic.schoolregister.repository.ShtmScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final EnrolmentRepository enrolmentRepository;

    private final ShtmScoreRepository shtmScoreRepository;  // 책임범위 넘어간거 갠찮은지 현업가서 확인

    /**
     * 개설 강좌 등록
     */
    @Transactional
    public Long newSubject(CollegeFormDto collegeFormDto) {
        // OpenClass 엔티티 안에서 Code, 채번 따고 OpenClass 엔티티로 생성해서 리턴
        OpenClass openClass = OpenClass.createSubject(collegeFormDto);
        // openClass 값 저장
        return collegeRepository.save(openClass).getId();
    }

    /**
     * <수강 신청할 강좌들 조회>
     */
    public List<CollegeFormDto> findSubject() {
        return collegeRepository.findAllSubject();
    }

    /**
     * <수강 신청할 학생들 조회>
     *     
     * 책임범위를 벗어난 기능
     */
    public List<MemberFormDto> findAllMember() {
        return collegeRepository.findMembers();
    }

    /**
     * <수강 신청>
     *
     *
     * Optional<T>
     *     - Optional을 사용하면 Null을 생각하지 않고 코딩할 수 있게 해준다
     *     - Optional을 사용하여 1개의 원소를 가지고 있는 특별한 Stream 처럼 사용할 수 있다
     *     - map() 은 타입을 변환해주는 함수이다
     *     - filter() 는 조건문 유사한 함수이다(조건으로 필터링을 한다는 의미)
     *
     *     - ofNullable() : null일 수도 있는 경우에 사용
     *     - 사용방법 : Optional.ofNullable().map().체인...;
     *
     *     - Object obj = objectOptional.map(Object::getObject)
     *                      .map(Object::getObject)
     *                      .orElse(null); // orElse(new Object), orElseGet(Object::new)
     *
     *     - Optional 내부에있는 Object에 getter를 연속적으로 호출한다. 메서드가 진행되면서 중간에 null이 발견되면 NPE없이 orElse()가 호출되게된다.
     *     - 어차피 값이 1개뿐인 filter보다 Optional에서는 map이 거의 주로 사용될 듯하다.
     *
     *     - String name = Optional.ofNullable(GetName()).orElse("No name"); <== 또다른 예제
     */
    @Transactional
    public void memberSubmit(Long openClassNo, Long memberId, String stuNo) {
        OpenClass openClass = collegeRepository.findById(openClassNo).get();
        Member member = collegeRepository.findMember(memberId, stuNo);
        // 수강 신청 엔티티 생성
        Enrolment enrolment = Enrolment.createEnrolment(openClass, member);
        Enrolment SaveEnrolment = enrolmentRepository.save(enrolment);
        // 수강 신청 하면 강좌별 성적 등록 엔티티도 생성
        ShtmScore shtmScore = ShtmScore.createShtmScore(member, SaveEnrolment);
        shtmScoreRepository.save(shtmScore);
    }

}
