package management.academic.college.controller;

import lombok.RequiredArgsConstructor;
import management.academic.college.dto.CollegeFormDto;
import management.academic.college.service.CollegeService;
import management.academic.schoolregister.dto.MemberFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;

    /**
     * <개설 강좌 화면 이동>
     */
    @GetMapping("/college/new/subject")
    public String newSubject(@ModelAttribute("collegeFormDto") CollegeFormDto collegeFormDto) {
        return "college/collegeNew";
    }

    /**
     * <개설 강좌 저장>
     */
    @PostMapping("/college/new/subject")
    public String newSubjectSave(@Valid @ModelAttribute("collegeFormDto") CollegeFormDto collegeFormDto,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "college/collegeNew";
        }
        System.out.println("collegeFormDto ===> " + collegeFormDto.toString());
        collegeService.newSubject(collegeFormDto);
        return "redirect:/college/new/subject";
    }

    /**
     * <수강 신청 화면 이동>
     * 
     * JPA에서 OpenClass 엔티티로 받아 DTO로 변환하지 않고 Custom 인터페이스 통해서 바로 DTO 객체에 담아서 리턴받음
     * 
     * Custom 인터페이스 사용하지 않는다면 엔티티로 직접 조회 후 map을 통해 DTO로 변환하는 중간과정이 필요함
     *   List<OrderDto> collect = orders.stream()
     *      .map(o -> new OrderDto(o))              // map() : 변환할 때 사용하는 함수
     *      .collect(Collectors.toList());
     */
    @GetMapping("/college/join/subject")
    public String joinSubject(Model model) {
        List<CollegeFormDto> subjects = collegeService.findSubject();
        model.addAttribute("subjects", subjects);
        return "college/collegeJoin";
    }

    /**
     * <수강 신청 화면 모달창 값 전달>
     *
     * TODO: 여기 부분 MemberService를 가져와서 의존하고 그걸 그냥 사용(이렇게 하니까 어디부분이 사용되는지 눈에 안보임)
     *
     */
    @GetMapping("/college/find/member/modal")
    public String findMemberModal(Model model){
        List<MemberFormDto> members = collegeService.findAllMember(); // 학적도 불러와서 뿌려줘야 하는데 너무 책임범위를 벗어나는데ㅠㅠ
        model.addAttribute("members", members);
//        return members;   // 이거랑 @ResponseBody 이거는 Json 형식의 API 보낼 때 사용
        return "college/moaModal";
    }


    /**
     * 수강 신청 저장
     *
     * Id만 넘기면 한번 Id에 맞는 개설 강좌를 한번 조회 후 그다음 수강신청 엔티티에 값을 저장하게 되고
     * form 형태로 데이터를 다 넘기면 바로 수강 신청 엔티티 생성새허 1번만 쿼리 날리면 됨
     *
     * 근데, 여기서는 모든 데이터를 다 넘길 수 없음 ==> 이유는 View단에 list로 뿌려지고 있는데 form으로 다 가져오면 이상하니까
     */
    @PostMapping("/college/{openClassNo}/submit")
    public String joinSubmit(@PathVariable("openClassNo") Long openClassNo,
                             @RequestParam("memberId") Long memberId,
                             @RequestParam("stuNo") String stuNo
                            ){
        System.out.println("openClassNo =======> "+ openClassNo);
        System.out.println("memberId =======> "+ memberId);
        System.out.println("stuNo =======> "+ stuNo);
        collegeService.memberSubmit(openClassNo, memberId, stuNo);
        return "redirect:/college/join/subject";
    }

    /**
     * 수강 신청 취소
     * 
     * 수강 신청 취소 하면 수강 신청 엔티티, 강좌별 성적 등록 엔티티 둘다 삭제 해야 함
     */
//    @GetMapping("/college/{id}/submit")
//    public String collegeRemove(@PathVariable("id") Long openClassNo, Model model) {
//        return "college/collegeJoin";
//    }
}/////
