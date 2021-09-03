package management.academic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import management.academic.Service.MemberService;
import management.academic.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 여기 CommonController로 바꾸고 html도 경로 구조 다시 해서 나중에 다 맞추기
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 초기화면
    @GetMapping("/")
    public String index() {
        log.info("MemberController--index page");
        return "index";
    }

    // 로그인 성공 후 메인 페이지 이동
    // .defaultSuccessUrl("/main") 처럼 해놓으면 자동으로 이동할줄 알았는데 아니였음
    // 서블릿 컨테이너단 Filter에서 로그인 시큐리티 통해서 토큰 생성 후 로그인 성공 하면 컨트롤러를 태워야 하기 때문에 이게 있어야 함
    @GetMapping("/main")
    public String main() {
        log.info("메인페이지 이동");
        return "main";
    }

    // 회원가입 화면
    @GetMapping("/member/signup")
    public String signupForm(Model model) {
        model.addAttribute("member",new MemberDto());
        return "member/signupForm";
    }

    // 회원가입 저장
    @PostMapping("/member/signup")
    public String signup(MemberDto memberDto) {
        log.info("memberDto ====>", memberDto);
        memberService.signUp(memberDto);
        return "redirect:/";
    }

}/////
