package msa.reservation.config;

import lombok.RequiredArgsConstructor;
import msa.reservation.config.filter.JwtTokenFilter;
import msa.reservation.config.filter.LoginFilter;
import msa.reservation.exception.CustomAuthenticationEntryPoint;
import msa.reservation.service.CustomUserDetailService;
import msa.reservation.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Value("${jwt.secret-key}")
    private String key;
    private final CustomUserDetailService userDetailService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenUtils jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/*/users/join","/api/*/users/login","/h2-console/**","/api/*/mail/**").permitAll()
//                        .requestMatchers("/api/*/users/mypage").hasRole("USER")
                        .requestMatchers("/api/**").authenticated()   // 위의 api를 제외한 나머지는 모두 인증 필요
                )
//                // h2를 위한 작업
//                .headers(he -> he
//                        .frameOptions(fr -> fr.disable()))


                .addFilterBefore(new JwtTokenFilter(key,userDetailService), LoginFilter.class)
                .addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),UsernamePasswordAuthenticationFilter.class)

                // 세션 따로 관리 안함
                .sessionManagement(se -> se
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT Token Exception Handing
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))

                .build();
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}