package management.academic.common.controller;

import lombok.RequiredArgsConstructor;
import management.academic.common.dto.UsersDto;
import management.academic.common.entity.Users;
import management.academic.common.repository.UsersRepository;
import management.academic.common.service.JwtTokenProvider;
import management.academic.common.service.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsersService usersService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;

    // 초기화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 회원가입 화면
    @GetMapping("/common/signup")
    public String signupForm(Model model) {
        model.addAttribute("users",new UsersDto());
        return "common/signupForm";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(UsersDto usersDto) {
        System.out.println("usersDto = " + usersDto.toString());

        Users users = Users.builder()
                .username(usersDto.getUsername())
                .password(passwordEncoder.encode(usersDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build();
        usersService.signUp(users);
        return "redirect:/";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        System.out.println("username = " + username);
        System.out.println("password = " + password);

        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        String token = jwtTokenProvider.createToken(users.getUsername(), users.getRoles());
        System.out.println("token = " + token);
        return "main";
    }




//============================================================================================ Login Start



//    // 로그인 성공 후 메인 페이지 이동
//    // .defaultSuccessUrl("/main") 처럼 해놓으면 자동으로 이동할줄 알았는데 아니였음
//    // 서블릿 컨테이너단 Filter에서 로그인 시큐리티 통해서 토큰 생성 후 로그인 성공 하면 컨트롤러를 태워야 하기 때문에 이게 있어야 함
//    @GetMapping("/main")
//    public String main() {
//        return "main";
//    }
//
//    // 회원가입 화면
//    @GetMapping("/common/signup")
//    public String signupForm(Model model) {
//        model.addAttribute("users",new UsersDto());
//        return "common/signupForm";
//    }
//
//    // 회원가입 저장
//    @PostMapping("/common/signup")
//    public String signup(UsersDto usersDto) {
//        System.out.println("usersDto ===> " + usersDto.toString());
//        usersService.signUp(usersDto);
//        return "redirect:/";
//    }

//============================================================================================ Login End

}
