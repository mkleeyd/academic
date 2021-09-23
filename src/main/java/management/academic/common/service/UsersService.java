package management.academic.common.service;

import lombok.RequiredArgsConstructor;
import management.academic.common.dto.UsersDto;
import management.academic.common.entity.Role;
import management.academic.common.entity.Users;
import management.academic.common.repository.UsersRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // lombok 라이브러리가 제공해주는 것(final이 붙은 필드를 가지고 자동으로 생성자 만들어줌)
@Transactional(readOnly = true) // 클래스 레벨에서 @Transactional 적용하고 readOnly 적용(조회 이외에 CRUD는 메서드 레벨에서 @Transactional을 다시 적용)
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    /**
     * signUp(UsersDto usersDto) : form에서 입력받은 정보를 담은 UsersDto를 받아 password를 암호화를 해준 뒤
     * UsersDto를 User객체로 변환하여 JPA를 통해 save()해줍니다.
     */
    @Transactional
    public Long signUp(UsersDto usersDto){
        // password를 암호화 한 뒤 dp에 저장
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usersDto.setPassword(passwordEncoder.encode(usersDto.getPassword()));

        return usersRepository.save(usersDto.toEntity()).getId();
    }


    /**
     * loadUserBy(String ) : Spring Security가 제공하는 로그인을 사용하기 위해 UserDetailsService를 구현해주어야 합니다.
     * 로그인 form에서 입력받은 을 가지고 DB를 찾은 뒤 있다면 권한 정보를 추가해주어 UserDetails라는 객체로 반환을 해줍니다.
     */
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        // 로그인을 하기 위해 가입된 user정보를 조회하는 메서드
        Optional<Users> usersByWrapper = usersRepository.findByusername(username);
        Users users = usersByWrapper.get(); // 이렇게 꺼내는건 안좋은 습관

        List<GrantedAuthority> authorities = new ArrayList<>();

        // 여기서는 간단하게 이 'admin'이면 admin권한 부여
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        else{
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        // 아이디, 비밀번호, 권한리스트를 매개변수로 User를 만들어 반환해준다.
        return new User(users.getUsername(), users.getPassword(), authorities);
    }
}/////
