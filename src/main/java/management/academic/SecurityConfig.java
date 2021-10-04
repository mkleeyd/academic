package management.academic;

import lombok.AllArgsConstructor;
import management.academic.common.service.JwtAuthenticationFilter;
import management.academic.common.service.JwtTokenProvider;
import management.academic.common.service.UsersService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * <Spring Security를 사용하기 위해서 Config파일을 작성하여 필요한 메서드들을 오버라이드 하기>
 *
 * @Configuration : config Bean이라는 것을 명시해주는 annotation입니다.
 *
 * @EnableWebSecurity : Spring Security config를 할 클래스라는 것을 명시해줍니다.
 *
 * WebSecurityConfigurerAdapter를 상속받아 필요한 메서드를 구현하여 필요한 설정을 해줍니다.
 *
 * PasswordEncoder : 입력받은 비밀번호를 그대로 DB에 저장하는 것이 아니고 암호화를 해서 저장을 해주어야 합니다. 따라서 이러한 암호화를 해주는 메서드로 다른 곳에서 사용할 수 있도록 @Bean으로 등록을 해줍니다.
 *
 * BCryptPasswordEncoder() : password 암호화 방법 중 한 가지입니다.
 *
 * configure(WebSecurity web) : WebSecurity는 FilterChainProxy를 생성하는 필터로서 ignoring() 을 사용하여 Spring Security가 무시할 수 있도록 설정을 할 수 있습니다. 파일의 기준 경로는 resources/static이라고 합니다.
 *
 * configure(HttpSecurity http) : HttpSecurity는 Http로 들어오는 요청에 대하여 보안을 구성할 수 있는 클래스로 authorizeRequests(), formLogin(), logout(), exceptionHandling()과 같은 메서드들을 이용해 로그인에 대한 설정을 해줍니다.
 *
 * configure(AuthenticationManagerBuilder auth) : AuthenticationManagerBuilder는 Spring Security의 모든 인증을 관리하는 AuthenticationManager를 생성하는 클래스로 UserDetailService를 통해 유저의 정보를 memberService에서 찾아 담아줍니다. 그리고 passwordEncoder로는 앞에서 Bean으로 등록한 passwordEncoder()를 사용하겠다고 설정을 해줍니다.
 *
 * 참고 : https://spring.io/guides/gs/securing-web/
 */
//@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private UsersService usersService;
    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 인증을 무시하기 위한 설정
        web.ignoring().antMatchers("/css/**","/js/**","/img/**","/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("USER") //or .roles("USER")
//                .and()
                .withUser("admin").password("admin").roles("USER"); //or .roles("USER")
    }
}