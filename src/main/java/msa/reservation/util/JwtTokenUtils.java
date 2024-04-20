package msa.reservation.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {


    public static String getuserName(String token,String key){
        return extractClaims(token,key).get("email",String.class);
    }

    // 토큰 시간 만료되었는지 확인
    public static boolean isExpired(String token,String key){
        Date expiredDate = extractClaims(token,key).getExpiration();
        return expiredDate.before(new Date());
    }

    // 외부에서 받아온 토큰을 우리가 발행한 토큰인지 확인하기 위해 Key 값을 비교함
    private static Claims extractClaims(String token, String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key))      // 외부 토큰 키값과 내가 발행한 키값이 같은지 비교 다를시, 예외 발생
                .build().parseClaimsJws(token).getBody();           // 키 값이 같다면 Claims 객체 생성 (외부에서 받아온 토큰안에 들어있는 정보를 담고 있음)
    }

    // 토큰에 정보 넣기
    public static String generateToken(String email, String key, long expiredTimeMs){
        // claims에 username 저장
        Claims claims = Jwts.claims();
        claims.put("email",email);

        return Jwts.builder()
                // 위에서 만든 claims을 토큰에 넣고
                .setClaims(claims)
                // 현재 토큰 발행 시간 넣고
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 토큰 만료 시간 적고
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
