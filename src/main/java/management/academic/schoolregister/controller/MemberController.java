package management.academic.schoolregister.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import management.academic.schoolregister.dto.*;
import management.academic.schoolregister.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 여기 CommonController로 바꾸고 html도 경로 구조 다시 해서 나중에 다 맞추기
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * <학생 등록 화면 이동>
     *
     * @param model
     * @return ** DTO에 setter가 반드시 있어야 값을 자동으로 바인딩하여 저장할 수 있다 ㅡㅡ
     * <p>
     * 중요!!!!!!!!!!!!
     * 1. DTO Form 객체를 담아서 넘기는 것
     * 2. DTO에 Setter를 반드시 생성할 것
     */
    @GetMapping("/member/new")
    public String memberNewForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberNew";
    }

    /**
     * <학생 등록>
     *
     * @param memberFormDto
     * @param result
     * @return ** DTO에 setter가 반드시 있어야 값을 자동으로 바인딩하여 저장할 수 있다 ㅡㅡ
     */
    @PostMapping("/member/new")
    public String memberNewSave(@Valid MemberFormDto memberFormDto,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "member/memberNew";
        }
        // 회원 저장
        memberService.newMemberSave(memberFormDto);
        return "main";
    }

    /**
     * <모든 학생 조회>
     *
     * @param model
     * @return ** BindingResult는 반드시 @ModelAttribute의 뒤에 선언해야 한다.
     */
    @GetMapping("/member/all/info")
    public String memberAllFind(@ModelAttribute("memberSearchCondition") MemberSearchCondition memberSearchCondition,
                                Model model) {
//        model.addAttribute("memberSearchCondition", new MemberSearchCondition());
        log.info("모든 학생 조회 Controller =======>");
        List<MemberFormDto> members = memberService.findAllMember(memberSearchCondition);
        model.addAttribute("members", members);
        return "member/memberAllInfo";
    }

    /**
     * <학생 조회 화면 이동>
     *
     * @param model
     * @return ** DTO에 setter가 반드시 있어야 값을 자동으로 바인딩하여 저장할 수 있다 ㅡㅡ
     * <p>
     * ** 한 화면에서 여러 Form을 사용하고 싶다면 이렇게 여러개 객체를 넘기면 끝남 ㅡㅡ
     * ** 받는 View 단에서도 아래처럼 2개 나눠서 받으면 된다
     * <form role="form" th:action="@{/member/info}" th:object="${memberSearchCondition}" method="post"></form>
     * <form role="form" th:object="${memberDto}"></form>
     */
    @GetMapping("/member/info")
    public String memberInfoForm(Model model) {
        model.addAttribute("memberSearchCondition", new MemberSearchCondition());
        model.addAttribute("memberDto", new MemberFormDto());
        return "member/memberInfo";
    }

    /**
     * <학생 조회>
     * 이거 필요 없음 위에꺼 하나로만 다 맵핑 가능
     *
     * @param memberSearchCondition
     * @param
     * @return ** DTO에 setter가 반드시 있어야 값을 자동으로 바인딩하여 저장할 수 있다 ㅡㅡ
     * <p>
     * ** BindingResult는 반드시 @ModelAttribute의 뒤에 선언해야 한다.
     */
    @PostMapping("/member/info")
    public String memberInfo(@Valid MemberSearchCondition memberSearchCondition,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "member/memberInfo";
        }
        if (StringUtils.hasText(memberSearchCondition.getStuNo())) { // 이름이 넘어왔다면 select 진행
            MemberFormDto memberDto = memberService.findOne(memberSearchCondition);
            model.addAttribute("memberDto", memberDto);
            return "member/memberInfo";
        }
        return "member/memberInfo";
    }

    /**
     * <학적 상태 변경 화면 이동>
     */
    @GetMapping("/member/status/change")
    public String memberStatusChange(Model model) {
        model.addAttribute("memberSearchCondition", new MemberSearchCondition());
        model.addAttribute("memberDto", new MemberFormDto());
        return "member/statusChange";
    }

    /**
     * <학적 상태 변경할 학생 조회>
     */
    @PostMapping("/member/status/change")
    public String memberStatusChangeSave(@Valid MemberSearchCondition memberSearchCondition,
                                         MemberFormDto memberFormDto,
                                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "member/statusChange";
        }
        if (StringUtils.hasText(memberSearchCondition.getStuNo())) { // 이름이 넘어왔다면 select 진행
            MemberFormDto memberDto = memberService.findOne(memberSearchCondition);
//            model.addAttribute("test", memberSearchCondition.getUsername());
            model.addAttribute("memberDto", memberDto);
            return "member/statusChange";
        }
        return "member/statusChange";
    }

    /**
     * <학적 상태 변경 저장>
     * <p>
     * !!!! 나중에 위에 조회 하는거 2개 1개로 합쳐서 이거랑 조회 한개만 가지고 운영하도록 리팩토링 하기
     */
    @PostMapping("/member/status/change/save")
    public String changeSave(MemberFormDto memberFormDto) {
        System.out.println("memberFormDto.getId() ===> " + memberFormDto.getId());
        memberService.statusUpdate(memberFormDto);
        return "redirect:/member/status/change";
    }

    /**
     * <강좌별 성적 등록 화면 이동>
     * <p>
     * TODO: 성적 관리 모듈들은 원래 따로 패키지 따서 관리해야 함
     */
    @GetMapping("/member/score/insert")
    public String memberScoreInsertForm(Model model) {
//        model.addAttribute("tempScores", new TempScore());
        return "member/memberScoreInsert";
    }

    /**
     * <강좌별 성적 등록 모달창 값 전달>
     * TODO: Modal 모듈 공통으로 빼야 함
     */
    @GetMapping("/member/score/insert/modal")
    public String findMemberModal(Model model) {
        List<MemberFormDto> members = memberService.findMembers(); // 학적도 불러와서 뿌려줘야 하는데 너무 책임범위를 벗어나는데ㅠㅠ
        model.addAttribute("members", members);
//        return members;   // 이거랑 @ResponseBody 이거는 Json 형식의 API 보낼 때 사용
        return "member/memberModal";
    }


    /**
     * <강좌별 성적 등록 학생별 수강신청한 강좌 조회>
     * <p>
     * TODO: 이렇게 다른 컴포넌트의 서비스를 바라보는게 더 좋을까 아님 중복코드라도 모듈화 하는게 좋을까??(컨디션을 넘겨야 하니까 그냥 memberservice에 넣어야 할듯)
     */
    @GetMapping("/member/subject")
    public String memberSubject(@ModelAttribute("memberSearchCondition") MemberSearchCondition memberSearchCondition,
                                Model model) {
        List<MemberSubjectDto> subjects = memberService.memberFindSubject(memberSearchCondition);
        model.addAttribute("subjects", subjects);
        return "member/memberScoreInsert";
    }


    /**
     * <강좌별 성적 등록 저장>
     * <p>
     * TODO: 성적 관리 모듈들은 원래 따로 패키지 따서 관리해야 함
     */
    @PostMapping("/member/score/insert")
    public String memberScoreInsert(@ModelAttribute("tempScores") ScoreSaveForm scoreSaveForm) {
        System.out.println("scoreSaveForm ====> " + scoreSaveForm.toString());
        memberService.scoreSaveOrUpdate(scoreSaveForm);
        return "redirect:/member/score/insert";
    }

    /**
     * <성적 산출 화면 이동>
     */
    @GetMapping("/member/score/result")
    public String scoreResultForm() {
        // 학생별로 성적 등록 데이터 뿌리기
        return "member/scoreResult";
    }

    /**
     * <강좌별 성적 등록 학생별 수강신청한 강좌 조회>
     * <p>
     * TODO: 이렇게 다른 컴포넌트의 서비스를 바라보는게 더 좋을까 아님 중복코드라도 모듈화 하는게 좋을까??(컨디션을 넘겨야 하니까 그냥 memberservice에 넣어야 할듯)
     */
    @GetMapping("/member/score/findMember")
    public String memberSubjectFindMember(@ModelAttribute("memberSearchCondition") MemberSearchCondition memberSearchCondition,
                                          Model model) {
        List<MemberSubjectDto> subjects = memberService.memberFindSubject(memberSearchCondition);
        model.addAttribute("subjects", subjects);
        return "member/scoreResult";
    }

    /**
     * <성적 산출 저장>
     */
    @PostMapping("/member/score/result")
    public String scoreResult(ScoreResultForm scoreResultForm) {
        System.out.println("scoreResultForm = " + scoreResultForm.toString());
        memberService.scoreReslutSave(scoreResultForm);
        return "member/scoreResult";
    }

    /**
     * <성적 통계 화면 이동>
     */
    @GetMapping("/member/score/view")
    public String socreViewForm(Model model){
        List<MemberFormDto> members = memberService.scoreViewMembers();
        model.addAttribute("members", members);
        return "member/scoreView";
    }
}/////
