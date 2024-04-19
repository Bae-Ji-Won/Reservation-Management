package msa.reservation.service;

import lombok.RequiredArgsConstructor;
import msa.reservation.domain.entity.User;
import msa.reservation.dto.UserDto;
import msa.reservation.dto.request.UserJoinRequest;
import msa.reservation.exception.ApplicationException;
import msa.reservation.exception.ErrorCode;
import msa.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User join(UserJoinRequest request){
        userRepository.findByUserName(request.userName()).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        });
        User user = userRepository.save(User.of())
    }
}
