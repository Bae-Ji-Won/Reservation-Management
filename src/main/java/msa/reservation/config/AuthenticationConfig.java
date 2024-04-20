package msa.reservation.config;

import lombok.RequiredArgsConstructor;
import msa.reservation.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig{

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/*/users/join","/api/*/users/login","/h2-console/**","/api/*/mail/**").permitAll()
                        .requestMatchers("/api/**").authenticated()   // 위의 api를 제외한 나머지는 모두 인증 필요
                )
                // h2를 위한 작업
                .headers(he -> he
                        .frameOptions(fr -> fr.disable()))
                // 세션 따로 관리 안함
                .sessionManagement(se -> se
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }
}