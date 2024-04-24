package msa.reservation.service;

import lombok.RequiredArgsConstructor;
import msa.reservation.domain.entity.User;
import msa.reservation.exception.ApplicationException;
import msa.reservation.exception.ErrorCode;
import msa.reservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

//    @Value("${spring.encodeEmailSecretKey}")
//    private String emailKey = "pw";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }


    public User loadUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ApplicationException(ErrorCode.EMAIL_NOT_FOUND));
//        return userRepository.findByEmail(email+emailKey).orElseThrow(() ->
//                new ApplicationException(ErrorCode.EMAIL_NOT_FOUND));
    }
}
