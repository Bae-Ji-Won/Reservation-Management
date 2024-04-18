package msa.reservation.service;

import lombok.RequiredArgsConstructor;
import msa.reservation.repository.EmployeeEntity;
import msa.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public String getUsernameById(Long id) {
        EmployeeEntity employee = userRepository.getFirstById(id);
        return employee.getName();
    }
}
