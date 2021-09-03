package management.academic.Service;

import lombok.RequiredArgsConstructor;
import management.academic.dto.MemberDto;
import management.academic.entity.Member;
import management.academic.entity.Role;
import management.academic.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // lombok 라이브러리가 제공해주는 것(final이 붙은 필드를 가지고 자동으로 생성자 만들어줌)
@Transactional(readOnly = true) // 클래스 레벨에서 @Transactional 적용하고 readOnly 적용(조회 이외에 CRUD는 메서드 레벨에서 @Transactional을 다시 적용)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    /**
     * signUp(MemberDto memberDto) : form에서 입력받은 정보를 담은 MemberDto를 받아 password를 암호화를 해준 뒤
     * MemberDto를 Member객체로 변환하여 JPA를 통해 save()해줍니다.
     */
    @Transactional
    public Long signUp(MemberDto memberDto){
        // password를 암호화 한 뒤 dp에 저장
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        return memberRepository.save(memberDto.toEntity()).getId();
    }


    /**
     * loadUserBy(String ) : Spring Security가 제공하는 로그인을 사용하기 위해 UserDetailsService를 구현해주어야 합니다.
     * 로그인 form에서 입력받은 을 가지고 DB를 찾은 뒤 있다면 권한 정보를 추가해주어 UserDetails라는 객체로 반환을 해줍니다.
     */
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        // 로그인을 하기 위해 가입된 user정보를 조회하는 메서드
        Optional<Member> memberByWrapper = memberRepository.findByusername(username);
        Member member = memberByWrapper.get();

        System.out.println("member = " + member);

        List<GrantedAuthority> authorities = new ArrayList<>();

        // 여기서는 간단하게 이 'admin'이면 admin권한 부여
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        else{
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        // 아이디, 비밀번호, 권한리스트를 매개변수로 User를 만들어 반환해준다.
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}/////
