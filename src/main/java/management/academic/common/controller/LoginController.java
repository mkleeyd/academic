package management.academic.common.controller;

import lombok.RequiredArgsConstructor;
import management.academic.common.dto.UsersDto;
import management.academic.common.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsersService usersService;

//============================================================================================ Login Start

    // 초기화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 성공 후 메인 페이지 이동
    // .defaultSuccessUrl("/main") 처럼 해놓으면 자동으로 이동할줄 알았는데 아니였음
    // 서블릿 컨테이너단 Filter에서 로그인 시큐리티 통해서 토큰 생성 후 로그인 성공 하면 컨트롤러를 태워야 하기 때문에 이게 있어야 함
    @GetMapping("/main")
    public String main() {
        return "main";
    }

    // 회원가입 화면
    @GetMapping("/common/signup")
    public String signupForm(Model model) {
        model.addAttribute("users",new UsersDto());
        return "common/signupForm";
    }

    // 회원가입 저장
    @PostMapping("/common/signup")
    public String signup(UsersDto usersDto) {
        usersService.signUp(usersDto);
        return "redirect:/";
    }

//============================================================================================ Login End

}
