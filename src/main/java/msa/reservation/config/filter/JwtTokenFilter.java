package msa.reservation.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.reservation.domain.entity.User;
import msa.reservation.service.CustomUserDetailService;
import msa.reservation.util.JwtTokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final CustomUserDetailService userDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("---------------------------------------------------------------1111");
        // 외부에서 받아온 토큰 분석
        // get header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Bearer로 시작하지 않다면 정상적인 토큰이 아님
        if(header == null || !header.startsWith("Bearer ")){
            log.error("JWT Header not normal");
            filterChain.doFilter(request,response);
            return;
        }

        // 정상적인 토큰이라면
        try{

            final String token = header.split(" ")[1].trim();

            // TODO: 여기서 에러 발생함
            // 토큰의 유효 시간 체크
            if(JwtTokenUtils.isExpired(token,key)){
                log.error("JWT KEY is expired");
                filterChain.doFilter(request,response);
                return;
            }

            // 토큰에서 이메일 추출
            String email =JwtTokenUtils.getEmail(token,key);


            User userDetails = userDetailService.loadUserByEmail(email);
            // UsernamePasswordAuthenticationToken : 사용자가 이미 인증되었을 때, 사용자의 인증 정보와 권한을 스프링 시큐리티 컨텍스트에 등록하는데 사용됨
            //                                       애플리케이션 내에서 사용자가 인증되었음을 인식하고, 사용자의 권한에 따라 접근 제어 가능
            /* UsernamePasswordAuthenticationToken(1,2,3)
                1. Entity 객체
                2. Credentials로, 이미 인증된 사용자의 정보를 바탕으로 토큰을 생성하는 상황에서는 비밀번호 정보 필요 X하며 null
                3. Authorities로, 사용자가 가진 권한을 나타냄 (이전에 권한 종류들을 만들었던것) - User Entity에 getAuthorities 메서드로 구분하여 사용함
             */
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,null, userDetails.getAuthorities());
            /*
            WebAuthenticationDetailsSource : 웹 요청으로부터 인증 세부 정보를 생성하는 데 사용
            buildDetails : 실제 웹 요청(HttpServletRequest 객체)로부터 인증 세부 정보를 생성
            authentication.setDetails : UsernamePasswordAuthenticationToken객체의 details 속성에 설정

            사용자가 로그인을 시도할 때, 사용자의 IP 주소나 세션 ID 같은 정보를 함께 추적하고 싶다면,
            WebAuthenticationDetails를 사용하여 이러한 정보를 Authentication 객체에 담을 수 있습니다.
             */
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // SecurityContextHolder.getContext()안에 autentication값 넣어둠
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (RuntimeException e){
            log.error("JWT valid Error: {}",e.toString());
            filterChain.doFilter(request,response);
            return;
        }

        filterChain.doFilter(request,response);
    }


}
